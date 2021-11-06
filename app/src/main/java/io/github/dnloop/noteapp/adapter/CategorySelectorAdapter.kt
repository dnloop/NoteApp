package io.github.dnloop.noteapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.dnloop.noteapp.data.Category
import io.github.dnloop.noteapp.databinding.ListItemCategorySimpleBinding

class CategoryDialogAdapter(val clickListener: CategorySelectorListListener) : ListAdapter<Category, CategoryDialogAdapter.ViewHolder>(CategoryDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    class ViewHolder private constructor(val binding: ListItemCategorySimpleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: Category,
            clickListener: CategorySelectorListListener
        ) {
            binding.category = item
            binding.counterBadge.text = number.toString()
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemCategorySimpleBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
            var number: Long = 0L
        }

    }
    fun setBadgeCounter(number: Long) {
        ViewHolder.number = number
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

class CategorySelectorListListener(val clickListener: (catId: Category) -> Unit) {
    fun onClick(catId: Category) = clickListener(catId)
}
