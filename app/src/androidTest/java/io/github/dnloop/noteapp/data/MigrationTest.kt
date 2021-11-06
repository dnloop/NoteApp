package io.github.dnloop.noteapp.data


import androidx.room.Room
import androidx.room.migration.Migration
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class MigrationTest {
    private val testDB = "migration-test"

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE Note ADD COLUMN color TEXT")
        }
    }

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        NoteDatabase::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    @Throws(IOException::class)
    fun migrate1To2() {
        helper.createDatabase(testDB, 1).apply {
            // db has schema version 1. insert some data using SQL queries.
            // You cannot use DAO classes because they expect the latest schema.
            execSQL("INSERT INTO Note (title, content, categoryId, archived," +
                    " deleted, createdAt, modifiedAt, deletedAt) " +
                    "VALUES('Title', 'Content', null, 0, 0, 0, 0, null)")

            // Prepare for the next version.
            close()
        }

        // Re-open the database with version 2 and provide
        // MIGRATION_1_2 as the migration process.
        helper.runMigrationsAndValidate(testDB, 2, true, MIGRATION_1_2)
        val noteDB = getMigratedRoomDatabase()
        val note: Note? = noteDB?.noteDao?.getLatest()
        assertEquals(1L, note?.id)
        assertEquals(null, note?.color)
        // MigrationTestHelper automatically verifies the schema changes,
        // but you need to validate that the data was migrated properly.
    }

    private fun getMigratedRoomDatabase(): NoteDatabase? {
        val database: NoteDatabase = Room.databaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NoteDatabase::class.java, testDB
        )
            .addMigrations(MIGRATION_1_2)
            .build()
        // close the database and release any stream resources when the test finishes
        helper.closeWhenFinished(database)
        return database
    }

}