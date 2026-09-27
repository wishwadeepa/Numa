package com.example.numa.feature.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.numa.data.NoteRepository
import com.example.numa.data.local.Note
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class NoteEditorViewModel(
    private val repository: NoteRepository,
    private val noteId: String? = null
) : ViewModel() {

    private val currentNoteId = noteId ?: UUID.randomUUID().toString()

    private var createdAt = System.currentTimeMillis()

    private var autosaveJob: Job? = null

    // ---------------------------------------------------------
    // Prevent a deleted note from being saved again
    // ---------------------------------------------------------

    private var isDeleted = false

    // ---------------------------------------------------------
    // Editor state
    // ---------------------------------------------------------

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title.asStateFlow()

    private val _content = MutableStateFlow("")
    val content: StateFlow<String> = _content.asStateFlow()

    init {
        if (noteId != null) {
            loadExistingNote(noteId)
        }
    }

    // ---------------------------------------------------------
    // Load existing note
    // ---------------------------------------------------------

    private fun loadExistingNote(id: String) {

        viewModelScope.launch {

            val note = repository.getNote(id)

            if (note != null) {
                _title.value = note.title
                _content.value = note.content
                createdAt = note.createdAt
            }
        }
    }

    // ---------------------------------------------------------
    // Title
    // ---------------------------------------------------------

    fun onTitleChanged(newTitle: String) {

        if (isDeleted) return

        _title.value = newTitle

        scheduleAutosave()
    }

    // ---------------------------------------------------------
    // Content
    // ---------------------------------------------------------

    fun onContentChanged(newContent: String) {

        if (isDeleted) return

        _content.value = newContent

        scheduleAutosave()
    }

    // ---------------------------------------------------------
    // Autosave
    // ---------------------------------------------------------

    private fun scheduleAutosave() {

        if (isDeleted) return

        autosaveJob?.cancel()

        autosaveJob = viewModelScope.launch {

            delay(500)

            saveNote()
        }
    }

    // ---------------------------------------------------------
    // Save immediately
    // ---------------------------------------------------------

    fun saveNow() {

        // IMPORTANT:
        // A deleted note must never be recreated.
        if (isDeleted) return

        autosaveJob?.cancel()

        viewModelScope.launch {
            saveNote()
        }
    }

    // ---------------------------------------------------------
    // Save note
    // ---------------------------------------------------------

    private suspend fun saveNote() {

        // Extra safety check
        if (isDeleted) return

        val currentTitle = _title.value
        val currentContent = _content.value

        // Don't save completely empty notes
        if (
            currentTitle.isBlank() &&
            currentContent.isBlank()
        ) {
            return
        }

        val note = Note(
            id = currentNoteId,
            title = currentTitle,
            content = currentContent,
            createdAt = createdAt,
            updatedAt = System.currentTimeMillis()
        )

        repository.saveNote(note)
    }

    // ---------------------------------------------------------
    // Delete note
    // ---------------------------------------------------------

    fun deleteNote(
        onDeleted: () -> Unit
    ) {

        // Mark it deleted FIRST.
        // This is important because the editor may immediately
        // be disposed after this.
        isDeleted = true

        // Stop any pending autosave.
        autosaveJob?.cancel()

        viewModelScope.launch {

            repository.deleteNote(currentNoteId)

            onDeleted()
        }
    }

    override fun onCleared() {

        autosaveJob?.cancel()

        super.onCleared()
    }
}