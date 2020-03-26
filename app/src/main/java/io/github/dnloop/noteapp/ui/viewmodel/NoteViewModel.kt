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

    private fun addDummyData() {
        // TODO move this to a helper class and remove from production
        CoroutineScope(Dispatchers.Main + Job()).launch { withContext(Dispatchers.IO) {
            getRepository().clearTable()
            // Add sample words.
            val note = Note()
            note.id = 1
            note.title = "title 1"
            note.content = "content 1"
            note.createdAt = System.currentTimeMillis()
            note.modifiedAt = System.currentTimeMillis()
            getRepository().insert(note)
            note.id = 2
            note.title = "title 2"
            note.content = "content 2"
            note.createdAt = System.currentTimeMillis()
            note.modifiedAt = System.currentTimeMillis()
            getRepository().insert(note)
        } }
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


    private suspend fun loadNotes(): LiveData<List<Note>> {
        return withContext(Dispatchers.IO) {
            getRepository().allNotes
        }
    }

    fun getNotesWithCategory(): LiveData<List<NotesWithCategory>> {
        return notesWithCategory
    }

    private val notesWithCategory: MutableLiveData<List<NotesWithCategory>> by lazy {
        MutableLiveData<List<NotesWithCategory>>().also {
            loadNotesWithCategory()
        }
    } // TODO what is this?

    private fun loadNotesWithCategory() {
        CoroutineScope(Dispatchers.Main + Job()).launch {
        }
    }
}