package io.github.dnloop.noteapp.auxiliary

class Validator {
    companion object {
        fun isCategoryNull(item: Int?) = item?.toString() ?: ""
    }
}
