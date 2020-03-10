package io.github.dnloop.noteapp.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.*
import java.io.IOException

class NoteTagDaoTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var db: NoteDatabase
    private lateinit var noteDao: NoteDao
    private lateinit var tagDao: TagDao
    private lateinit var noteTagDao: NoteTagDao

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, NoteDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        noteDao = db.noteDao
        noteTagDao = db.noteTagDao
        tagDao = db.tagDao
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
    fun getTagDao() {
        Assert.assertNotNull("Null database", noteTagDao)
    }

    /**
     * The database is working as expected by inserting
     * a row (many-to-many).
     * */
    @Test
    @Throws(Exception::class)
    fun insertAndGetNoteTag() {
        val tag = Tag(
            id = 1,
            name = "title",
            deleted = false,
            createdAt = System.currentTimeMillis(),
            modifiedAt = System.currentTimeMillis(),
            deletedAt = null
        )
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
        val crossRef = NoteTagCrossRef(tag.id, note.id)
        val ret= noteTagDao.insert(crossRef)

        Assert.assertEquals(1, ret)
    }

    /**
     * A new record is inserted with a valid id.
     * No validations performed.
     * */
    @Test
    @Throws(Exception::class)
    fun insertEmpty() {
        val tag = Tag()
        val note = Note()
        tag.id = 1
        note.id = 1
        val crossRef = NoteTagCrossRef(tag.id, note.id)
        val ret: Long = noteTagDao.insert(crossRef)
        Assert.assertEquals(1, ret)
    }


    /**
     * No exception expected based on insert OnConflictStrategy.Ignore
     */
    @Test
    fun insertConflict() {
        val tag1 = Tag()
        val tag2 = Tag()
        val note1 = Note()
        val note2 = Note()
        tag1.id = 1
        tag2.id = 1
        note1.id = 1
        note2.id = 1

        val crossRef1 = NoteTagCrossRef(note1.id, tag1.id)
        val crossRef2 = NoteTagCrossRef(note2.id, tag2.id)

        noteTagDao.insert(crossRef1) // OK
        noteTagDao.insert(crossRef2) // OK
    }

    /**
     * Attach a List of tags to notes.
     * */
    @Test
    @Throws(Exception::class)
    fun batchInsertTags() {
        // Create some tags and Notes
        val tag1 = Tag()
        val tag2 = Tag()
        val tag3 = Tag()
        val note1 = Note()
        val note2 = Note()
        val note3 = Note()
        tag1.id = 1
        tag2.id = 2
        tag3.id = 3
        note1.id = 1
        note2.id = 2
        note3.id = 3
        tagDao.insert(tag1)
        tagDao.insert(tag2)
        tagDao.insert(tag3)
        noteDao.insert(note1)
        noteDao.insert(note2)
        noteDao.insert(note3)

        val tagCrossRef1 = NoteTagCrossRef(
            noteId = 1,
            tagId = 1
        )
        val tagCrossRef2 = NoteTagCrossRef(
            noteId = 2,
            tagId = 2
        )
        val tagCrossRef3 = NoteTagCrossRef(
            noteId = 3,
            tagId = 3
        )

        // add to List
        val list: List<NoteTagCrossRef> = arrayListOf(tagCrossRef1, tagCrossRef2, tagCrossRef3)

        noteTagDao.insertAll(list)
        val allTags = noteDao.getNoteWithTags()
        allTags.observeForever{} // messy but functional
        Assert.assertEquals(false, allTags.value.isNullOrEmpty())
        Assert.assertEquals(3, allTags.value?.size)
    }

    /**
     * Detach a List of tags of a note.
     * */
    @Test
    @Throws(Exception::class)
    fun batchDeleteTags() {
        // Create some tags and a Note
        val tag1 = Tag()
        val tag2 = Tag()
        val tag3 = Tag()
        val note1 = Note()
        tag1.id = 1
        tag2.id = 2
        tag3.id = 3
        note1.id = 1
        tagDao.insert(tag1)
        tagDao.insert(tag2)
        tagDao.insert(tag3)
        noteDao.insert(note1)

        val tagCrossRef1 = NoteTagCrossRef(
            noteId = 1,
            tagId = 1
        )
        val tagCrossRef2 = NoteTagCrossRef(
            noteId = 1,
            tagId = 2
        )
        val tagCrossRef3 = NoteTagCrossRef(
            noteId = 1,
            tagId = 3
        )
        // add to List
        val list: List<NoteTagCrossRef> = arrayListOf(tagCrossRef1, tagCrossRef2, tagCrossRef3)

        noteTagDao.insertAll(list)

        noteTagDao.deleteAll(list)
        val allTags = noteDao.getNoteWithTags()
        allTags.observeForever{} // messy but functional
        Assert.assertEquals(true, allTags.value?.get(0)?.tags?.isEmpty())
    }

    /**
     * Retrieve a LiveData List of Notes with Tags.
     */
    @Test
    @Throws(Exception::class)
    fun getNoteWithTags() {
        val tag = Tag()
        val note = Note()
        tag.id = 1
        note.id = 1
        noteDao.insert(note)
        tagDao.insert(tag)
        val crossRef = NoteTagCrossRef(note.id, tag.id)
        val retTag: Long = noteTagDao.insert(crossRef)
        Assert.assertEquals(1, retTag)
        // check assertion
        val list = noteDao.getNoteWithTags()
        list.observeForever{}
        Assert.assertEquals(false, list.value?.isNullOrEmpty())
    }

    /**
     * Retrieve a LiveData List of Tags with Notes.
     */
    @Test
    @Throws(Exception::class)
    fun getTagsWithNotes() {
        val tag = Tag()
        val note = Note()
        tag.id = 1
        note.id = 1
        noteDao.insert(note)
        tagDao.insert(tag)
        val crossRef = NoteTagCrossRef(note.id, tag.id)
        val retTag: Long = noteTagDao.insert(crossRef)
        Assert.assertEquals(1, retTag)
        // check assertion
        val list = noteDao.getTagWithNotes()
        list.observeForever{}
        Assert.assertEquals(false, list.value?.isNullOrEmpty())
    }

    /**
     * Retrieve a LiveData List of Tags with Notes and delete it.
     */
    @Test
    @Throws(Exception::class)
    fun getAllAndClearDatabase() {
        val tag = Tag()
        val note = Note()
        val crossRef = NoteTagCrossRef()
        note.id = 1
        tag.id = 1
        noteDao.insert(note)
        tagDao.insert(tag)
        crossRef.noteId = note.id
        crossRef.tagId = tag.id
        noteTagDao.insert(crossRef)
        var list = noteDao.getTagWithNotes()
        // check assertion
        list.observeForever{}
        Assert.assertEquals(false, list.value?.isNullOrEmpty())
        // delete record
        noteTagDao.delete(crossRef)
        list = noteDao.getTagWithNotes()
        list.observeForever{}
        Assert.assertEquals(true, list.value?.get(0)?.notes?.isEmpty())
    }
}