package io.github.dnloop.noteapp.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import io.github.dnloop.noteapp.R
import io.github.dnloop.noteapp.data.Category
import io.github.dnloop.noteapp.data.Tag

class CategoryDialogFragment(private val _category: Category) : DialogFragment() {
    internal lateinit var listener: CategoryDialogListener

    /**
     * The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it.
     * */
    interface CategoryDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment, category: Category)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater
            val view: View = inflater.inflate(R.layout.dialog_category_editor, null)
            val editText = view.findViewById<EditText>(R.id.editText)
            editText.setText(_category.name)
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.confirm) { _, _ ->
                    _category.name = editText.text.toString()
                    listener.onDialogPositiveClick(this, _category)
                }
                .setNegativeButton(R.string.cancel) { _, _ ->
                    dialog?.cancel()
                    listener.onDialogNegativeClick(this)
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}