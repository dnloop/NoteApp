package io.github.dnloop.noteapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.dnloop.noteapp.data.*
import kotlinx.coroutines.*
import javax.sql.DataSource

class NoteViewModel(private val dataSource: NoteDao, application: Application) : AndroidViewModel(application) {

    private lateinit var repository: NoteRepository
    private lateinit var _notesWithCategory: LiveData<List<NotesWithCategory>>
    val allNotes: LiveData<List<Note>> = dataSource.getAllNotes()
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        initializeRepository()
    }

    private fun addDummyData() {
        uiScope.launch { withContext(Dispatchers.IO) {
            repository.clearTable()
            // Add sample words.
            val note = Note()
            note.id = 1
            note.title = "title 1"
            note.content = "content 1"
            note.createdAt = System.currentTimeMillis()
            note.modifiedAt = System.currentTimeMillis()
            repository.insert(note)
            note.id = 2
            note.title = "title 2"
            note.content = "content 2"
            note.createdAt = System.currentTimeMillis()
            note.modifiedAt = System.currentTimeMillis()
            repository.insert(note)
        } }
    }

    private fun initializeRepository() {
        uiScope.launch {
            repository = getRepository()
            addDummyData()
        }
    }

    private suspend fun getRepository(): NoteRepository {
        return withContext(Dispatchers.IO) {
           NoteRepository(dataSource)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
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

    private suspend fun loadNotes(): LiveData<List<Note>> {
        return withContext(Dispatchers.IO) {
            repository.allNotes
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