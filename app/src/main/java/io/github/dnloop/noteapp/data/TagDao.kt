package io.github.dnloop.noteapp.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TagDao {
    /**
     * Adds a new Tag
     *
     * @param tag new tag value to write
     */
    @Insert
    fun insert(tag: Tag): Int

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param tag new value to write
     */
    @Update
    fun update(tag: Tag)

    /**
     * Selects and returns the row that matches the supplied noteId.
     *
     * @param key id
     */
    @Query("SELECT * from tag WHERE id = :key")
    fun get(key: Long): LiveData<Tag>

    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM Tag")
    fun clear()

    /**
     * Selects and returns all rows in the table,
     *
     * sorted by id in descending order.
     */
    @Query("SELECT * FROM Tag ORDER BY id DESC")
    fun getAllTags(): LiveData<List<Tag>>
}