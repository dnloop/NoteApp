package io.github.dnloop.noteapp.ui.note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.github.dnloop.noteapp.R
import io.github.dnloop.noteapp.data.NoteDatabase
import io.github.dnloop.noteapp.databinding.FragmentNoteEditorBinding
import io.github.dnloop.noteapp.ui.viewmodel.NoteViewModel
import io.github.dnloop.noteapp.ui.viewmodel.NoteViewModelFactory

class EditNoteFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentNoteEditorBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_note_editor, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = NoteDatabase.getInstance(application).noteDao

        val viewModelFactory = NoteViewModelFactory(dataSource, application)

        val noteViewModel =
            ViewModelProvider(this, viewModelFactory).get(NoteViewModel::class.java)

        return binding.root
    }
}
