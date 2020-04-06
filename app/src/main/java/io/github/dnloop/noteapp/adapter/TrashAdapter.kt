package io.github.dnloop.noteapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.dnloop.noteapp.data.Note
import io.github.dnloop.noteapp.databinding.ListItemTrashBinding

class TrashAdapter(val clickListener: TrashListener): ListAdapter<Note, TrashAdapter.ViewHolder>(TrashDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    class ViewHolder private constructor(val binding: ListItemTrashBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(
            item: Note,
            clickListener: TrashListener
        ) {
            binding.note = item
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

    private class TrashDiffCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }

}


class TrashListener(val clickListener: (catId: Note) -> Unit) {
    var listener: ((item: Note) -> Unit)? = null

    fun onClick(cat: Note) = clickListener(cat)

    fun setOnDeleteClickListener(listener: (item: Note) -> Unit) {
        this.listener = listener
    }

}