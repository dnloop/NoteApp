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
import io.github.dnloop.noteapp.adapter.NoteAdapter
import io.github.dnloop.noteapp.adapter.NoteListener
import io.github.dnloop.noteapp.data.NoteDatabase
import io.github.dnloop.noteapp.databinding.FragmentHomeBinding
import io.github.dnloop.noteapp.ui.viewmodel.NoteViewModel
import io.github.dnloop.noteapp.ui.viewmodel.NoteViewModelFactory

/**
 * A simple [Fragment] subclass.
 */
class FolderFragment : Fragment() {

    private lateinit var arguments: FolderFragmentArgs

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments = FolderFragmentArgs.fromBundle(requireArguments())

        val binding: FragmentHomeBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false
        )

        val application = requireNotNull(this.activity).application

        val dataSource = NoteDatabase.getInstance(application).noteDao

        val viewModelFactory = NoteViewModelFactory(dataSource, application)

        val noteViewModel =
            ViewModelProvider(this, viewModelFactory).get(NoteViewModel::class.java)

        val adapter = NoteAdapter(NoteListener { noteId ->
            noteViewModel.onNoteSelected(noteId)
        })

        binding.noteList.adapter = adapter

        binding.noteViewModel = noteViewModel

        binding.lifecycleOwner = this

        noteViewModel.updateLabel(arguments.title)

        noteViewModel.navigateToEditor.observe(viewLifecycleOwner, Observer { note ->
            note?.let {
                this.findNavController().navigate(
                    FolderFragmentDirections.actionNavFolderToNavContentNote(
                        note, false
                    )
                )
                noteViewModel.onNoteEditorNavigated()
            }
        })

        noteViewModel.loadNotesByCategory(arguments.catId).observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }

}
