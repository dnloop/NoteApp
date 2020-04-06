package io.github.dnloop.noteapp.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import io.github.dnloop.noteapp.R

class GenericDialogFragment(private val message: String) : DialogFragment() {
    internal lateinit var listener: GenericDialogListener

    interface GenericDialogListener {
        fun onDialogPositiveClick()
        fun onDialogNegativeClick()
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(message)
                // Add action buttons
                .setPositiveButton(R.string.yes) { _, _ ->
                    listener.onDialogPositiveClick()
                }
                .setNegativeButton(R.string.no) { _, _ ->
                    dialog?.cancel()
                    listener.onDialogNegativeClick()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}