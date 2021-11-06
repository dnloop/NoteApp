package io.github.dnloop.noteapp.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import io.github.dnloop.noteapp.R
import io.github.dnloop.noteapp.data.Tag


/**
 * A simple [Fragment] subclass.
 */
class TagDialogFragment(private val _tag: Tag) : DialogFragment() {

    internal lateinit var listener: TagDialogListener

    /**
     * The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it.
     * */
    interface TagDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment, tag: Tag)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater
            val view: View = inflater.inflate(R.layout.dialog_tag_editor, null)
            val editText = view.findViewById<EditText>(R.id.editText)
            editText.setText(_tag.name)
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.confirm) { _, _ ->
                    _tag.name = editText.text.toString()
                    listener.onDialogPositiveClick(this, _tag)
                }
                .setNegativeButton(R.string.cancel) { _, _ ->
                    dialog?.cancel()
                    listener.onDialogNegativeClick(this)
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}