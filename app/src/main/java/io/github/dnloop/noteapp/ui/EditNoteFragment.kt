package io.github.dnloop.noteapp.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import io.github.dnloop.noteapp.R
import io.github.dnloop.noteapp.data.Note
import io.github.dnloop.noteapp.data.NoteDatabase
import io.github.dnloop.noteapp.databinding.FragmentNoteEditorBinding
import io.github.dnloop.noteapp.ui.viewmodel.EditNoteViewModel
import io.github.dnloop.noteapp.ui.viewmodel.EditNoteViewModelFactory
import timber.log.Timber

class EditNoteFragment(val _noteId: Long) : Fragment() {

    private var wasEdited: Boolean = false

    private var note: Note = Note()

    private lateinit var editNoteViewModel: EditNoteViewModel

    private lateinit var binding: FragmentNoteEditorBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_note_editor, container, false)

        editNoteViewModel = init()

        binding.setBinding()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if(wasEdited)
            checkNoteId()
    }

    private fun FragmentNoteEditorBinding.setBinding() {
        editNoteViewModel = this@EditNoteFragment.editNoteViewModel

        lifecycleOwner = this@EditNoteFragment

        inputTitle.afterTextChanged {}

        inputContent.afterTextChanged {}
    }

    private fun init(): EditNoteViewModel {
        val application = requireNotNull(this.activity).application

        val dataSource = NoteDatabase.getInstance(application).noteDao

        val viewModelFactory = EditNoteViewModelFactory(_noteId, dataSource)

        return ViewModelProvider(this, viewModelFactory).get(EditNoteViewModel::class.java)
    }

    private fun checkNoteId() {
        note.title = binding.inputTitle.text.toString()
        note.content = binding.inputContent.text.toString()
        Timber.i("Note fields loaded: ${note.title}")
        if (_noteId > 0) {
            editNoteViewModel.onUpdate(note)
            Timber.i("Updated note: '${note.title}'")
        } else {
            editNoteViewModel.onInsert(note)
            Timber.i("Note '${note.title}' Inserted")
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
