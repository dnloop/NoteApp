package io.github.dnloop.noteapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.dnloop.noteapp.data.Category
import io.github.dnloop.noteapp.data.CategoryDao
import io.github.dnloop.noteapp.data.CategoryRepository
import kotlinx.coroutines.*

class CategoryViewModel(val categoryDataSource: CategoryDao, application: Application) :
    AndroidViewModel(application) {

    private val _openDialogEditor = MutableLiveData<Category>()

    val openDialogEditor
        get() = _openDialogEditor

    fun onCategorySelected(item: Category) {
        _openDialogEditor.value = item
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

    fun onInsert(category: Category) {
        CoroutineScope(Dispatchers.Main + Job()).launch {
            withContext(Dispatchers.IO) {
                getRepository().insert(category)
            }
        }
    }

    fun onUpdate(category: Category) {
        CoroutineScope(Dispatchers.Main + Job()).launch {
            withContext(Dispatchers.IO) {
                getRepository().update(category)
            }
        }
    }

    fun onDelete(category: Category) {
        CoroutineScope(Dispatchers.Main + Job()).launch {
            withContext(Dispatchers.IO) {
                getRepository().hardDelete(category)
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

    fun findById(key: Long): LiveData<Category> {
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