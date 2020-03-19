package io.github.dnloop.noteapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.dnloop.noteapp.data.CategoryDao

@Suppress("UNCHECKED_CAST")
class CategoryViewModelFactory(
    private val categoryDataSource: CategoryDao,
    private val application: Application
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            return CategoryViewModel(categoryDataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}