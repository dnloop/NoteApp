package io.github.dnloop.noteapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import io.github.dnloop.noteapp.MainActivity
import io.github.dnloop.noteapp.R
import io.github.dnloop.noteapp.adapter.TagAdapter
import io.github.dnloop.noteapp.adapter.TagListener
import io.github.dnloop.noteapp.data.NoteDatabase
import io.github.dnloop.noteapp.data.Tag
import io.github.dnloop.noteapp.databinding.FragmentTagEditorBinding
import io.github.dnloop.noteapp.ui.TagDialogFragment.TagDialogListener
import io.github.dnloop.noteapp.ui.viewmodel.TagEditorViewModel
import io.github.dnloop.noteapp.ui.viewmodel.TagEditorViewModelFactory
import timber.log.Timber


class TagEditorFragment : Fragment(), TagDialogListener {
    private lateinit var binding: FragmentTagEditorBinding

    private lateinit var tagEditorViewModel: TagEditorViewModel

    private var _tag: Tag = Tag()


    private fun showTagDialog(tag: Tag) {
        val dialog = TagDialogFragment(tag)
        dialog.listener = this
        dialog.show(childFragmentManager, "TagDialogFragment")
    }

    override fun onDialogPositiveClick(dialog: DialogFragment, tag: Tag) {
        tagEditorViewModel.onUpdate(tag)
        Toast.makeText(context, "Tag edited", Toast.LENGTH_SHORT).show()
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tagEditorViewModel = init()

        val adapter = TagAdapter(TagListener {
            tagId -> tagEditorViewModel.onTagSelected(tagId)
        })

        adapter.clickListener.setOnDeleteClickListener {tag ->
            tag.let {
                tagEditorViewModel.onDelete(tag)
                Toast.makeText(context, "Tag deleted: ${it.name}", Toast.LENGTH_SHORT).show()
            }
        }

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_tag_editor, container, false
        )

        binding.setBinding(adapter)

        tagEditorViewModel.tagDataSource.getAllTags().observe( viewLifecycleOwner, Observer{
            it?.let {
                adapter.submitList(it)
            }
        })

        tagEditorViewModel.openDialogEditor.observe(viewLifecycleOwner, Observer {tag ->
            tag?.let {
                showTagDialog(it)
            }
        })

        return binding.root
    }

    private fun checkTag() {
        _tag.name = binding.tagInput.text.toString()
        if (_tag.name.isNotBlank()) {
            tagEditorViewModel.onInsert(_tag)
            Timber.i("Tag '${_tag.name}' Inserted")
        }
    }

    private fun FragmentTagEditorBinding.setBinding(adapter: TagAdapter) {
        (activity as MainActivity?)?.getFloatingActionButton()?.hide()

        binding.tagList.adapter = adapter

        binding.tagEditorViewModel = this@TagEditorFragment.tagEditorViewModel

        binding.lifecycleOwner = this@TagEditorFragment

        btnAdd.setOnClickListener {
           checkTag()
        }
    }

    private fun init(): TagEditorViewModel {
        val application = requireNotNull(this.activity).application

        val dataSource = NoteDatabase.getInstance(application).tagDao

        val viewModelFactory = TagEditorViewModelFactory(dataSource, application)

        return ViewModelProvider(this, viewModelFactory).get(TagEditorViewModel::class.java)
    }

    override fun onDetach() {
        super.onDetach()
        (activity as MainActivity?)?.getFloatingActionButton()?.show()
    }
}
