package io.github.dnloop.noteapp.data

import androidx.lifecycle.LiveData
import androidx.room.*


@Entity
data class Note (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "note_id", index = true)
    var id : Long,
    var title : String,
    var content : String,
    var categoryId : Long?,
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

data class NoteWithCategory(
    @Embedded var note : Note,
    @Relation(parentColumn = "note_id", entityColumn = "category_id")
    var category : Category
) {
    constructor() : this(note = Note(), category = Category())
}

data class NotesWithCategory(
    @Embedded var category : Category,
    @Relation(parentColumn = "category_id", entityColumn = "categoryId")
    var notes: Note
)

data class NoteWithTags(
    @Embedded val note : Note,
    @Relation(
        parentColumn = "note_id",
        entityColumn = "tag_id",
        associateBy = Junction(NoteTagCrossRef::class)
    )
    val tags : List<Tag>
)

data class TagWithNotes(
    @Embedded val tag : Tag,
    @Relation(
        parentColumn = "tag_id",
        entityColumn = "note_id",
        associateBy = Junction(NoteTagCrossRef::class)
    )
    val notes : List<Note>
)