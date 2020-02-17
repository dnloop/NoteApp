package io.github.dnloop.noteapp.ui.archive

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.dnloop.noteapp.data.Note

class ArchiveViewModel : ViewModel() {
    private val notes: MutableLiveData<List<Note>> by lazy {
        MutableLiveData<List<Note>>().also {
            loadArchivedNotes()
        }
    }

    fun getArchivedNotes(): LiveData<List<Note>> {
        return notes
    }

    private fun loadArchivedNotes() {
        // Do an asynchronous operation to fetch archived notes.
    }
}
