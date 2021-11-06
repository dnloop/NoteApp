package io.github.dnloop.noteapp.data

import androidx.room.*

@Dao
interface NoteTagDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(join: NoteTagCrossRef) : Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(join: List<NoteTagCrossRef>) : List<Long>

    @Delete
    fun delete(join: NoteTagCrossRef)

    @Transaction
    fun deleteAll(join: List<NoteTagCrossRef>){
        for (item in join)
            delete(item)
    }
}