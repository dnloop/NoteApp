package io.github.dnloop.noteapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import io.github.dnloop.noteapp.data.*
import kotlinx.coroutines.*
import javax.sql.DataSource


/**
 * This class deals with the insertion of notes, it must be used in combination of NoteTag.
 */
class TagViewModel(
    noteKeyId: Long,
    val tagDataSource:  TagDao,
    val noteDataSource: NoteDao,
    private val noteTagDataSource: NoteTagDao
) : ViewModel() {
    private val viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private suspend fun getNoteRepository(): NoteRepository {
        return withContext(Dispatchers.IO) {
            NoteRepository(noteDataSource)
        }
    }

    private suspend fun getNoteTagRepository(): NoteTagRepository {
        return withContext(Dispatchers.IO) {
            NoteTagRepository(noteTagDataSource)
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