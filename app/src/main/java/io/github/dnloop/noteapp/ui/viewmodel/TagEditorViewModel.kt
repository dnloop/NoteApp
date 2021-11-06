package io.github.dnloop.noteapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import io.github.dnloop.noteapp.data.Tag
import io.github.dnloop.noteapp.data.TagDao
import io.github.dnloop.noteapp.data.TagRepository
import kotlinx.coroutines.*

class TagEditorViewModel(val tagDataSource: TagDao, application: Application) : AndroidViewModel(application) {
    private val _openDialogEditor = MutableLiveData<Tag?>()

    val openDialogEditor
        get() = _openDialogEditor

    fun onOpenDialogEditorNavigated() {
        _openDialogEditor.value = null
    }

    fun onTagSelected(item: Tag) {
        _openDialogEditor.value = item
    }

    private suspend fun getRepository(): TagRepository {
        return withContext(Dispatchers.IO) {
            TagRepository(tagDataSource)
        }
    }

    override fun onCleared() {
        super.onCleared()
        Job().cancel()
    }

    fun onInsert(tag: Tag) {
        CoroutineScope(Dispatchers.Main + Job()).launch {
            withContext(Dispatchers.IO) {
                getRepository().insert(tag)
            }
        }
    }

    fun onUpdate(tag: Tag) {
        CoroutineScope(Dispatchers.Main + Job()).launch {
            withContext(Dispatchers.IO) {
                getRepository().update(tag)
            }
        }
    }

    fun onDelete(tag: Tag) {
        CoroutineScope(Dispatchers.Main + Job()).launch {
            withContext(Dispatchers.IO) {
                getRepository().hardDelete(tag)
            }
        }
    }
}
