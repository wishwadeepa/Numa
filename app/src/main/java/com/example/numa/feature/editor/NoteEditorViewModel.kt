package com.example.numa.feature.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.numa.data.NoteRepository
import com.example.numa.data.local.Note
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class NoteEditorViewModel(
    private val repository: NoteRepository,
    existingNoteId: String? = null
) : ViewModel() {

    private val noteId = existingNoteId ?: UUID.randomUUID().toString()

    private var createdAt = System.currentTimeMillis()

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title.asStateFlow()

    private val _content = MutableStateFlow("")
    val content: StateFlow<String> = _content.asStateFlow()

    private var autosaveJob: Job? = null

    init {
        if (existingNoteId != null) {
            loadExistingNote(existingNoteId)
        }
    }

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

    fun onTitleChanged(newTitle: String) {
        _title.value = newTitle
        scheduleAutosave()
    }

    fun onContentChanged(newContent: String) {
        _content.value = newContent
        scheduleAutosave()
    }

    private fun scheduleAutosave() {

        autosaveJob?.cancel()

        autosaveJob = viewModelScope.launch {
            delay(500)
            saveNote()
        }
    }

    fun saveNow() {

        autosaveJob?.cancel()

        viewModelScope.launch {
            saveNote()
        }
    }

    fun deleteNote(
        onDeleted: () -> Unit
    ) {

        autosaveJob?.cancel()

        viewModelScope.launch {

            repository.deleteNote(noteId)

            onDeleted()
        }
    }

    private suspend fun saveNote() {

        val currentTitle = _title.value
        val currentContent = _content.value

        if (currentTitle.isBlank() && currentContent.isBlank()) {
            return
        }

        val note = Note(
            id = noteId,
            title = currentTitle,
            content = currentContent,
            createdAt = createdAt,
            updatedAt = System.currentTimeMillis()
        )

        repository.saveNote(note)
    }

    override fun onCleared() {

        autosaveJob?.cancel()

        super.onCleared()
    }
}