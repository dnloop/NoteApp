package io.github.dnloop.noteapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import io.github.dnloop.noteapp.R
import io.github.dnloop.noteapp.data.NoteDatabase
import io.github.dnloop.noteapp.databinding.FragmentNoteEditorBinding
import io.github.dnloop.noteapp.ui.viewmodel.EditNoteViewModel
import io.github.dnloop.noteapp.ui.viewmodel.EditNoteViewModelFactory

class EditNoteFragment(val _noteId: Long) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentNoteEditorBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_note_editor, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = NoteDatabase.getInstance(application).noteDao

        val viewModelFactory = EditNoteViewModelFactory(_noteId, dataSource)

        val editNoteViewModel =
            ViewModelProvider(this, viewModelFactory).get(EditNoteViewModel::class.java)

        binding.editNoteViewModel = editNoteViewModel

        binding.lifecycleOwner = this

        editNoteViewModel.navigateToHome.observe(viewLifecycleOwner, Observer {
            if( it == true) {
                this.findNavController().navigate(ContentNoteFragmentDirections.actionNavContentNoteToNavHome())
                editNoteViewModel.doneNavigating()
            }
        })

        return binding.root
    }


}
