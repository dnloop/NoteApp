package io.github.dnloop.noteapp.data

import androidx.lifecycle.LiveData
import androidx.room.*


@Entity
data class Note (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "note_id", index = true)
    var id : Int,
    var title : String,
    var content : String,
    var categoryId : Int?,
    var archived : Boolean,
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
        "",
        null,
        false,
        false,
        0,
        0,
        null)
}

data class NotesWithCategory(
    @Embedded val note : Note,
    @Relation(parentColumn = "note_id", entityColumn = "category_id")
    val category : Category
)

data class NotesWithTags(
    @Embedded val note : Note,
    @Relation(
        parentColumn = "note_id",
        entityColumn = "tag_id",
        associateBy = Junction(NoteTagCrossRef::class)
    )
    val tags : List<Tag>
)

data class TagsWithNotes(
    @Embedded val tag : Tag,
    @Relation(
        parentColumn = "tag_id",
        entityColumn = "note_id",
        associateBy = Junction(NoteTagCrossRef::class)
    )
    val notes : List<Note>
)