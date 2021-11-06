package io.github.dnloop.noteapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.dnloop.noteapp.data.NoteWithCategory
import io.github.dnloop.noteapp.databinding.ListItemTrashBinding

class TrashAdapter(val clickListener: TrashListener): ListAdapter<NoteWithCategory, TrashAdapter.ViewHolder>(TrashDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    class ViewHolder private constructor(val binding: ListItemTrashBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(
            item: NoteWithCategory,
            clickListener: TrashListener
        ) {
            binding.item = item
            binding.clickListener = clickListener
            binding.btnHardDelete.setOnClickListener {
                clickListener.listener?.invoke(item)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemTrashBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    private class TrashDiffCallback : DiffUtil.ItemCallback<NoteWithCategory>() {
        override fun areItemsTheSame(oldItem: NoteWithCategory, newItem: NoteWithCategory): Boolean {
            return oldItem.note.id == newItem.note.id
        }

        override fun areContentsTheSame(oldItem: NoteWithCategory, newItem: NoteWithCategory): Boolean {
            return oldItem.note == newItem.note
        }
    }

}


class TrashListener(val clickListener: (catId: NoteWithCategory) -> Unit) {
    var listener: ((item: NoteWithCategory) -> Unit)? = null

    fun onClick(cat: NoteWithCategory) = clickListener(cat)

    fun setOnDeleteClickListener(listener: (item: NoteWithCategory) -> Unit) {
        this.listener = listener
    }

}