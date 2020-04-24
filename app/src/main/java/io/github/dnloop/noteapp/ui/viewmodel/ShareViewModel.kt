package io.github.dnloop.noteapp.ui.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.provider.DocumentsContract
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import io.github.dnloop.noteapp.R
import io.github.dnloop.noteapp.data.NoteDatabase
import io.github.dnloop.noteapp.data.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.channels.FileChannel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ShareViewModel(application: Application, private val _dataSource: NoteDatabase) :
    AndroidViewModel(application) {

    private val _context = getApplication<Application>().applicationContext

    private var _granted: Boolean = false

    private lateinit var _currentDb: File

    private suspend fun getRepository(): NoteRepository {
        return withContext(Dispatchers.IO) {
            NoteRepository(_dataSource.noteDao)
        }
    }

    fun identityHash(): String {
        return runBlocking {
            withContext(Dispatchers.IO) {
                getRepository().identityHash()
            }
        }
    }

    fun exportLocalDatabase(): Boolean {
        return if (_granted) {
            fileBackup()
            true
        } else {
            false
        }
    }

    fun isPermissionGranted(): Boolean {
        return _granted
    }

    fun setPermission(granted: Boolean) {
        _granted = granted
    }

    /**
     * Checks at fragment creation if the backup folders exist, otherwise it creates it.
     * The return value is used on the file [fileBackup].
     * @return the path to the external folder.
     */
    fun checkBackupFolder(): String {
        val path = _context.getExternalFilesDir("Backup").toString()
        val dir = File(path)
        if (!dir.exists())
            dir.mkdir()
        return path
    }

    /**
     * Performs a backup operation of the entire database saving the file on the external storage.
     */
    private fun fileBackup() {
        _currentDb = _context.getDatabasePath(_dataSource.openHelper.databaseName)
        val dir = checkBackupFolder()
        val df: DateFormat = SimpleDateFormat("EEEd-MM-yyyy_HHmmss")
        val date: String = df.format(Calendar.getInstance().time)
        val fileName = "${_currentDb.name}-$date"
        val backup = File(dir, fileName)
        if (_currentDb.exists()) {
            val src: FileChannel = FileInputStream(_currentDb).channel
            val dst: FileChannel = FileOutputStream(backup).channel
            try {
                dst.transferFrom(src, 0, src.size())
                Toast.makeText(
                    _context,
                    "Database successfully exported to: $dir",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (exception: Exception) {
                Timber.e(exception)
                Toast.makeText(_context, "ALERT: Export Error", Toast.LENGTH_SHORT)
                    .show()
            } finally {
                src.close()
                dst.close()
                _dataSource.openHelper.writableDatabase
            }
        }
    }

    /**
     * Method that imports an sqlite database previously backed up.
     * If a migration was performed, the imported file becomes invalid.
     * @param dataBase The selected file to be restored.
     */
    private fun fileRestore(dataBase: File) {
        _currentDb = _context.getDatabasePath(_dataSource.openHelper.databaseName)
        if (dataBase.exists()) {
            _dataSource.openHelper.close()
            val src: FileChannel = FileInputStream(dataBase).channel
            val dst: FileChannel = FileOutputStream(_currentDb).channel
            try {
                dst.transferFrom(src, 0, src.size())
                Toast.makeText(
                    _context,
                    "Database successfully imported.",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (exception: Exception) {
                Timber.e(exception)
                Toast.makeText(_context, "ALERT: Restore Error", Toast.LENGTH_SHORT)
                    .show()
            } finally {
                src.close()
                dst.close()
                _dataSource.openHelper.writableDatabase

            }
        }
    }

    /**
     * Retrieves the identity_hash of the Room Database used to check if it is a valid schema.
     * @return Room identity_hash value.
     */
    private fun getPreferences(): String? {
        val sharedPref = _context.getSharedPreferences(
            _context.getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        return sharedPref.getString("db_identity_hash", null)
    }

    fun checkOpenedFile(file: File?) {
        val idHash = getPreferences()
        if (file != null) {
            if (idHash != null) {
                val hash: String?
                val database: SQLiteDatabase = SQLiteDatabase.openDatabase(
                    file.absoluteFile.toString(), null, SQLiteDatabase.OPEN_READONLY
                )
                val cursor = database.rawQuery("SELECT identity_hash FROM room_master_table", null)
                if (cursor.moveToFirst()) {
                    hash = cursor.getString(0)
                    cursor.close()
                    if (hash != null)
                        if (hash == idHash)
                            fileRestore(file)
                        else
                            Toast.makeText(_context, "Corrupted Database.", Toast.LENGTH_SHORT)
                                .show()
                    else
                        Toast.makeText(_context, "Corrupted Database Header.", Toast.LENGTH_SHORT)
                            .show()
                } else
                    Toast.makeText(_context, "Cannot import SQL File.", Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(_context, "Invalid settings.", Toast.LENGTH_SHORT).show()
        } else
            Toast.makeText(_context, "Invalid SQL File.", Toast.LENGTH_SHORT).show()
    } // Just for debugging, the user will only receive a failed import not reason.
}