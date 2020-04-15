package io.github.dnloop.noteapp.auxiliary

class Validator {
    companion object {
        fun isCategoryNull(item: Long?): Boolean { return item == null }
        fun isDeletedAtNull(item: Long?) = item?.let { Formatter.longToDate(it) } ?: ""
    }
}
