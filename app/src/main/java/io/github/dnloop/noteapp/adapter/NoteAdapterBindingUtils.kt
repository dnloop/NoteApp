package io.github.dnloop.noteapp.adapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import io.github.dnloop.noteapp.R
import io.github.dnloop.noteapp.auxiliary.Formatter
import io.github.dnloop.noteapp.auxiliary.Validator
import io.github.dnloop.noteapp.data.Category
import io.github.dnloop.noteapp.data.Note
import io.github.dnloop.noteapp.data.NoteWithCategory
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
fun TextView.setNoteCategory(item: NoteWithCategory?) {
    item?.let {
        text = if(Validator.isCategoryNull(item.note.categoryId))
            resources.getString(R.string.none)
        else
            item.category?.name
    }
}

@BindingAdapter("noteModifiedAt")
fun TextView.setModifiedAt(item: Note?) {
    item?.let { text = Formatter.longToDate(item.modifiedAt) }
}

@BindingAdapter("noteDeletedAt")
fun TextView.setDeletedAt(item: Note?) {
    item?.let { text = Validator.isDeletedAtNull(item.deletedAt) }
}

@BindingAdapter("tagName")
fun TextView.setTagName(item: Tag?) {
    item?.let { text = item.name }
}

@BindingAdapter("categoryName")
fun TextView.setCategoryName(item: Category?) {
    item?.let { text = item.name }
}