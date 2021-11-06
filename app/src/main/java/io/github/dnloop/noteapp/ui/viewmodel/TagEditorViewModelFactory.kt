package io.github.dnloop.noteapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.dnloop.noteapp.data.TagDao

class TagEditorViewModelFactory(
    private val tagDataSource: TagDao,
    private val application: Application
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TagEditorViewModel::class.java)) {
            return TagEditorViewModel(tagDataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}