package io.github.dnloop.noteapp.auxiliary

import timber.log.Timber
import java.io.File
import java.io.FileReader

class Validator {
    companion object {
        fun isCategoryNull(item: Long?): Boolean { return item == null }

        fun isDeletedAtNull(item: Long?) = item?.let { Formatter.longToDate(it) } ?: ""

        fun isValidSQLite(dbPath: String): Boolean {
            val file = File(dbPath)
            return if (!file.exists() || !file.canRead()) {
                Timber.i("does not exist or cannot be read")
                false
            } else try {
                val fr = FileReader(file)
                val buffer = CharArray(16)
                fr.read(buffer, 0, 16)
                val str = String(buffer)
                fr.close()
                str == "SQLite format 3\u0000"
            } catch (e: Exception) {
                Timber.i("Exception")
                e.printStackTrace()
                false
            }
        }
    }
}
