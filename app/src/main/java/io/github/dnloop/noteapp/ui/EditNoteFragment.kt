package io.github.dnloop.noteapp.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import io.github.dnloop.noteapp.R
import io.github.dnloop.noteapp.data.Category
import io.github.dnloop.noteapp.data.Note
import io.github.dnloop.noteapp.data.NoteDatabase
import io.github.dnloop.noteapp.data.NoteWithCategory
import io.github.dnloop.noteapp.databinding.FragmentNoteEditorBinding
import io.github.dnloop.noteapp.ui.viewmodel.EditNoteViewModel
import io.github.dnloop.noteapp.ui.viewmodel.EditNoteViewModelFactory

class EditNoteFragment(val _noteId: Long) : Fragment(),
    CategorySelectorFragment.CategorySelectorListener {

    private var wasEdited: Boolean = false

    private var _noteCategory: NoteWithCategory = NoteWithCategory()

    private lateinit var editNoteViewModel: EditNoteViewModel

    private lateinit var binding: FragmentNoteEditorBinding

    override fun onDialogPositiveClick(dialog: DialogFragment, category: Category) {
        _noteCategory.category = category
        editNoteViewModel.onUpdateWithCategory(_noteCategory)
    }

    override fun onDialogNeutralClick(
        dialog: DialogFragment,
        updated: Boolean
    ) {
        if (updated) {
            _noteCategory.note.categoryId = null
            editNoteViewModel.onUpdate(_noteCategory.note)
        }
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_note_editor, container, false
        )

        editNoteViewModel = init()

        binding.setBinding()

        editNoteViewModel.getNote().observe(viewLifecycleOwner, Observer {
             it?.let {note ->
                 _noteCategory.note = note
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (wasEdited)
            checkNoteId()
    }

    private fun FragmentNoteEditorBinding.setBinding() {
        editNoteViewModel = this@EditNoteFragment.editNoteViewModel

        lifecycleOwner = this@EditNoteFragment

        inputTitle.afterTextChanged {}

        inputContent.afterTextChanged {}

        bottomNavigationView.setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.item_category -> showCategoryList(_noteCategory)
                R.id.item_archive -> showArchive(_noteCategory.note)
                R.id.item_trash -> deleteNote(_noteCategory.note)
            }
        }
    }

    private fun deleteNote(note: Note) {
        // TODO implement soft delete
    }

    private fun showArchive(note: Note) {
        // TODO implement send to archive
    }

    private fun showCategoryList(noteCategory: NoteWithCategory) {
        if (noteCategory.note.categoryId == null)
            noteCategory.category.id = -1L
        val dialog = CategorySelectorFragment(noteCategory.note.categoryId)
        dialog.listener = this
        dialog.show(childFragmentManager, "CategoryDialogFragment")
    }

    private fun init(): EditNoteViewModel {
        val application = requireNotNull(this.activity).application

        val dataSourceNote = NoteDatabase.getInstance(application).noteDao

        val viewModelFactory = EditNoteViewModelFactory(_noteId, dataSourceNote)

        return ViewModelProvider(this, viewModelFactory).get(EditNoteViewModel::class.java)
    }

    private fun checkNoteId() {
        _noteCategory.note.title = binding.inputTitle.text.toString()
        _noteCategory.note.content = binding.inputContent.text.toString()
        if (_noteId > 0) {
            editNoteViewModel.onUpdate(_noteCategory.note)
            Toast.makeText(activity, "Note updated.", Toast.LENGTH_SHORT).show()
        } else {
            editNoteViewModel.onInsert(_noteCategory.note)
            Toast.makeText(activity, "Note created.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
                if (!editable.isNullOrBlank() && hasFocus())
                    wasEdited = true
            }
        })
    }
}
