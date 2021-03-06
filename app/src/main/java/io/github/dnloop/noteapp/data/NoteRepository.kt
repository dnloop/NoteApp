package io.github.dnloop.noteapp.data

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery

class NoteRepository(private val noteDao: NoteDao) {
    private val _identityHashQuery : SimpleSQLiteQuery = SimpleSQLiteQuery("SELECT identity_hash FROM room_master_table")

    val lastNote: Note? = noteDao.getLatest()

    val allNotes: LiveData<List<NoteWithCategory>> = noteDao.getAllNotesWithCategories()

    val allArchivedNotes: LiveData<List<NoteWithCategory>> = noteDao.getAllArchivedNotes()

    val allDeletedNotes: LiveData<List<NoteWithCategory>> = noteDao.getAllDeletedNotes()

    val noteWithTags: LiveData<List<NoteWithTags>> = noteDao.getNotesWithTags()

    val tagWithNotes: LiveData<List<TagWithNotes>> = noteDao.getTagsWithNotes()

    fun getNotesWithCategory(key: Long): LiveData<List<NoteWithCategory>> {
        return noteDao.getAllNotesByCategory(key)
    }

    fun insert(note: Note): Long {
        return noteDao.insertWithTimestamp(note)
    }

    fun insertWithCategory(note: Note, category: Category): Long {
        return noteDao.insertWithCategory(note, category)
    }

    fun updateWithCategory(pair: NoteWithCategory) {
        noteDao.updateWithCategory(pair)
    }

    fun update(note: Note) {
        noteDao.updateWithTimestamp(note)
    }

    fun findById(id: Long): LiveData<Note> {
        return noteDao.get(id)
    }

    fun softDelete(note: Note) {
        noteDao.update(note)
    }

    fun hardDelete(note: Note) {
        noteDao.delete(note)
    }

    fun identityHash(): String {
        return noteDao.getIdentityHash(_identityHashQuery)
    }

    fun clearTable() {
        noteDao.clear()
    }
}