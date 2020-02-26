package io.github.dnloop.noteapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.dnloop.noteapp.data.Category

class CategoryViewModel : ViewModel() {

    private val notes: MutableLiveData<List<Category>> by lazy {
        MutableLiveData<List<Category>>().also {
            loadCategories()
        }
    }

    fun getCategories(): LiveData<List<Category>> {
        return notes
    }

    private fun loadCategories() {
        // Do an asynchronous operation to fetch categories.
    }
}