package io.github.dnloop.noteapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Category (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "category_id", index = true)
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

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun toString(): String {
        return super.toString()
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