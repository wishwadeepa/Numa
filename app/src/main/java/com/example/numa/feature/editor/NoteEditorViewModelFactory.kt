package com.example.numa.feature.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.numa.data.NoteRepository

class NoteEditorViewModelFactory(
    private val repository: NoteRepository,
    private val noteId: String? = null
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(NoteEditorViewModel::class.java)) {

            return NoteEditorViewModel(
                repository = repository,
                existingNoteId = noteId
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}