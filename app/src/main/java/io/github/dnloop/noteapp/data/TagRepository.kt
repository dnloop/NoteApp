package io.github.dnloop.noteapp.data

import androidx.lifecycle.LiveData

class TagRepository(private val tagDao: TagDao) {

    val latestTag: Tag? = tagDao.getLatest()
    val allTags: LiveData<List<Tag>> = tagDao.getAllTags()

    fun insert(tag: Tag): Long {
        return tagDao.insertWithTimestamp(tag)
    }

    fun update(tag: Tag) {
        tagDao.updateWithTimestamp(tag)
    }

    fun findById(id: Long): LiveData<Tag> {
        return tagDao.get(id)
    }

    fun hardDelete(tag: Tag) {
        tagDao.delete(tag)
    }

    fun clearTable() {
        tagDao.clear()
    }

    // TODO add soft delete and Cascade methods
    // TODO coroutine support
}