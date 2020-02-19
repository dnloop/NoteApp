package io.github.dnloop.noteapp.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CategoryDao {
    /**
     * Adds a new Category
     *
     * @param category new category value to write
     */
    @Insert
    fun insert(category: Category)

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param category new value to write
     */
    @Update
    fun update(category: Category)

    /**
     * Selects and returns the row that matches the supplied noteId.
     *
     * @param key id
     */
    @Query("SELECT * from category WHERE id = :key")
    fun get(key: Long): LiveData<Category>

    /**
     * Deletes all values from the table.
     */
    @Query("DELETE FROM Category")
    fun clear()

    /**
     * Selects and returns all rows in the table,
     *
     * sorted by noteId in descending order.
     */
    @Query("SELECT * FROM Category ORDER BY id DESC")
    fun getAllNotes(): LiveData<List<Category>>
}