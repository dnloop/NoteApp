package io.github.dnloop.noteapp.data

import android.database.sqlite.SQLiteConstraintException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.*
import java.io.IOException

class TagDaoTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var db: NoteDatabase
    private lateinit var tagDao: TagDao

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, NoteDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
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
        Assert.assertNotNull("Null database", tagDao)
    }

    /**
     * The database is working as expected by inserting
     * and retrieving a row.
     * */
    @Test
    @Throws(Exception::class)
    fun insertAndGetTag() {
        val tag = Tag(
            id = 1,
            name = "title",
            deleted = false,
            createdAt = System.currentTimeMillis(),
            modifiedAt = System.currentTimeMillis(),
            deletedAt = null
        )
        tagDao.insert(tag)
        val newCat = tagDao.getLatest()
        Assert.assertEquals(1, newCat?.id)
    }

    /**
     * A new record is inserted with a valid id.
     * No validations performed.
     * */
    @Test
    @Throws(Exception::class)
    fun insertEmpty() {
        val tag = Tag()
        tag.id = 1
        val ret: Long = tagDao.insert(tag)
        val newTag = tagDao.getLatest()
        Assert.assertEquals(1, ret)
        Assert.assertEquals(1, newTag?.id)
    }


    /**
     * Expected exception based on insert OnConflictStrategy.ABORT
     */
    @Test(expected = SQLiteConstraintException::class)
    fun insertConflict() {
        val tag1 = Tag()
        val tag2 = Tag()
        tag1.id = 1
        tag2.id = 1
        tagDao.insert(tag1) // OK
        tagDao.insert(tag2) // FAIL
    }


    /**
     * Timestamps must differ once modified.*/
    @Test
    @Throws(Exception::class)
    fun insertWithTimestamp() {
        val tag = Tag()
        val ret: Long = tagDao.insertWithTimestamp(tag)
        val newTag = tagDao.getLatest()
        Assert.assertEquals(1, ret)
        Assert.assertNotEquals(0, newTag?.createdAt)
        Assert.assertNotEquals(0, newTag?.modifiedAt)
    }

    /**
     * Basic update test.
     */
    @Test
    @Throws(Exception::class)
    fun updateTag() {
        val tag = Tag()
        tag.id = 1
        tag.name = "name"
        // insert 'existing' value
        val retTag: Long = tagDao.insert(tag)
        Assert.assertEquals(1, retTag)
        // perform update
        tag.name = "newTitle"
        tagDao.update(tag)
        val newTag = tagDao.getLatest()
        Assert.assertEquals("newTitle", newTag?.name)
    }

    /**
     * Timestamps must differ to pass.
     */
    @Test
    @Throws(Exception::class)
    fun updateWithTimestamp() {
        val tag = Tag()
        tag.id = 1
        tag.name = "name"
        // createdAt and modifiedAt are the same
        val retTag: Long = tagDao.insertWithTimestamp(tag)
        Assert.assertEquals(1, retTag)
        // perform an update
        tag.name = "newTitle"
        tagDao.updateWithTimestamp(tag)
        // check assertion
        val newTag = tagDao.getLatest()
        Assert.assertEquals("newTitle", newTag?.name)
        Assert.assertNotEquals(newTag?.createdAt, newTag?.modifiedAt)
    }

    /**
     * Retrieve a LiveData record with its id.
     */
    @Test
    @Throws(Exception::class)
    fun getId() {
        val tag = Tag()
        tag.id = 1
        val retTag: Long = tagDao.insert(tag)
        Assert.assertEquals(1, retTag)
        // check assertion
        val liveData = tagDao.get(1)
        liveData.observeForever { value -> value.id }
        Assert.assertEquals(1, liveData.value?.id)
    }

    /**
     * Retrieve a LiveData List and delete the database.
     */
    @Test
    @Throws(Exception::class)
    fun getAllAndClearDatabase() {
        val tag = Tag()
        // insert multiple fields
        for (i in 1..5){
            tag.id = i
            tagDao.insert(tag)
        }
        // check assertion
        var liveData = tagDao.getAllTags()
        liveData.observeForever{} // messy but functional
        Assert.assertEquals(false, liveData.value.isNullOrEmpty())
        Assert.assertEquals(5, liveData.value?.size)
        // clear database
        tagDao.clear()
        liveData = tagDao.getAllTags()
        liveData.observeForever {}
        Assert.assertEquals(true, liveData.value?.isEmpty())

    }
}