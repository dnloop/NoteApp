package io.github.dnloop.noteapp.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
abstract class NoteDao {

    /**
    * Adds a new Note
    *
    * @param note new note value to write
    */
    @Insert
    abstract fun insert(note: Note) : Long

    fun insertWithTimestamp(note: Note) : Long {
        return insert(note.apply{
            createdAt = System.currentTimeMillis()
            modifiedAt = System.currentTimeMillis()
        })
    }

    /**
     * Inserts a note matching the categoryId.
     *
     * @param note the note to be added
     * @param category the category for the note
     */
    @Transaction
    @Insert
    fun insertWithCategory(note: Note, category: Category) : Long {
        return insert(note.apply { categoryId = category.id })
    }

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param note new value to write
     */
    @Update
    abstract fun update(note: Note)

    fun updateWithTimestamp(note: Note) {
        update(note.apply {
            modifiedAt = System.currentTimeMillis()
        })
    }

    /**
     * Selects and returns the row that matches the supplied noteId.
     *
     * @param key noteId
     */
    @Query("SELECT * from note WHERE note_id = :key")
    abstract fun get(key: Long): LiveData<Note>

    /**
     * Delete a single value from the table.
     */
    @Delete
    abstract fun delete(note: Note)

    /**
     * Deletes all values from the table.
     */
    @Query("DELETE FROM Note")
    abstract fun clear()

    /**
     * Selects and returns all rows in the table,
     * sorted by noteId in descending order.
     */
    @Query("SELECT * FROM Note ORDER BY note_id DESC")
    abstract fun getAllNotes(): LiveData<List<Note>>

    /**
     * Selects and returns the latest Note.
     */
    @Query("SELECT * FROM Note ORDER BY note_id DESC LIMIT 1")
    abstract fun getLatest(): Note?


    /**
     * Selects the Notes with a Category.
     */
    @Transaction
    @Query("SELECT * FROM Note")
    abstract fun getNotesWithCategory(): LiveData<List<NotesWithCategory>>

    /**
     * Selects all Notes with Tags.
     */
    @Transaction
    @Query("SELECT * FROM Note")
    abstract fun getNotesWithTags(): LiveData<List<NotesWithTags>>

    /**
     * Selects all Tags with Notes.
     */
    @Transaction
    @Query("SELECT * FROM Tag")
    abstract fun getTagsWithNotes(): LiveData<List<TagsWithNotes>>

}