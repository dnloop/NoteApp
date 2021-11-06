package io.github.dnloop.noteapp.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import io.github.dnloop.noteapp.R
import io.github.dnloop.noteapp.data.Note

class GenericDialogFragment(private val _note: Note, private val _message: Int) : DialogFragment() {
    internal lateinit var listener: GenericDialogListener

    interface GenericDialogListener {
        fun onDialogPositiveClick(note: Note)
        fun onDialogNegativeClick()
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(_message)
                // Add action buttons
                .setPositiveButton(R.string.yes) { _, _ ->
                    listener.onDialogPositiveClick(_note)
                }
                .setNegativeButton(R.string.no) { _, _ ->
                    dialog?.cancel()
                    listener.onDialogNegativeClick()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}