package io.github.dnloop.noteapp.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {

    /**
    * Adds a new Note
    *
    * @param note new note value to write
    */
    @Insert
    fun insert(note: Note): Int

    /**
     * Inserts a note matching the categoryId.
     *
     * @param note the note to be added
     * @param category the category for the note
     */
    @Transaction
    fun insertWithCategory(note: Note, category: Category){
        note.categoryId = category.id
        insert(note)
    }

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
     * Selects and returns the latest Note.
     */
    @Query("SELECT * FROM Note ORDER BY id DESC LIMIT 1")
    fun getLatest(): Note?


    /**
     * Selects the Notes with a Category.
     */
    @Transaction
    @Query("SELECT * FROM Note")
    fun getNotesWithCategory(): LiveData<List<NoteWithCategory>>

}