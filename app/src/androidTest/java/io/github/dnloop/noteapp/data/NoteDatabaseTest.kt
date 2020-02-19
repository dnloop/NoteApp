package io.github.dnloop.noteapp.data

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry

import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class NoteDatabaseTest {

    private lateinit var db : NoteDatabase

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, NoteDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun getNoteDao() {
        assertNotNull("Null database",db.noteDao)
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetNote() {
        val note = Note(
            id = 1,
            title = "title",
            content = "content",
            categoryId = 0,
            archived = false,
            deleted = false,
            createdAt = System.currentTimeMillis(),
            modifiedAt = System.currentTimeMillis(),
            deletedAt = null
        )
        db.noteDao.insert(note)
        val newNote = db.noteDao.getLatest()
        assertEquals(newNote?.categoryId, 0)
        assertNotNull("Null database",db.noteDao)
    }
}