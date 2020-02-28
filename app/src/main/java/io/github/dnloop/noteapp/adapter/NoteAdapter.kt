package io.github.dnloop.noteapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.dnloop.noteapp.R

import io.github.dnloop.noteapp.auxiliary.Formatter
import io.github.dnloop.noteapp.auxiliary.Validator
import io.github.dnloop.noteapp.data.Note

class NoteAdapter : ListAdapter<Note, NoteAdapter.ViewHolder>(NoteDiffCallback())  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder private constructor(itemView: View): RecyclerView.ViewHolder(itemView){
        // TODO user image or color code
//        val circularImageView: CircularImageView = itemView.findViewById(R.id.circularImageView)
        private val textTitle: TextView = itemView.findViewById(R.id.text_title)
        private val textBody: TextView = itemView.findViewById(R.id.text_body)
        private val textCategory: TextView = itemView.findViewById(R.id.text_category)
        private val textModifiedAt: TextView = itemView.findViewById(R.id.text_modified_at)

        fun bind(
            item: Note
        ) {
            textTitle.text = item.title
            textBody.text = item.content
            textCategory.text = Validator.isCategoryNull(item.categoryId)
            textModifiedAt.text = Formatter.longToDate(item.modifiedAt)
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_note, parent, false)
                return ViewHolder(view)
            }
        }
    }


}

private class NoteDiffCallback : DiffUtil.ItemCallback<Note>() {

    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem
    }
}

class NoteListener(val clickListener: (noteId: Int) -> Unit) {
    fun onClick(note: Note) = clickListener(note.id)
}