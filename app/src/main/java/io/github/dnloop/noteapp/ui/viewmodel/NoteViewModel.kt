package io.github.dnloop.noteapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.dnloop.noteapp.data.Note
import io.github.dnloop.noteapp.data.NoteDatabase
import io.github.dnloop.noteapp.data.NoteRepository
import io.github.dnloop.noteapp.data.NotesWithCategory
import kotlinx.coroutines.*

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NoteRepository
    private lateinit var _allNotes: LiveData<List<Note>>
    private lateinit var _notesWithCategory: LiveData<List<NotesWithCategory>>
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

    fun onInsert(note: Note) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                repository.insert(note)
            }
        }
    }

    fun onUpdate(note: Note) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                repository.update(note)
            }
        }
    }

    private val allNotes: MutableLiveData<List<Note>> by lazy {
        MutableLiveData<List<Note>>().also { loadNotes() }
    }

    private fun loadNotes() {
        uiScope.launch {
            _allNotes = withContext(Dispatchers.IO) {
                repository.allNotes
            }
        }
    }

    fun getNotesWithCategory(): LiveData<List<NotesWithCategory>> {
        return notesWithCategory
    }

    private val notesWithCategory: MutableLiveData<List<NotesWithCategory>> by lazy {
        MutableLiveData<List<NotesWithCategory>>().also {
            loadNotesWithCategory()
        }
    }

    private fun loadNotesWithCategory() {
        uiScope.launch {
            _notesWithCategory = withContext(Dispatchers.IO) {
                repository.notesWithCategory
            }
        }
    }
}