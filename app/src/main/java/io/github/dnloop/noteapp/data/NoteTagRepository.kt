package io.github.dnloop.noteapp.data

class NoteTagRepository(private val noteTagDao: NoteTagDao) {

    fun attach(note: Note, tag: Tag) {
        val join = NoteTagCrossRef(note.id, tag.id)
        noteTagDao.insert(join)
    }

    fun detach(note: Note, tag: Tag) {
        val join = NoteTagCrossRef(note.id, tag.id)
        noteTagDao.delete(join)
    }
}