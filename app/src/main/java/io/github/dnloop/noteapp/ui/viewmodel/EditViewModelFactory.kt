package io.github.dnloop.noteapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.dnloop.noteapp.data.NoteDao

class EditNoteViewModelFactory(
    private val noteKeyId: Long,
    private val dataSourceNote: NoteDao
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditNoteViewModel::class.java)) {
            return EditNoteViewModel(noteKeyId, dataSourceNote) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}