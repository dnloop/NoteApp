package io.github.dnloop.noteapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.dnloop.noteapp.data.Note
import io.github.dnloop.noteapp.data.NoteDao
import kotlinx.coroutines.*

class EditNoteViewModel(noteKeyId: Long, dataSource: NoteDao): ViewModel() {

    private val viewModelJob = Job()

    private val note = MediatorLiveData<Note>()

    fun getNote() = note

    init {
        note.addSource(dataSource.get(noteKeyId), note::setValue)
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
}