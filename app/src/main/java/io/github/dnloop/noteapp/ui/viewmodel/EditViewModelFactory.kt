package io.github.dnloop.noteapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.dnloop.noteapp.data.CategoryDao
import io.github.dnloop.noteapp.data.NoteDao

@Suppress("UNCHECKED_CAST")
class EditNoteViewModelFactory(
    private val noteKeyId: Long,
    private val dataSourceNote: NoteDao,
    private val dataSourceCategory: CategoryDao
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditNoteViewModel::class.java)) {
            return EditNoteViewModel(noteKeyId, dataSourceNote, dataSourceCategory ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}