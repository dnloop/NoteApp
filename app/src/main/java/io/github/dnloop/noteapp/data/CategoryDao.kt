package io.github.dnloop.noteapp.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
abstract class CategoryDao {
    /**
     * Adds a new Category
     *
     * @param category new category value to write
     */
    @Insert
    abstract fun insert(category: Category) : Long

    fun insertWithTimestamp(category: Category) : Long {
        return insert(category.apply {
            createdAt = System.currentTimeMillis()
            modifiedAt = System.currentTimeMillis()
        })
    }

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param category new value to write
     */
    @Update
    abstract fun update(category: Category)

    fun updateWithTimestamp(category: Category) {
        update(category.apply {
            modifiedAt = System.currentTimeMillis()
        })
    }

    /**
     * Selects and returns the row that matches the supplied noteId.
     *
     * @param key id
     */
    @Query("SELECT * from Category WHERE category_id = :key")
    abstract fun get(key: Long): LiveData<Category>

    /**
     * Selects and returns the latest Category.
     */
    @Query("SELECT * FROM Category ORDER BY category_id DESC LIMIT 1")
    abstract fun getLatest(): Category?

    /**
     * Delete a single value from the table.
     */
    @Delete
    abstract fun delete(category: Category)

    /**
     * Deletes all values from the table.
     */
    @Query("DELETE FROM Category")
    abstract fun clear()

    /**
     * Selects and returns all rows in the table,
     *
     * sorted by noteId in descending order.
     */
    @Query("SELECT * FROM Category ORDER BY category_id DESC")
    abstract fun getAllCategories(): LiveData<List<Category>>

    /**
     * Counts all rows in the table with the given category id.
     *
     * @param key category id
     */
    @Transaction
    @Query("SELECT COUNT(note_id) FROM Note WHERE categoryId = :key ")
    abstract fun getNotesCount(key: Long): LiveData<Long>
}