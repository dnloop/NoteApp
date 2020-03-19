package io.github.dnloop.noteapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.dnloop.noteapp.data.Category
import io.github.dnloop.noteapp.databinding.ListItemCategoryBinding

class CategoryAdapter(val clickListener: CategoryListener): ListAdapter<Category, CategoryAdapter.ViewHolder>(CategoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    class ViewHolder private constructor(val binding: ListItemCategoryBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(
            item: Category,
            clickListener: CategoryListener
        ) {
            binding.category = item
            binding.clickListener = clickListener
            binding.btnDelete.setOnClickListener {
                clickListener.listener?.invoke(item)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemCategoryBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    private class CategoryDiffCallback : DiffUtil.ItemCallback<Category>() {

        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }
}

class CategoryListener(val clickListener: (catId: Category) -> Unit) {
    var listener: ((item: Category) -> Unit)? = null

    fun onClick(cat: Category) = clickListener(cat)

    fun setOnDeleteClickListener(listener: (item: Category) -> Unit) {
        this.listener = listener
    }

}
