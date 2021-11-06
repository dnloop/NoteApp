package io.github.dnloop.noteapp.auxiliary

import java.text.SimpleDateFormat
import java.util.*

class Formatter {
    companion object{
        fun longToDate(long: Long): CharSequence? {
            return SimpleDateFormat("MMM-dd-yyyy' | 'HH:mm:ss", Locale.getDefault())
                .format(long).toString()
        }
    }
}