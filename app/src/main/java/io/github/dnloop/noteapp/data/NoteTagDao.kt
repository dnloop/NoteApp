package io.github.dnloop.noteapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface NoteTagDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(join: NoteTagCrossRef) : Long

    @Delete
    fun delete(join: NoteTagCrossRef)
}