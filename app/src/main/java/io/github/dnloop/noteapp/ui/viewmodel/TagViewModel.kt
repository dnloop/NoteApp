package io.github.dnloop.noteapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import io.github.dnloop.noteapp.data.*
import kotlinx.coroutines.*


/**
 * This class deals with the insertion of notes, it must be used in combination of NoteTag.
 */
class TagViewModel(
    noteKeyId: Long,
    private val tagDataSource: TagDao,
    private val noteTagDataSource: NoteTagDao
) : ViewModel() {
    private val viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private suspend fun getTagRepository(): TagRepository {
        return withContext(Dispatchers.IO) {
            TagRepository(tagDataSource)
        }
    }

    /* BEG TEST */

    fun onInsertDummy(id: Long): Long = runBlocking {
        return@runBlocking withContext(Dispatchers.Default) {
            insertDummy(id)
        }
    }

    private suspend fun insertDummy(id: Long): Long {
        return withContext(Dispatchers.IO) { id }
    }

    /* END TEST */

    private suspend fun getNoteTagRepository(): NoteTagRepository {
        return withContext(Dispatchers.IO) {
            NoteTagRepository(noteTagDataSource)
        }
    }
    fun onInsertTag(tag: Tag): Long = runBlocking {
        return@runBlocking withContext(Dispatchers.Default) {
            insertTag(tag)
        }
    }

    private suspend fun insertTag(tag: Tag): Long {
        return withContext(Dispatchers.IO) {
            getTagRepository().insert(tag)
        }
    }

    fun onAttach(noteId: Long, tagId: Long) {
        CoroutineScope(Dispatchers.Main + Job()).launch {
            withContext(Dispatchers.IO) {
                getNoteTagRepository().attach(noteId, tagId)
            }
        }
    }

    fun onDetach(noteId: Long, tagId: Long) {
        CoroutineScope(Dispatchers.Main + Job()).launch {
            withContext(Dispatchers.IO) {
                getNoteTagRepository().detach(noteId, tagId)
            }
        }
    }
}