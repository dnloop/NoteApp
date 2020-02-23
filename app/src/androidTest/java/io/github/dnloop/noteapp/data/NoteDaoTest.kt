package io.github.dnloop.noteapp.data

import android.database.sqlite.SQLiteConstraintException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class NoteDaoTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var db: NoteDatabase
    private lateinit var noteDao: NoteDao
    private lateinit var categoryDao: CategoryDao

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, NoteDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        noteDao = db.noteDao
        categoryDao = db.categoryDao
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        db.close()
    }

    /**
     * Basic test. The database is created
     * */
    @Test
    @Throws(Exception::class)
    fun getNoteDao() {
        assertNotNull("Null database", db.noteDao)
    }

    /**
     * The database is working as expected by inserting
     * and retrieving a row.
     * */
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
        noteDao.insert(note)
        val newNote = db.noteDao.getLatest()
        assertEquals(newNote?.categoryId, 0)
        assertNotNull("Null database", db.noteDao)
    }

    /**
     * A new record is inserted with a valid id.
     * No validations performed.
     * */
    @Test
    @Throws(Exception::class)
    fun insertEmpty() {
        val note = Note()
        note.id = 1
        val ret: Long = noteDao.insert(note)
        val newNote = db.noteDao.getLatest()
        assertEquals(1, ret)
        assertEquals(1, newNote?.id)
    }


    /**
     * Expected exception based on insert OnConflictStrategy.ABORT
     */
    @Test(expected = SQLiteConstraintException::class)
    fun insertConflict() {
        val note1 = Note()
        val note2 = Note()
        note1.id = 1
        note2.id = 1
        noteDao.insert(note1) // OK
        noteDao.insert(note2) // FAIL
    }


    /**
     * Timestamps must differ once modified.*/
    @Test
    @Throws(Exception::class)
    fun insertWithTimestamp() {
        val note = Note()
        val ret: Long = noteDao.insertWithTimestamp(note)
        val newNote = db.noteDao.getLatest()
        assertEquals(1, ret)
        assertNotEquals(0, newNote?.createdAt)
        assertNotEquals(0, newNote?.modifiedAt)
    }

    /**
     * Inserts an existing category to a new note.
     */
    @Test
    @Throws(Exception::class)
    fun insertWithCategory() {
        val note = Note()
        val category = Category()
        category.id = 1
        val retCategory: Long = categoryDao.insert(category)
        val retNote: Long = noteDao.insertWithCategory(note, category)
        // check if category exists
        val newNote = db.noteDao.getLatest()
        assertEquals(retCategory, 1)
        assertEquals(retNote, 1)
        assertEquals(1, newNote?.categoryId )
    }

    /**
     * Basic update test.
     */
    @Test
    @Throws(Exception::class)
    fun updateNote() {
        val note = Note()
        note.id = 1
        note.title = "title"
        // insert 'existing' value
        val retNote: Long = noteDao.insert(note)
        assertEquals(retNote, 1)
        // perform update
        note.title = "newTitle"
        noteDao.update(note)
        val newNote = db.noteDao.getLatest()
        assertEquals("newTitle", newNote?.title)
    }

    /**
     * Timestamps must differ to pass.
     */
    @Test
    @Throws(Exception::class)
    fun updateWithTimestamp() {
        val note = Note()
        note.id = 1
        note.title = "title"
        // createdAt and modifiedAt are the same
        val retNote: Long = noteDao.insertWithTimestamp(note)
        assertEquals(retNote, 1)
        // perform an update
        note.title = "newTitle"
        noteDao.updateWithTimestamp(note)
        // check assertion
        val newNote = db.noteDao.getLatest()
        assertEquals("newTitle", newNote?.title)
        assertNotEquals(newNote?.createdAt, newNote?.modifiedAt)
    }

    /**
     * Retrieve a LiveData record with its id.
     */
    @Test
    @Throws(Exception::class)
    fun getId() {
        val note = Note()
        note.id = 1
        val retNote: Long = noteDao.insert(note)
        assertEquals(1, retNote)
        // check assertion
        val liveData = noteDao.get(1)
        liveData.observeForever {value -> value.id}
        assertEquals(1, liveData.value?.id)
    }

    /**
     * Retrieve a LiveData List and delete the database.
     */
    @Test
    @Throws(Exception::class)
    fun getAllAndClearDatabase() {
        val note = Note()
        // insert multiple fields
        for (i in 1..5){
            note.id = i
            noteDao.insert(note)
        }
        // check assertion
        var liveData = noteDao.getAllNotes()
        liveData.observeForever{}
        assertEquals(false , liveData.value.isNullOrEmpty())
        assertEquals(5, liveData.value?.size)
        // clear database
        noteDao.clear()
        liveData = noteDao.getAllNotes()
        liveData.observeForever {}
        assertEquals(true, liveData.value?.isEmpty())

    }

    /**
     * Retrieves notes with categories.
     */
    @Test
    @Throws(Exception::class)
    fun getNotesWithCategory() {
        // Insert record
        val note = Note()
        val category = Category()
        category.id = 1
        category.name = "catName"
        val retCategory: Long = categoryDao.insert(category)
        assertEquals(retCategory, 1)
        val retNote: Long = noteDao.insertWithCategory(note, category)
        assertEquals(retNote, 1)
        // check if category exists
        val liveData = noteDao.getNotesWithCategory()

        liveData.observeForever{}
        assertEquals("catName", liveData.value?.get(0)?.category?.name )
    }
}