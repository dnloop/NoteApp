package io.github.dnloop.noteapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.dnloop.noteapp.data.NoteDao

class TrashViewModelFactory (
    private val dataSource: NoteDao,
    private val application: Application
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TrashViewModel::class.java)) {
            return TrashViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}