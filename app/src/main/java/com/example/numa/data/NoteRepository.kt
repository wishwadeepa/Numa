package com.example.numa.data

import com.example.numa.data.local.Note
import com.example.numa.data.local.NoteDao
import kotlinx.coroutines.flow.Flow

class NoteRepository(
    private val noteDao: NoteDao
) {

    suspend fun saveNote(note: Note) {
        noteDao.upsert(note)
    }

    suspend fun getNote(id: String): Note? {
        return noteDao.getNote(id)
    }

    fun observeAllNotes(): Flow<List<Note>> {
        return noteDao.observeAllNotes()
    }

    suspend fun deleteNote(id: String) {
        noteDao.deleteNote(id)
    }
}