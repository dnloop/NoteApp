package io.github.dnloop.noteapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.dnloop.noteapp.data.NoteDatabase

@Suppress("UNCHECKED_CAST")
class ShareViewModelFactory(
    private val application: Application,
    private val dataSource: NoteDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShareViewModel::class.java)) {
            return ShareViewModel(application, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}