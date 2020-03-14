package io.github.dnloop.noteapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.dnloop.noteapp.data.NoteTagDao
import io.github.dnloop.noteapp.data.TagDao

@Suppress("UNCHECKED_CAST")
class TagViewModelFactory (
    private val noteKeyId: Long,
    private val noteDataSource: NoteTagDao,
    private val noteTagDataSource: NoteTagDao
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TagViewModel::class.java)) {
            return TagViewModel(noteKeyId, noteDataSource, noteTagDataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}