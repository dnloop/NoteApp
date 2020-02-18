package io.github.dnloop.noteapp.ui.folder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.dnloop.noteapp.data.Note

class FolderViewModel: ViewModel() {

    private val folder: MutableLiveData<List<Note>> by lazy {
        MutableLiveData<List<Note>>().also { loadNotesByCategory() }
    } // notes by category

    fun getFolder(): LiveData<List<Note>> {
        return folder
    }

    private fun loadNotesByCategory() {
        TODO("not implemented") // Do an asynchronous operation to fetch notes.
    }
}