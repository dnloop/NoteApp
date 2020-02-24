package io.github.dnloop.noteapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["note_id", "tag_id"])
data class NoteTagCrossRef(
    @ColumnInfo(name = "note_id", index = true)
    var noteId : Int,
    @ColumnInfo(name = "tag_id", index = true)
    var tagId : Int
) {
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    constructor() : this(noteId = 0, tagId = 0)
}