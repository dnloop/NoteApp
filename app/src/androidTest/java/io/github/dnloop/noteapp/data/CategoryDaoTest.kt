package io.github.dnloop.noteapp.data

import android.database.sqlite.SQLiteConstraintException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class CategoryDaoTest {
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
    fun getCategoryDao() {
        Assert.assertNotNull("Null database", db.categoryDao)
    }

    /**
     * The database is working as expected by inserting
     * and retrieving a row.
     * */
    @Test
    @Throws(Exception::class)
    fun insertAndGetCategory() {
        val category = Category(
            id = 1,
            name = "title",
            deleted = false,
            createdAt = System.currentTimeMillis(),
            modifiedAt = System.currentTimeMillis(),
            deletedAt = null
        )
        categoryDao.insert(category)
        val newCat = db.categoryDao.getLatest()
        Assert.assertEquals(1, newCat?.id)
    }

    /**
     * A new record is inserted with a valid id.
     * No validations performed.
     * */
    @Test
    @Throws(Exception::class)
    fun insertEmpty() {
        val category = Category()
        category.id = 1
        val ret: Long = categoryDao.insert(category)
        val newCategory = categoryDao.getLatest()
        Assert.assertEquals(1, ret)
        Assert.assertEquals(1, newCategory?.id)
    }


    /**
     * Expected exception based on insert OnConflictStrategy.ABORT
     */
    @Test(expected = SQLiteConstraintException::class)
    fun insertConflict() {
        val category1 = Category()
        val category = Category()
        category1.id = 1
        category.id = 1
        categoryDao.insert(category1) // OK
        categoryDao.insert(category) // FAIL
    }


    /**
     * Timestamps must differ once modified.*/
    @Test
    @Throws(Exception::class)
    fun insertWithTimestamp() {
        val category = Category()
        val ret: Long = categoryDao.insertWithTimestamp(category)
        val newCategory = db.categoryDao.getLatest()
        Assert.assertEquals(1, ret)
        Assert.assertNotEquals(0, newCategory?.createdAt)
        Assert.assertNotEquals(0, newCategory?.modifiedAt)
    }

    /**
     * Basic update test.
     */
    @Test
    @Throws(Exception::class)
    fun updateCategory() {
        val category = Category()
        category.id = 1
        category.name = "name"
        // insert 'existing' value
        val retCategory: Long = categoryDao.insert(category)
        Assert.assertEquals(1, retCategory)
        // perform update
        category.name = "newTitle"
        categoryDao.update(category)
        val newCategory = db.categoryDao.getLatest()
        Assert.assertEquals("newTitle", newCategory?.name)
    }

    /**
     * Timestamps must differ to pass.
     */
    @Test
    @Throws(Exception::class)
    fun updateWithTimestamp() {
        val category = Category()
        category.id = 1
        category.name = "name"
        // createdAt and modifiedAt are the same
        val retCategory: Long = categoryDao.insertWithTimestamp(category)
        Assert.assertEquals(1, retCategory)
        // perform an update
        category.name = "newTitle"
        categoryDao.updateWithTimestamp(category)
        // check assertion
        val newCategory = db.categoryDao.getLatest()
        Assert.assertEquals("newTitle", newCategory?.name)
        Assert.assertNotEquals(newCategory?.createdAt, newCategory?.modifiedAt)
    }

    /**
     * Retrieve a LiveData record with its id.
     */
    @Test
    @Throws(Exception::class)
    fun getId() {
        val category = Category()
        category.id = 1
        val retCategory: Long = categoryDao.insert(category)
        Assert.assertEquals(1, retCategory)
        // check assertion
        val liveData = categoryDao.get(1)
        liveData.observeForever { value -> value.id }
        Assert.assertEquals(1, liveData.value?.id)
    }

    /**
     * Retrieve a LiveData List and delete the database.
     */
    @Test
    @Throws(Exception::class)
    fun getAllAndClearDatabase() {
        val category = Category()
        // insert multiple fields
        for (i in 1..5){
            category.id = i.toLong()
            categoryDao.insert(category)
        }
        // check assertion
        var liveData = categoryDao.getAllCategories()
        liveData.observeForever{} // messy but functional
        Assert.assertEquals(false, liveData.value.isNullOrEmpty())
        Assert.assertEquals(5, liveData.value?.size)
        // clear database
        categoryDao.clear()
        liveData = categoryDao.getAllCategories()
        liveData.observeForever {}
        Assert.assertEquals(true, liveData.value?.isEmpty())

    }
}