package io.github.dnloop.noteapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.dnloop.noteapp.data.Tag
import io.github.dnloop.noteapp.databinding.ListItemTagBinding

class TagAdapter(val clickListener: TagListener): ListAdapter<Tag, TagAdapter.ViewHolder>(TagDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    class ViewHolder private constructor(val binding: ListItemTagBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(
            item: Tag,
            clickListener: TagListener
        ) {
            binding.tag = item
            binding.clickListener = clickListener
            binding.btnDelete.setOnClickListener { clickListener.setOnDeleteClickListener {
                clickListener.listener?.invoke(item)
            } }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemTagBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

private class TagDiffCallback : DiffUtil.ItemCallback<Tag>() {

    override fun areItemsTheSame(oldItem: Tag, newItem: Tag): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Tag, newItem: Tag): Boolean {
        return oldItem == newItem
    }
}

class TagListener(val clickListener: (tagId: Tag) -> Unit) {
    var listener: ((item: Tag) -> Unit)? = null

    fun onClick(tag: Tag) = clickListener(tag)

    fun setOnDeleteClickListener(listener: (item: Tag) -> Unit) {
        this.listener = listener
    }
}