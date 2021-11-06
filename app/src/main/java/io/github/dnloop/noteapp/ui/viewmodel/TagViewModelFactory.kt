package io.github.dnloop.noteapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.dnloop.noteapp.data.NoteDao
import io.github.dnloop.noteapp.data.NoteTagDao
import io.github.dnloop.noteapp.data.TagDao

class TagViewModelFactory (
    private val noteKeyId: Long,
    private val tagDataSource: TagDao,
    private val noteDataSource: NoteDao,
    private val noteTagDataSource: NoteTagDao
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TagViewModel::class.java)) {
            return TagViewModel(noteKeyId, tagDataSource, noteDataSource, noteTagDataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}