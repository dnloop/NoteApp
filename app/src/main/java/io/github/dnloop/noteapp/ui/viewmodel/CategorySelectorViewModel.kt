package io.github.dnloop.noteapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.dnloop.noteapp.data.Category
import io.github.dnloop.noteapp.data.CategoryDao
import io.github.dnloop.noteapp.data.CategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class CategorySelectorViewModel(
    val categoryDataSource: CategoryDao,
    application: Application
) : AndroidViewModel(application) {
    private val _selectedCategory = MutableLiveData<Category>()

    private val _badgeCounter = MutableLiveData<Long>()

    val selectedCategory
        get() = _selectedCategory

    val badgeCounter
        get() = _badgeCounter

    fun onOpenDialogEditorNavigated() {
        _selectedCategory.value = null
    }

    fun onCategorySelected(item: Category) {
        _selectedCategory.value = item
    }

    fun onBadgeCounterChanged(item: Long) {
        _badgeCounter.value = item
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

    fun onNoteCount(category: Category): LiveData<Long> {
        return runBlocking {
            withContext(Dispatchers.IO) {
                getRepository().countNotes(category.id)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Job().cancel()
    }
}