package io.github.dnloop.noteapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.dnloop.noteapp.data.*
import kotlinx.coroutines.*
import timber.log.Timber

class EditNoteViewModel(noteKeyId: Long, private val dataSourceNote: NoteDao) : ViewModel() {
    private lateinit var dataSourceCategory: CategoryDao

    private val viewModelJob = Job()

    private var _note = MediatorLiveData<Note>()

    fun getNote() = _note

    init {
        _note.addSource(dataSourceNote.get(noteKeyId), _note::setValue)
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
            NoteRepository(dataSourceNote)
        }
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

    fun getNoteWithCategory(key: Long): LiveData<NoteWithCategory> {
        return runBlocking {
            withContext(Dispatchers.IO) {
                getRepository().noteWithCategory(key)
            }
        }
    }

    fun onUpdateWithCategory(pair: NoteWithCategory) {
        CoroutineScope(Dispatchers.Main + Job()).launch {
            withContext(Dispatchers.IO) {
                getRepository().updateWithCategory(pair)
            }
        }
    }
}