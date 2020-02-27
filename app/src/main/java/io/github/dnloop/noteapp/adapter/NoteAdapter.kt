package io.github.dnloop.noteapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.dnloop.noteapp.R
import io.github.dnloop.noteapp.data.Note

class NoteAdapter : RecyclerView.Adapter<TextItemViewHolder>()  {

    var data= listOf<Note>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.text_item_view, parent, false) as TextView
        return TextItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {
        val item = data[position]
        holder.textView.text = item.title
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

/**
 * ViewHolder that holds a single [TextView].
 * Needs optimizations, future removal
 */

class TextItemViewHolder(val textView: TextView): RecyclerView.ViewHolder(textView)