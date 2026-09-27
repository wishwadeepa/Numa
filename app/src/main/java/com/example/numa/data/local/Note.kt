package com.example.numa.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey
    val id: String,

    val title: String = "",

    val content: String = "",

    val createdAt: Long,

    val updatedAt: Long
)