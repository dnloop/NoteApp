package io.github.dnloop.noteapp.ui.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.dnloop.noteapp.data.Note
import io.github.dnloop.noteapp.data.NoteDao
import io.github.dnloop.noteapp.data.NoteRepository
import io.github.dnloop.noteapp.data.NoteWithCategory
import kotlinx.coroutines.*

class EditNoteViewModel(noteKeyId: Long, private val dataSourceNote: NoteDao) : ViewModel() {
    private val viewModelJob = Job()

    private var _note = MediatorLiveData<Note>()

    fun getNote() = _note

    init {
        _note.addSource(dataSourceNote.get(noteKeyId), _note::setValue)
    }

    private val _navigateToHome = MutableLiveData<Boolean?>()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    /* Coroutines */

    private suspend fun getRepository(): NoteRepository {
        return withContext(Dispatchers.IO) {
            NoteRepository(dataSourceNote)
        }
    }

    fun onInsert(note: Note): Long {
        return runBlocking {
            withContext(Dispatchers.IO) {
                getRepository().insert(note)
            }
        }
    }

    fun onGetLatest() : Note? {
        return runBlocking {
            withContext(Dispatchers.IO) {
                getRepository().lastNote
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

    fun onUpdateWithCategory(pair: NoteWithCategory) {
        CoroutineScope(Dispatchers.Main + Job()).launch {
            withContext(Dispatchers.IO) {
                getRepository().updateWithCategory(pair)
            }
        }
    }
}