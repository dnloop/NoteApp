package io.github.dnloop.noteapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.dnloop.noteapp.data.Note
import io.github.dnloop.noteapp.data.NoteDao
import io.github.dnloop.noteapp.data.NoteRepository
import io.github.dnloop.noteapp.data.NoteWithCategory
import kotlinx.coroutines.*

class TrashViewModel( private val dataSource: NoteDao,  application: Application) : AndroidViewModel(application) {

    private val _openConfirmDialog = MutableLiveData<Note>()

    val openConfirmDialog
        get() = _openConfirmDialog

    fun onNoteSelected(item: Note) {
        _openConfirmDialog.value = item
    }

    private suspend fun getRepository() : NoteRepository {
        return withContext(Dispatchers.IO) {
            NoteRepository(dataSource)
        }
    }

    fun loadDeletedNotes() : LiveData<List<NoteWithCategory>> {
        return runBlocking {
            withContext(Dispatchers.IO) {
                getRepository().allDeletedNotes
            }
        }
    }

    fun onRestore(item: Note) {
        item.deleted = false
        CoroutineScope(Dispatchers.Main + Job()).launch {
            withContext(Dispatchers.IO) {
                getRepository().update(item)
            }
        }
    }

    fun onDelete(item: Note) {
        CoroutineScope(Dispatchers.Main + Job()).launch {
            withContext(Dispatchers.IO) {
                getRepository().hardDelete(item)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Job().cancel()
    }
}