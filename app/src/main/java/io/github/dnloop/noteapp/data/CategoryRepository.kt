package io.github.dnloop.noteapp.data

import androidx.lifecycle.LiveData

class CategoryRepository(private val categoryDao: CategoryDao) {

    val lastCategory: Category? = categoryDao.getLatest()
    val allCategories: LiveData<List<Category>>  = categoryDao.getAllCategories()

    fun insert(category: Category) : Long {
        return categoryDao.insertWithTimestamp(category)
    }

    fun update(category: Category) {
        categoryDao.updateWithTimestamp(category)
    }

    fun findById(id: Long) : LiveData<Category> {
        return categoryDao.get(id)
    }

    fun hardDelete(category: Category) {
        categoryDao.delete(category)
    }

    fun countNotes(id: Long) : Long {
        return categoryDao.getNotesCount(id)
    }

    fun clearTable() {
        categoryDao.clear()
    }

    // TODO add soft delete and Cascade methods
    // TODO coroutine support
}