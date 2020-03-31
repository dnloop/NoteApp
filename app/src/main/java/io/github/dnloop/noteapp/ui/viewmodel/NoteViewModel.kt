package io.github.dnloop.noteapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.dnloop.noteapp.data.*
import kotlinx.coroutines.*

class NoteViewModel(val dataSource: NoteDao, application: Application) : AndroidViewModel(application) {

    private val _navigateToEditor = MutableLiveData<Long>()

    val navigateToEditor
        get() = _navigateToEditor

    fun onNoteEditorNavigated() {
        _navigateToEditor.value = null
    }

    fun onNoteSelected(item: Long) {
        _navigateToEditor.value = item
    }

    private suspend fun getRepository(): NoteRepository {
        return withContext(Dispatchers.IO) {
           NoteRepository(dataSource)
        }
    }

    override fun onCleared() {
        super.onCleared()
        Job().cancel()
    }


    fun loadNotes(): LiveData<List<Note>> {
        return runBlocking {
            withContext(Dispatchers.IO) {
                getRepository().allNotes
            }
        }
    }

    fun loadArchivedNotes(): LiveData<List<Note>> {
        return runBlocking {
            withContext(Dispatchers.IO) {
                getRepository().allArchivedNotes
            }
        }
    }
}