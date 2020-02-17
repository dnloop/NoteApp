package io.github.dnloop.noteapp.ui.note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import io.github.dnloop.noteapp.R

class AddNoteFragment : Fragment() {

    private lateinit var addNoteViewModel: AddNoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addNoteViewModel =
                ViewModelProviders.of(this).get(AddNoteViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_note_editor, container, false)
        // TODO add logic
        return root
    }
}