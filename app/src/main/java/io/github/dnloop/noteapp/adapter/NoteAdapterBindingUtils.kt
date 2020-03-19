package io.github.dnloop.noteapp.adapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import io.github.dnloop.noteapp.auxiliary.Formatter
import io.github.dnloop.noteapp.auxiliary.Validator
import io.github.dnloop.noteapp.data.Category
import io.github.dnloop.noteapp.data.Note
import io.github.dnloop.noteapp.data.Tag

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

@BindingAdapter("tagName")
fun TextView.setTagName(item: Tag?) {
    item?.let { text = item.name }
}

@BindingAdapter("categoryName")
fun TextView.setCategoryName(item: Category?) {
    item?.let { text = item.name }
}