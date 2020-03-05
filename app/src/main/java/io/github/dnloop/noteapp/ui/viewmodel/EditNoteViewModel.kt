package io.github.dnloop.noteapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.dnloop.noteapp.data.Note
import io.github.dnloop.noteapp.data.NoteDao
import io.github.dnloop.noteapp.data.NoteRepository
import kotlinx.coroutines.*
import timber.log.Timber

class EditNoteViewModel(noteKeyId: Long, private val dataSource: NoteDao): ViewModel() {

    private val viewModelJob = Job()

    private val note = MediatorLiveData<Note>()

    fun getNote() = note

    init {
        note.addSource(dataSource.get(noteKeyId), note::setValue)
    }

    private val _navigateToHome = MutableLiveData<Boolean?>()

    val navigateToHome: LiveData<Boolean?>
        get() = _navigateToHome

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun doneNavigating() {
        _navigateToHome.value = null
    }

    fun onClose() {
        _navigateToHome.value = true
    }

    /* Coroutines */

    private suspend fun getRepository(): NoteRepository {
        return withContext(Dispatchers.IO) {
            NoteRepository(dataSource)
        }
    }

    fun dummyInsert() {
        Timber.i("Inserted")
    }

    fun dummyUpdate(id: Long) {
        Timber.i("Updated: $id")
    }

    fun onInsert(note: Note) {
        CoroutineScope(Dispatchers.Main + Job()).launch {
            withContext(Dispatchers.IO) {
                getRepository().insert(note)
            }
        }
    }

    fun onUpdate(note: Note) {
        CoroutineScope(Dispatchers.Main + Job()).launch {
            withContext(Dispatchers.IO) {
                getRepository().update(note)
            }
        }
    }
}