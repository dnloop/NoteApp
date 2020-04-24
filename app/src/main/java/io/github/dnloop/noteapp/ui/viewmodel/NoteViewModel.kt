package io.github.dnloop.noteapp.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.dnloop.noteapp.R
import io.github.dnloop.noteapp.data.NoteDao
import io.github.dnloop.noteapp.data.NoteRepository
import io.github.dnloop.noteapp.data.NoteWithCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import timber.log.Timber

class NoteViewModel(val dataSource: NoteDao, application: Application) :
    AndroidViewModel(application) {

    private val _context = getApplication<Application>().applicationContext

    private val _navigateToEditor = MutableLiveData<Long>()

    val navigateToEditor
        get() = _navigateToEditor

    fun onNoteEditorNavigated() {
        _navigateToEditor.value = null
    }

    fun onNoteSelected(item: Long) {
        _navigateToEditor.value = item
    }

    private val _title = MutableLiveData<String>()

    val title: LiveData<String>
        get() = _title

    fun updateLabel(title: String) = _title.postValue(title)


    private suspend fun getRepository(): NoteRepository {
        return withContext(Dispatchers.IO) {
            NoteRepository(dataSource)
        }
    }

    override fun onCleared() {
        super.onCleared()
        Job().cancel()
    }


    fun loadNotes(): LiveData<List<NoteWithCategory>> {
        return runBlocking {
            withContext(Dispatchers.IO) {
                getRepository().allNotes
            }
        }
    }

    fun loadNotesByCategory(key: Long): LiveData<List<NoteWithCategory>> {
        return runBlocking {
            withContext(Dispatchers.IO) {
                getRepository().getNotesWithCategory(key)
            }
        }
    }

    fun loadArchivedNotes(): LiveData<List<NoteWithCategory>> {
        return runBlocking {
            withContext(Dispatchers.IO) {
                getRepository().allArchivedNotes
            }
        }
    }

    fun loadPreferences() {
        runBlocking {
            withContext(Dispatchers.IO) {
                val sharedPreferences = _context.getSharedPreferences(
                    _context.getString(R.string.preference_file_key), Context.MODE_PRIVATE
                )
                val str = getRepository().identityHash()
                with(sharedPreferences.edit()) {
                    putString("db_identity_hash", str)
                    commit()
                }
            }
        }
    }
}