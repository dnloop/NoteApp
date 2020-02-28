package io.github.dnloop.noteapp.adapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import io.github.dnloop.noteapp.auxiliary.Formatter
import io.github.dnloop.noteapp.auxiliary.Validator
import io.github.dnloop.noteapp.data.Note

@BindingAdapter("noteTitle")
fun TextView.setNoteTitle(item: Note?) {
    item?.let { text = item.title }
}

@BindingAdapter("noteContent")
fun TextView.setNoteContent(item: Note?) {
    item?.let { text = item.content }
}

@BindingAdapter("noteCategory")
fun TextView.setNoteCategory(item: Note?) {
    item?.let { text = Validator.isCategoryNull(item.categoryId) }
}

@BindingAdapter("noteModifiedAt")
fun TextView.setModifiedAt(item: Note?) {
    item?.let { text = Formatter.longToDate(item.modifiedAt) }
}