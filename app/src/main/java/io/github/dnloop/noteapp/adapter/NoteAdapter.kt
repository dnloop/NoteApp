package io.github.dnloop.noteapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.dnloop.noteapp.data.NoteWithCategory
import io.github.dnloop.noteapp.databinding.ListItemNoteBinding

class NoteAdapter(val clickListener: NoteListener) : ListAdapter<NoteWithCategory, NoteAdapter.ViewHolder>(NoteDiffCallback())  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    class ViewHolder private constructor(val binding: ListItemNoteBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(
            item: NoteWithCategory,
            clickListener: NoteListener
        ) {
            binding.item = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemNoteBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }


}

private class NoteDiffCallback : DiffUtil.ItemCallback<NoteWithCategory>() {

    override fun areItemsTheSame(oldItem: NoteWithCategory, newItem: NoteWithCategory): Boolean {
        return oldItem.note.id == newItem.note.id
    }

    override fun areContentsTheSame(oldItem: NoteWithCategory, newItem: NoteWithCategory): Boolean {
        return oldItem.note == newItem.note
    }
}

class NoteListener(val clickListener: (noteId: Long) -> Unit) {
    fun onClick(item: NoteWithCategory) = clickListener(item.note.id)
}