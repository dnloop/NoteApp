package io.github.dnloop.noteapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.dnloop.noteapp.data.Note

class HomeViewModel : ViewModel() {

    private val notes: MutableLiveData<List<Note>> by lazy {
        MutableLiveData<List<Note>>().also {
            loadNotes()
        }
    }

    fun getNotes(): LiveData<List<Note>> {
        return notes
    }

    private fun loadNotes() {
        // Do an asynchronous operation to fetch users.
    }
}