package io.github.dnloop.noteapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery

@Dao
abstract class NoteDao {
    /**
     * Adds a new Note
     *
     * @param note new note value to write
     */
    @Insert
    abstract fun insert(note: Note): Long

    fun insertWithTimestamp(note: Note): Long {
        return insert(note.apply {
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
    fun insertWithCategory(note: Note, category: Category): Long {
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
            pair.category?.let {
                categoryId = it.id
            }
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
    @Transaction
    @Query("SELECT * FROM Note INNER JOIN Category ON categoryId = category_id WHERE Note.categoryId = :key AND Note.archived = 0 AND Note.deleted = 0 GROUP BY note_id ORDER BY note_id DESC")
    abstract fun getAllNotesByCategory(key: Long): LiveData<List<NoteWithCategory>>

    /**
     * Selects and returns all rows in the table,
     * sorted by noteId in descending order with categories.
     */
    @Transaction
    @Query("SELECT * FROM Note Left JOIN Category ON categoryId = category_id WHERE Note.archived = 0 AND Note.deleted = 0 GROUP BY note_id ORDER BY title DESC")
    abstract fun getAllNotesWithCategories(): LiveData<List<NoteWithCategory>>

    /**
     * Selects and returns all rows in the table,
     * sorted by noteId in descending order.
     */
    @Transaction
    @Query("SELECT * FROM Note LEFT JOIN Category ON categoryId = category_id WHERE Note.archived = 1 AND Note.deleted = 0 GROUP BY note_id ORDER BY title DESC")
    abstract fun getAllArchivedNotes(): LiveData<List<NoteWithCategory>>

    /**
     * Selects and returns all rows in the table,
     * sorted by noteId in descending order.
     */
    @Transaction
    @Query("SELECT * FROM Note INNER JOIN Category WHERE Note.deleted = 1 AND Category.deleted = 0 GROUP BY note_id ORDER BY title DESC")
    abstract fun getAllDeletedNotes(): LiveData<List<NoteWithCategory>>

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


    /**
     * Retrieves the identity hash used by room at runtime, this is necessary because we make sure
     * that the schema is correct when we import the backed database.
     * @return the hash identity value.
     */
    @Transaction
    @RawQuery
    abstract fun getIdentityHash(_query: SimpleSQLiteQuery): String

}