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
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one using the category id
     * to link it to the note.
     *
     * @param pair a note with a category object.
     */
    fun updateWithCategory(pair: NoteWithCategory) {
        update(pair.note.apply {
            categoryId = pair.category.id
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
    @Query("SELECT * FROM Note WHERE archived = 0 AND deleted = 0  ORDER BY note_id DESC")
    abstract fun getAllNotes(): LiveData<List<Note>>

    /**
     * Selects and returns all rows in the table,
     * sorted by noteId in descending order.
     */
    @Query("SELECT * FROM Note WHERE categoryId = :key AND archived = 0 AND deleted = 0  ORDER BY note_id DESC")
    abstract fun getAllNotesByCategory(key: Long): LiveData<List<Note>>

    /**
     * Selects and returns all rows in the table,
     * sorted by noteId in descending order with categories.
     */
    @Transaction
    @Query("SELECT * FROM Note INNER JOIN Category ON Note.categoryId WHERE Note.archived = 0 AND Note.deleted = 0 AND Category.deleted = 0 GROUP BY note_id ORDER BY note_id DESC")
    abstract fun getAllNotesWithCategories(): LiveData<List<NoteWithCategory>>

    /**
     * Selects and returns all rows in the table,
     * sorted by noteId in descending order.
     */
    @Query("SELECT * FROM Note WHERE archived = 1 AND deleted = 0 ORDER BY note_id DESC")
    abstract fun getAllArchivedNotes(): LiveData<List<Note>>

    /**
     * Selects and returns all rows in the table,
     * sorted by noteId in descending order.
     */
    @Query("SELECT * FROM Note WHERE deleted = 1 ORDER BY note_id DESC")
    abstract fun getAllDeletedNotes(): LiveData<List<Note>>

    /**
     * Selects and returns the latest Note.
     */
    @Query("SELECT * FROM Note WHERE archived = 0 AND deleted = 0 ORDER BY note_id DESC LIMIT 1")
    abstract fun getLatest(): Note?

    /**
     * Selects all Notes with Tags.
     */
    @Transaction
    @Query("SELECT * FROM Note WHERE archived = 0 AND deleted = 0")
    abstract fun getNotesWithTags(): LiveData<List<NoteWithTags>>

    /**
     * Selects all Tags with Notes.
     */
    @Transaction
    @Query("SELECT * FROM Tag")
    abstract fun getTagsWithNotes(): LiveData<List<TagWithNotes>>

    /**
     * Selects all Tags with Note id.
     */
    @Transaction
    @Query("SELECT * FROM Note WHERE note_id = :id AND archived = 0 AND deleted = 0")
    abstract fun getNoteWithTags(id: Long): LiveData<NoteWithTags>

    /**
     * Selects all Notes with Tag id.
     */
    @Transaction
    @Query("SELECT * FROM Tag WHERE tag_id = :id AND deleted = 0")
    abstract fun getTagWithNotes(id: Long): LiveData<TagWithNotes>

}