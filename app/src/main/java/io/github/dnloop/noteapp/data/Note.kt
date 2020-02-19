package io.github.dnloop.noteapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import java.sql.Timestamp

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

)

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