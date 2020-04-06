package io.github.dnloop.noteapp.adapter

import io.github.dnloop.noteapp.data.Note

class TrashAdapter {
}


class TrashListener(val clickListener: (catId: Note) -> Unit) {
    var listener: ((item: Note) -> Unit)? = null

    fun onClick(cat: Note) = clickListener(cat)

    fun setOnDeleteClickListener(listener: (item: Note) -> Unit) {
        this.listener = listener
    }

}