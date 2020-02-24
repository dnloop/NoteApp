package io.github.dnloop.noteapp.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteTagDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(join: NoteTagCrossRef) : Long

    @Delete
    fun delete(join: NoteTagCrossRef)
}