package io.github.dnloop.noteapp.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDatabaseDao {
    @Insert
    fun insert(note: Note)

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param note new value to write
     */
    @Update
    fun update(note: Note)

    /**
     * Selects and returns the row that matches the supplied noteId.
     *
     * @param key noteId
     */
    @Query("SELECT * from note WHERE id = :key")
    fun get(key: Long): LiveData<Note>

    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM Note")
    fun clear()

    /**
     * Selects and returns all rows in the table,
     *
     * sorted by noteId in descending order.
     */
    @Query("SELECT * FROM Note ORDER BY id DESC")
    fun getAllNotes(): LiveData<List<Note>>

    /**
     * Selects and returns the latest note.
     */
    @Query("SELECT * FROM Note ORDER BY id DESC LIMIT 1")
    fun getLatest(): Note?


    /**
     * Selects the Notes with a category.
     */
    @Transaction
    @Query("SELECT * FROM Note")
    fun getNotesWithCategory(): LiveData<List<NoteWithCategory>>

    /**
     * Selects all Notes with Tags.
     */
    @Transaction
    @Query("SELECT * FROM Note")
    fun getNotesWithTags(): List<NotesWithTags>

    /**
     * Selects all Tags with Notes.
     */
    @Transaction
    @Query("SELECT * FROM Tag")
    fun getTagsWithNotes(): List<TagsWithNotes>

}
