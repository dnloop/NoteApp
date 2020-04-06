package io.github.dnloop.noteapp.auxiliary

class Validator {
    companion object {
        fun isCategoryNull(item: Long?) = item?.toString() ?: ""
        fun isDeletedAtNull(item: Long?) = item?.let { Formatter.longToDate(it) } ?: ""
    }
}
