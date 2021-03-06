package io.github.dnloop.noteapp.ui

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import io.github.dnloop.noteapp.R
import io.github.dnloop.noteapp.data.Category
import io.github.dnloop.noteapp.data.Note
import io.github.dnloop.noteapp.data.NoteDatabase
import io.github.dnloop.noteapp.data.NoteWithCategory
import io.github.dnloop.noteapp.databinding.FragmentNoteEditorBinding
import io.github.dnloop.noteapp.ui.viewmodel.EditNoteViewModel
import io.github.dnloop.noteapp.ui.viewmodel.EditNoteViewModelFactory

class EditNoteFragment(private var _noteId: Long, private val _archived: Boolean) : Fragment(),
    CategorySelectorFragment.CategorySelectorListener, GenericDialogFragment.GenericDialogListener, ColorPickerDialogFragment.ColorPickerDialogListener {

    private var wasEdited: Boolean = false

    private var _noteCategory: NoteWithCategory = NoteWithCategory()

    private lateinit var editNoteViewModel: EditNoteViewModel

    private lateinit var binding: FragmentNoteEditorBinding

    override fun onDialogPositiveClick(dialog: DialogFragment, category: Category) {
        _noteCategory.category = category
        editNoteViewModel.onUpdateWithCategory(_noteCategory)
        Toast.makeText(activity, "Category added.", Toast.LENGTH_SHORT).show()
    } // Category Selector Dialog

    override fun onDialogNeutralClick(
        dialog: DialogFragment,
        updated: Boolean
    ) {
        if (updated) {
            _noteCategory.note.categoryId = null
            editNoteViewModel.onUpdate(_noteCategory.note)
            Toast.makeText(activity, "Category Removed.", Toast.LENGTH_SHORT).show()
        }
    } // Category Selector Dialog

    override fun onDialogPositiveClick(note: Note) {
        note.deleted = true
        editNoteViewModel.onUpdate(note)
        Toast.makeText(activity, "Note Deleted.", Toast.LENGTH_SHORT).show()
        findNavController().navigateUp()
    } // Generic Dialog

    override fun callbackColor(note: Note) {
        _noteCategory.note = note
        editNoteViewModel.onUpdateWithCategory(_noteCategory)
        binding.fabColorPicker.setColorFilter(Color.parseColor("#"+note.color))
        Toast.makeText(activity, "Color added.", Toast.LENGTH_SHORT).show()
    }

    override fun onDialogNegativeClick() {} // Generic Dialog

    override fun onDialogNegativeClick(dialog: DialogFragment) {} // Category Selector Dialog

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
            it?.let { note ->
                _noteCategory.note = note
                note.color?.let { color ->
                    binding.fabColorPicker.setColorFilter(Color.parseColor("#$color"))
                }
            }
        })

        binding.fabColorPicker.setOnClickListener {
            showColorPicker(_noteCategory)
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.share_menu, menu)
        // check if the activity resolves
        if (null == getShareIntent().resolveActivity(requireActivity().packageManager)) {
            // hide the menu item if it doesn't resolve
            menu.findItem(R.id.item_share)?.setVisible(false)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_share -> shareNote()
        }
        return super.onOptionsItemSelected(item)
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

        if (_archived) {
            bottomNavigationView.menu.findItem(R.id.item_archive).isVisible = false
            bottomNavigationView.menu.findItem(R.id.item_unarchive).isVisible = true
        }

        bottomNavigationView.setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.item_category -> showCategoryList(_noteCategory)
                R.id.item_archive -> onArchive(_noteCategory.note)
                R.id.item_unarchive -> onUnarchive(_noteCategory.note)
                R.id.item_trash -> onDeleteNote(_noteCategory.note)
                R.id.item_save -> onSave()
            }
        }
    }

    private fun onSave() {
        if (checkNoteId()) {
            wasEdited = false
        } else
            Toast.makeText(activity, "Cannot save an empty note", Toast.LENGTH_SHORT).show()
    }

    private fun onDeleteNote(note: Note) {
        if (_noteId > 0) {
            val dialog = GenericDialogFragment(note, R.string.confirm_delete)
            dialog.listener = this
            dialog.show(childFragmentManager, "GenericDialogFragment")
        } else
            Toast.makeText(activity, "Note must be saved first.", Toast.LENGTH_SHORT).show()
    }

    private fun onArchive(note: Note) {
        if (_noteId > 0) {
            note.archived = true
            editNoteViewModel.onUpdate(note)
            Toast.makeText(activity, "Note Archived.", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        } else
            Toast.makeText(activity, "Note must be saved first.", Toast.LENGTH_SHORT).show()
    }

    private fun onUnarchive(note: Note) {
        note.archived = false
        editNoteViewModel.onUpdate(note)
        Toast.makeText(activity, "Note Unarchived.", Toast.LENGTH_SHORT).show()
        findNavController().navigateUp()
    }

    private fun showCategoryList(noteCategory: NoteWithCategory) {
        if (_noteId > 0) {
            if (noteCategory.note.categoryId == null) {
                noteCategory.category = Category()
                noteCategory.category!!.id = -1L
            }
            val dialog = CategorySelectorFragment(noteCategory.note.categoryId)
            dialog.listener = this
            dialog.show(childFragmentManager, "CategoryDialogFragment")
        } else
            Toast.makeText(activity, "Note must be saved first.", Toast.LENGTH_SHORT).show()
    }

    private fun showColorPicker(noteCategory: NoteWithCategory) {
        if (_noteId > 0) {
            val dialog = ColorPickerDialogFragment(noteCategory.note)
            dialog.listener = this
            dialog.show(childFragmentManager, "ColorPickerDialogFragment")
        } else
            Toast.makeText(activity, "Note must be saved first.", Toast.LENGTH_SHORT).show()
    }

    private fun init(): EditNoteViewModel {
        val application = requireNotNull(this.activity).application

        val dataSourceNote = NoteDatabase.getInstance(application).noteDao

        val viewModelFactory = EditNoteViewModelFactory(_noteId, dataSourceNote)

        return ViewModelProvider(this, viewModelFactory).get(EditNoteViewModel::class.java)
    }

    private fun checkNoteId(): Boolean {
        var check = 0
        _noteCategory.note.title = binding.inputTitle.text.toString()
        _noteCategory.note.content = binding.inputContent.text.toString()
        if (binding.inputTitle.text.isNullOrBlank())
            check += 1
        if (binding.inputContent.text.isNullOrBlank())
            check += 1

        return if (check < 2) {
            if (_noteId > 0) {
                editNoteViewModel.onUpdate(_noteCategory.note)
                Toast.makeText(activity, "Note updated.", Toast.LENGTH_SHORT).show()
            } else {
                _noteId = editNoteViewModel.onInsert(_noteCategory.note)
                editNoteViewModel.onGetLatest()?.let { _noteCategory.note = it }
                Toast.makeText(activity, "Note created.", Toast.LENGTH_SHORT).show()
            }
            true
        } else
            false
    }

    private fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
                if (!editable.isNullOrBlank() && hasFocus())
                    wasEdited = true
            }
        })
    }

    private fun getShareIntent(): Intent {
        val stringBuilder: StringBuilder = StringBuilder()
        val shareIntent = Intent(Intent.ACTION_SEND)

        stringBuilder.append(_noteCategory.note.title)
        stringBuilder.append("\n\n")
        stringBuilder.append(_noteCategory.note.content)

        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, stringBuilder.toString())

        return shareIntent
    }

    private fun shareNote() {
        if (_noteId > 0) {
            startActivity(getShareIntent())
        } else {
            Toast.makeText(activity, "Note must be saved", Toast.LENGTH_SHORT).show()
        }
    }
}
