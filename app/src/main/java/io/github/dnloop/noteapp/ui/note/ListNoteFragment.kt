package io.github.dnloop.noteapp.ui.note


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.github.dnloop.noteapp.R

/**
 * A simple [Fragment] subclass.
 */
class ListNoteFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_notes, container, false)

        return root
    }


}
