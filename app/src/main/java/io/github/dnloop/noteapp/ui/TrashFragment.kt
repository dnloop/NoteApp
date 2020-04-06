package io.github.dnloop.noteapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import io.github.dnloop.noteapp.MainActivity
import io.github.dnloop.noteapp.R
import io.github.dnloop.noteapp.adapter.TrashAdapter
import io.github.dnloop.noteapp.adapter.TrashListener
import io.github.dnloop.noteapp.data.Note
import io.github.dnloop.noteapp.data.NoteDatabase
import io.github.dnloop.noteapp.databinding.FragmentTrashBinding
import io.github.dnloop.noteapp.ui.viewmodel.TrashViewModel
import io.github.dnloop.noteapp.ui.viewmodel.TrashViewModelFactory
import kotlinx.android.synthetic.main.list_item_trash.*

class TrashFragment : Fragment(), GenericDialogFragment.GenericDialogListener {

    private lateinit var binding: FragmentTrashBinding

    private lateinit var trashViewModel: TrashViewModel

    private var _note: Note = Note()

    private fun showConfirmDialog(note: Note) {
        val dialog = GenericDialogFragment(note, R.string.confirm_hard_delete)
        dialog.listener = this
        dialog.show(childFragmentManager, "GenericDialogFragment")
    }

    override fun onDialogPositiveClick(note: Note) {
        trashViewModel.onDelete(note)
        Toast.makeText(context, "Note permanently deleted: ${note.title}", Toast.LENGTH_SHORT).show()
    }

    override fun onDialogNegativeClick() {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        trashViewModel = init()

        val adapter = TrashAdapter(TrashListener {
            noteId -> trashViewModel.onNoteSelected(noteId)
        })

        adapter.clickListener.setOnDeleteClickListener { note ->
            showConfirmDialog(note)
        }
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_trash, container, false
        )

        binding.setBinding(adapter)

        trashViewModel.loadDeletedNotes().observe(viewLifecycleOwner, Observer { noteList ->
            noteList?.let {
                adapter.submitList(noteList)
            }
        })

        trashViewModel.openConfirmDialog.observe(viewLifecycleOwner, Observer {note ->
            note?.let {
                trashViewModel.onRestore(it)
                Toast.makeText(context, "Note restored: ${it.title}", Toast.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }

    private fun FragmentTrashBinding.setBinding(adapter: TrashAdapter) {
        (activity as MainActivity?)?.getFloatingActionButton()?.hide()

        trashList.adapter = adapter

        trashViewModel = this@TrashFragment.trashViewModel

        lifecycleOwner = this@TrashFragment

    }

    private fun init(): TrashViewModel {
        val application = requireNotNull(this.activity).application

        val dataSource = NoteDatabase.getInstance(application).noteDao

        val viewModelFactory = TrashViewModelFactory(dataSource, application)

        return ViewModelProvider(this, viewModelFactory).get(TrashViewModel::class.java)
    }

}
