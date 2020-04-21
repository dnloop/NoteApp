package io.github.dnloop.noteapp.ui.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import io.github.dnloop.noteapp.data.NoteDatabase
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.channels.FileChannel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ShareViewModel(application: Application) :  AndroidViewModel(application) {

    private val _context = getApplication<Application>().applicationContext

    private var _granted: Boolean = false

    private lateinit var _currentDb: File

    private lateinit var _dataSource: NoteDatabase

    fun exportLocalDatabase(): Boolean {
        return if (_granted) {
            fileBackup()
            true
        } else {
            false
        }
    }

    fun importLocalDatabase(): Boolean {
        return if (_granted) {
            true
        } else {
            false
        }
    }

    fun setPermission(granted: Boolean) {
        _granted = granted
    }

    fun setDataSource(dataSource: NoteDatabase) {
        _dataSource = dataSource
        _currentDb = _context.getDatabasePath(_dataSource.openHelper.databaseName)
    }

    private fun fileBackup() {
        val dir = _context.getExternalFilesDir("Backup").toString()
        val df: DateFormat = SimpleDateFormat("EEEd-MM-yyyy_HHmmss")
        val date: String = df.format(Calendar.getInstance().time)
        val fileName = "${_currentDb.name}-$date"
        val backup = File(dir,fileName)
        if (_currentDb.exists()) {
            val src: FileChannel = FileInputStream(_currentDb).channel
            val dst: FileChannel = FileOutputStream(backup).channel
            dst.transferFrom(src, 0, src.size())
            src.close()
            dst.close()
            Toast.makeText(_context, "Database Successfully exported to: $dir", Toast.LENGTH_SHORT).show()
        }
    }

    private fun restoreBackup() {

    }
}