package io.github.dnloop.noteapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.dnloop.noteapp.data.NoteDao

@Suppress("UNCHECKED_CAST")
class EditNoteViewModelFactory (
    private val noteKeyId: Long,
    private val dataSource: NoteDao
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditNoteViewModel::class.java)) {
            return EditNoteViewModel(noteKeyId, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}