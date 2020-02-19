package io.github.dnloop.noteapp.data

import androidx.room.Entity

@Entity(primaryKeys = ["noteId", "tagId"])
data class NoteTagCrossRef(
    val noteId : Int,
    val tagId : Int
)