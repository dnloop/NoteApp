package io.github.dnloop.noteapp.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.github.dnloop.noteapp.R
import io.github.dnloop.noteapp.data.Note

class ColorPickerDialogFragment(private val _note: Note) : DialogFragment(), View.OnClickListener {
    internal lateinit var listener: ColorPickerDialogListener

    interface ColorPickerDialogListener {
        fun callbackColor(note: Note)
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater
            val view: View =  inflater.inflate(R.layout.color_picker, null)

            setColorClickListeners(view)
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(view)
                // Add action buttons
                .setNegativeButton(R.string.cancel) { _, _ ->
                    dialog?.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun setColorClickListeners(view: View) {
        val btnRed = view.findViewById<FloatingActionButton>(R.id.btn_red)
        btnRed.setOnClickListener(this)
        val btnYellow = view.findViewById<FloatingActionButton>(R.id.btn_yellow)
        btnYellow.setOnClickListener(this)
        val btnBlue = view.findViewById<FloatingActionButton>(R.id.btn_blue)
        btnBlue.setOnClickListener(this)
        val btnOrange = view.findViewById<FloatingActionButton>(R.id.btn_orange)
        btnOrange.setOnClickListener(this)
        val btnGreen = view.findViewById<FloatingActionButton>(R.id.btn_green)
        btnGreen.setOnClickListener(this)
        val btnViolet = view.findViewById<FloatingActionButton>(R.id.btn_violet)
        btnViolet.setOnClickListener(this)
        val btnOrangeRed = view.findViewById<FloatingActionButton>(R.id.btn_orange_red)
        btnOrangeRed.setOnClickListener(this)
        val btnYellowOrange = view.findViewById<FloatingActionButton>(R.id.btn_yellow_orange)
        btnYellowOrange.setOnClickListener(this)
        val btnYellowGreen = view.findViewById<FloatingActionButton>(R.id.btn_yellow_green)
        btnYellowGreen.setOnClickListener(this)
        val btnBlueGreen = view.findViewById<FloatingActionButton>(R.id.btn_blue_green)
        btnBlueGreen.setOnClickListener(this)
        val btnBlueViolet = view.findViewById<FloatingActionButton>(R.id.btn_blue_violet)
        btnBlueViolet.setOnClickListener(this)
        val btnRedViolet = view.findViewById<FloatingActionButton>(R.id.btn_red_violet)
        btnRedViolet.setOnClickListener(this)
    }

    @Suppress("DEPRECATION")
    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_red -> _note.color = Integer.toHexString(resources.getColor(R.color.red))
            R.id.btn_yellow -> _note.color = Integer.toHexString(resources.getColor(R.color.yellow))
            R.id.btn_blue -> _note.color = Integer.toHexString(resources.getColor(R.color.blue))
            R.id.btn_orange -> _note.color = Integer.toHexString(resources.getColor(R.color.orange))
            R.id.btn_green -> _note.color = Integer.toHexString(resources.getColor(R.color.green))
            R.id.btn_violet -> _note.color = Integer.toHexString(resources.getColor(R.color.violet))
            R.id.btn_orange_red -> _note.color = Integer.toHexString(resources.getColor(R.color.orange_red))
            R.id.btn_yellow_orange -> _note.color = Integer.toHexString(resources.getColor(R.color.yellow_orange))
            R.id.btn_yellow_green -> _note.color = Integer.toHexString(resources.getColor(R.color.yellow_green))
            R.id.btn_blue_green -> _note.color = Integer.toHexString(resources.getColor(R.color.blue_green))
            R.id.btn_blue_violet -> _note.color = Integer.toHexString(resources.getColor(R.color.blue_violet))
            R.id.btn_red_violet -> _note.color = Integer.toHexString(resources.getColor(R.color.red_violet))
        }
        listener.callbackColor(_note)
        this.dialog?.dismiss()
    }
}