package io.github.dnloop.noteapp.data

import androidx.lifecycle.LiveData

class NoteRepository(private val noteDao: NoteDao) {
    val lastNote: Note? = noteDao.getLatest()

    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()

    val notesWithCategory: LiveData<List<NotesWithCategory>> = noteDao.getNotesWithCategory()

    val noteWithTags: LiveData<List<NoteWithTags>> = noteDao.getNoteWithTags()

    val tagWithNotes: LiveData<List<TagWithNotes>> = noteDao.getTagWithNotes()

    fun insert(note: Note) : Long {
        return noteDao.insertWithTimestamp(note)
    }

    fun insertWithCategory(note: Note, category: Category): Long {
        return noteDao.insertWithCategory(note, category)
    }

    fun update(note: Note) {
        noteDao.updateWithTimestamp(note)
    }

    fun findById(id: Long): LiveData<Note> {
        return noteDao.get(id)
    }

    fun hardDelete(note: Note) {
        noteDao.delete(note)
    }

    fun clearTable() {
        noteDao.clear()
    }

    // TODO add soft delete and check Cascade deletion
    // TODO coroutine support
}