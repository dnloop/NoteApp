package io.github.dnloop.noteapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mikhaellopez.circularimageview.CircularImageView
import io.github.dnloop.noteapp.R

import io.github.dnloop.noteapp.auxiliary.Formatter
import io.github.dnloop.noteapp.auxiliary.Validator
import io.github.dnloop.noteapp.data.Note

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.ViewHolder>()  {

    var data= listOf<Note>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_note, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        val res = holder.itemView.resources
//        holder.circularImageView.apply { circleColor = Color.GREEN }
        holder.textTitle.text = item.title
        holder.textBody.text = item.content
        holder.textCategory.text = Validator.isCategoryNull(item.categoryId)
        holder.textModifiedAt.text = Formatter.longToDate(item.modifiedAt)
    }



    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val circularImageView: CircularImageView = itemView.findViewById(R.id.circularImageView)
        val textTitle: TextView = itemView.findViewById(R.id.text_title)
        val textBody: TextView = itemView.findViewById(R.id.text_body)
        val textCategory: TextView = itemView.findViewById(R.id.text_category)
        val textModifiedAt: TextView = itemView.findViewById(R.id.text_modified_at)
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