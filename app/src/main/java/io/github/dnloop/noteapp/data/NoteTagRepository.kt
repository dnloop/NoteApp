package io.github.dnloop.noteapp.data

class NoteTagRepository(private val noteTagDao: NoteTagDao) {

    fun attach(noteId: Long, tagId: Long) {
        val join = NoteTagCrossRef(noteId, tagId)
        noteTagDao.insert(join)
    }

    fun attachAll(join: List<NoteTagCrossRef>) {
        noteTagDao.insertAll(join)
    }

    fun detach(noteId: Long, tagId: Long) {
        val join = NoteTagCrossRef(noteId, tagId)
        noteTagDao.delete(join)
    }

    fun detachAll(join: List<NoteTagCrossRef>) {
        noteTagDao.deleteAll(join)
    }
}
