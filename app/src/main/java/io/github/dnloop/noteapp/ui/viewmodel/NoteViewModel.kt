package io.github.dnloop.noteapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.dnloop.noteapp.data.Note
import io.github.dnloop.noteapp.data.NoteDatabase
import io.github.dnloop.noteapp.data.NoteRepository
import kotlinx.coroutines.*

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NoteRepository
    private lateinit var _allNotes: LiveData<List<Note>>
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        val noteDao = NoteDatabase.getInstance(application).noteDao
        repository = NoteRepository(noteDao)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun getNotes(): LiveData<List<Note>> {
        return allNotes
    }

    private val allNotes: MutableLiveData<List<Note>> by lazy {
        MutableLiveData<List<Note>>().also {
            loadNotes()
        }
    }

    private fun loadNotes() {
        uiScope.launch {
            _allNotes = getFromDatabase()
        }
    }

    private suspend fun getFromDatabase(): LiveData<List<Note>> {
        return withContext(Dispatchers.IO) {
            repository.allNotes
        }
    }

    fun onInsert(note: Note) {
        uiScope.launch {
            insert(note)
        }
    }

    private suspend fun insert(note: Note) {
        withContext(Dispatchers.IO) {
            repository.insert(note)
        }
    }
}