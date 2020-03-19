package io.github.dnloop.noteapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import io.github.dnloop.noteapp.data.Category
import io.github.dnloop.noteapp.data.CategoryDao
import io.github.dnloop.noteapp.data.CategoryRepository
import kotlinx.coroutines.*

class CategoryViewModel(val categoryDataSource: CategoryDao, application: Application) : AndroidViewModel(application) {

    private val _openDialogEditor = MutableLiveData<Category>()

    val openDialogEditor
        get() = _openDialogEditor

    fun onOpenDialogEditorNavigated() {
        _openDialogEditor.value = null
    }

    fun onCategorySelected(item: Category) {
        _openDialogEditor.value = item
    }

    private suspend fun getRepository(): CategoryRepository {
        return withContext(Dispatchers.IO) {
            CategoryRepository(categoryDataSource)
        }
    }

    override fun onCleared() {
        super.onCleared()
        Job().cancel()
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
}