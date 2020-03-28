package io.github.dnloop.noteapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.dnloop.noteapp.data.Category
import io.github.dnloop.noteapp.data.CategoryDao
import io.github.dnloop.noteapp.data.CategoryRepository
import kotlinx.coroutines.*

class CategorySelectorViewModel(
    val categoryDataSource: CategoryDao,
    application: Application
) : AndroidViewModel(application) {
    private val _selectedCategory = MutableLiveData<Category>()

    val selectedCategory
        get() = _selectedCategory

    fun onCategorySelected(item: Category) {
        _selectedCategory.value = item
    }

    private suspend fun getRepository(): CategoryRepository {
        return withContext(Dispatchers.IO) {
            CategoryRepository(categoryDataSource)
        }
    }

    fun loadCategories(): LiveData<List<Category>> {
        return runBlocking {
            withContext(Dispatchers.IO) {
                getRepository().allCategories
            }
        }
    }

    fun onNoteCount(category: Category): Long {
        return runBlocking {
            withContext(Dispatchers.IO) {
                getRepository().countNotes(category.id)
            }
        }
    }

    fun findById(key: Long) : LiveData<Category> {
        return runBlocking {
            withContext(Dispatchers.IO) {
                getRepository().findById(key)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Job().cancel()
    }
}