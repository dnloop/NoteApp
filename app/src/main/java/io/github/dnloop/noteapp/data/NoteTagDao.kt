package io.github.dnloop.noteapp.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteTagDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(join: NoteTagCrossRef)

    @Delete
    fun delete(join: NoteTagCrossRef)

    /**
     * Selects all Notes with Tags.
     */
    @Transaction
    @Query("SELECT * FROM Note")
    fun getNotesWithTags(): LiveData<List<NotesWithTags>>

    /**
     * Selects all Tags with Notes.
     */
    @Transaction
    @Query("SELECT * FROM Tag")
    fun getTagsWithNotes(): LiveData<List<TagsWithNotes>>
}