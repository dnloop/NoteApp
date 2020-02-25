package io.github.dnloop.noteapp.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
abstract class TagDao {
    /**
     * Adds a new Tag
     *
     * @param tag new tag value to write
     */
    @Insert
    abstract fun insert(tag: Tag): Long

    fun insertWithTimestamp(tag: Tag) : Long {
        return insert(tag.apply {
            createdAt = System.currentTimeMillis()
            modifiedAt = System.currentTimeMillis()
        })
    }

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param tag new value to write
     */
    @Update
    abstract fun update(tag: Tag)

    fun updateWithTimestamp(tag: Tag) {
        update(tag.apply {
            modifiedAt = System.currentTimeMillis()
        })
    }

    /**
     * Selects and returns the row that matches the supplied noteId.
     *
     * @param key id
     */
    @Query("SELECT * from tag WHERE tag_id = :key")
    abstract fun get(key: Long): LiveData<Tag>

    /**
     * Delete a single value from the table.
     */
    @Delete
    abstract fun delete(tag: Tag)

    /**
     * Deletes all values from the table.
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM Tag")
    abstract fun clear()

    /**
     * Selects and returns all rows in the table,
     * sorted by id in descending order.
     */
    @Query("SELECT * FROM Tag ORDER BY tag_id DESC")
    abstract fun getAllTags(): LiveData<List<Tag>>

    /**
     * Selects and returns the latest Category.
     */
    @Query("SELECT * FROM Tag ORDER BY tag_id DESC LIMIT 1")
    abstract fun getLatest(): Tag?

}