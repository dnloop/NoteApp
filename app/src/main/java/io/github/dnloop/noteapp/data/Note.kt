package io.github.dnloop.noteapp.data

import androidx.lifecycle.LiveData
import androidx.room.*


@Entity
data class Note (
    @PrimaryKey(autoGenerate = true) var id : Int,
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

data class NoteWithCategory(
    @Embedded val note : Note,
    @Relation(parentColumn = "id", entityColumn = "id")
    val category : Category
)

data class NotesWithTags(
    @Embedded val note : Note,
    @Relation(
        parentColumn = "noteId",
        entityColumn = "tagId",
        associateBy = Junction(NoteTagCrossRef::class)
    )
    val tags : LiveData<List<Tag>>
)

data class TagsWithNotes(
    @Embedded val tag : Tag,
    @Relation(
        parentColumn = "tagId",
        entityColumn = "noteId",
        associateBy = Junction(NoteTagCrossRef::class)
    )
    val notes : LiveData<List<Note>>
)