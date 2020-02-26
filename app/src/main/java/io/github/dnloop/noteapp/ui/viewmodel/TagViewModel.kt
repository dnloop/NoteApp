package io.github.dnloop.noteapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.dnloop.noteapp.data.Tag

class TagViewModel : ViewModel() {
    private val tags: MutableLiveData<List<Tag>> by lazy {
        MutableLiveData<List<Tag>>().also {
            loadTags()
        }
    }

    fun getTags(): LiveData<List<Tag>> {
        return tags
    }

    private fun loadTags() {
        // Do an asynchronous operation to fetch tags.
    }
}