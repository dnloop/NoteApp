package io.github.dnloop.noteapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Tag(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tag_id", index = true)
    var id : Int,
    var name : String,
    // CRUD values
    var deleted : Boolean,
    var createdAt : Long,
    var modifiedAt : Long,
    var deletedAt : Long?
) {
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    constructor() : this(
        0,
        "",
        false,
        0,
        0,
        null
    )
}

