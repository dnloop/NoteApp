package io.github.dnloop.noteapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.dnloop.noteapp.data.CategoryDao

@Suppress("UNCHECKED_CAST")
class CategorySelectorViewModelFactory(
    private val categoryDataSource: CategoryDao,
    private val application: Application
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategorySelectorViewModel::class.java)) {
            return CategorySelectorViewModel(categoryDataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}