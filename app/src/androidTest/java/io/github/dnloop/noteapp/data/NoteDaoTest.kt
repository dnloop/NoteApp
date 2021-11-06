package io.github.dnloop.noteapp.data

import android.database.sqlite.SQLiteConstraintException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import timber.log.Timber
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
        assertNotNull("Null database", noteDao)
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
            color = null,
            deleted = false,
            createdAt = System.currentTimeMillis(),
            modifiedAt = System.currentTimeMillis(),
            deletedAt = null
        )
        noteDao.insert(note)
        val newNote = db.noteDao.getLatest()
        assertEquals(1, newNote?.id)
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
        val newNote = noteDao.getLatest()
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
        val newNote = noteDao.getLatest()
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
        val newNote = noteDao.getLatest()
        assertEquals(retCategory, 1)
        assertEquals(retNote, 1)
        assertEquals(1, newNote?.categoryId )
    }

    /**
     * Retrieves the count of notes within a category.
     */
    @Test
    @Throws(Exception::class)
    fun countNotes() {
        val note = Note()
        val category = Category()
        category.id = 1
        category.name = "testCategory"
        note.title = "no category"
        noteDao.insert(note) // a note without category
        note.title = "with category"
        categoryDao.insert(category)
        noteDao.insertWithCategory(note, category) // another note with category
        // check if category exists
        val notes = categoryDao.getNotesCount(category.id)
        assertEquals(1L, notes )
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
        val newNote = noteDao.getLatest()
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
        val newNote = noteDao.getLatest()
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
     * Retrieve a LiveData List of archived notes.
     */
    @Test
    @Throws(Exception::class)
    fun getAllArchived() {
        val note = Note()
        // insert multiple fields
        for (i in 1..5) {
            note.id = i.toLong()
            note.archived = true
            noteDao.insert(note)
        }
        // check assertion
        val liveData = noteDao.getAllArchivedNotes()
        liveData.observeForever{}
        assertEquals(false , liveData.value.isNullOrEmpty())
        assertEquals(5, liveData.value?.size)
    }

    /**
     * Retrieve a LiveData List of soft deleted notes.
     */
    @Test
    @Throws(Exception::class)
    fun getAllDeleted() {
        val note = Note()
        // insert multiple fields
        for (i in 1..5) {
            note.id = i.toLong()
            note.deleted = true
            noteDao.insert(note)
        }
        // check assertion
        val liveData = noteDao.getAllDeletedNotes()
        liveData.observeForever{}
        assertEquals(false , liveData.value.isNullOrEmpty())
        assertEquals(5, liveData.value?.size)
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
            note.id = i.toLong()
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
     * Retrieves a list of notes with categories.
     */
    @Test
    @Throws(Exception::class)
    fun getNotesWithCategories() {
        // Insert record
        val note = Note()
        val category = Category()
        category.id = 1
        category.name = "catName1"
        categoryDao.insert(category)

        for (i in 1..5){
            note.id = i.toLong()
            note.title = "Note_$i"
            noteDao.insertWithCategory(note, category)
        }

        category.id = 2
        category.name = "catName2"
        categoryDao.insert(category)
        note.id = 6
        note.title = "Note_6"
        noteDao.insertWithCategory(note, category)
        // check if category exists
        val list = noteDao.getAllNotesWithCategories()
        list.observeForever{}
        assertEquals("catName2", list.value?.get(5)?.category?.name )
        assertEquals(6, list.value?.size )
    }

    /**
     * Test whether its possible to obtain the hash from a room database.
     * */
    @Test
    @Throws(Exception::class)
    fun getIdentityHash() {
        val query  = SimpleSQLiteQuery("SELECT identity_hash FROM room_master_table")
        val identityHash = noteDao.getIdentityHash(query)
        assertEquals(true, !identityHash.isBlank())
    }
}