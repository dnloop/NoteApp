package io.github.dnloop.noteapp.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.iterator
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import io.github.dnloop.noteapp.R
import io.github.dnloop.noteapp.data.NoteDatabase
import io.github.dnloop.noteapp.data.Tag
import io.github.dnloop.noteapp.databinding.ChipActionBinding
import io.github.dnloop.noteapp.databinding.ChipItemBinding
import io.github.dnloop.noteapp.databinding.FragmentContentTagBinding
import io.github.dnloop.noteapp.ui.viewmodel.TagViewModel
import io.github.dnloop.noteapp.ui.viewmodel.TagViewModelFactory


/**
 * A simple [Fragment] subclass.
 */
class TagFragment(private val _noteId: Long) : Fragment() {

    private lateinit var binding: FragmentContentTagBinding

    private lateinit var chipBinding: ChipItemBinding

    private lateinit var chipActionBinding: ChipActionBinding

    private lateinit var tagViewModel: TagViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_content_tag, container, false
        )

        chipBinding = DataBindingUtil.inflate(
            inflater, R.layout.chip_item, binding.selectedChipGroup, false
        ) // used by the attached tags

        chipActionBinding = DataBindingUtil.inflate(
            inflater, R.layout.chip_action, binding.selectedChipGroup, false
        ) // used by the list of possible tags

        tagViewModel = init()

        tagViewModel.noteDataSource.getNoteWithTags(_noteId).observe(viewLifecycleOwner, Observer {
            it?.let {
                it.tags.forEach { tag ->
                    addChip(tag, true)
                }
            }
        })

        tagViewModel.tagDataSource.getAllTags().observe(viewLifecycleOwner, Observer {
            it.let {
                it.forEach { tag ->
                    addChip(tag, false)
                }
            }
        })

        binding.setBinding()

        return binding.root
    }

    private fun addChip(tag: Tag, checked: Boolean) {
        if (checked) {
            chipBinding.tag = tag
            chipBinding.chipItem.tag = tag.id
            chipBinding.chipItem.isChecked = true
            chipBinding.chipItem.setOnCloseIconClickListener {
                binding.selectedChipGroup.removeView(it)
                detachTag(it.tag)
            }
            if (chipBinding.chipItem.parent != null)
                (chipBinding.chipItem.parent as ViewGroup).removeView(chipBinding.chipItem)
            binding.selectedChipGroup.addView(chipBinding.chipItem)
        } else {
            chipActionBinding.tag = tag
            chipActionBinding.chipAction.tag = tag.id
            chipActionBinding.chipAction.setOnClickListener {
                it as Chip
                attachTag(it.tag)
            }
            if (chipActionBinding.chipAction.parent != null)
                (chipActionBinding.chipAction.parent as ViewGroup).removeView(chipActionBinding.chipAction)
            binding.chipGroup.addView(chipActionBinding.chipAction)
        }
    }

    private fun init(): TagViewModel {
        val application = requireNotNull(this.activity).application
        val tagDataSource = NoteDatabase.getInstance(application).tagDao
        val noteDataSource = NoteDatabase.getInstance(application).noteDao
        val noteTagDataSource = NoteDatabase.getInstance(application).noteTagDao

        val tagViewModelFactory =
            TagViewModelFactory(_noteId, tagDataSource, noteDataSource, noteTagDataSource)

        return ViewModelProvider(this, tagViewModelFactory).get(TagViewModel::class.java)
    }

    private fun FragmentContentTagBinding.setBinding() {
        tagViewModel = this@TagFragment.tagViewModel

        lifecycleOwner = this@TagFragment
    }

    private fun attachTag(tag: Any) {
        tagViewModel.onAttach(_noteId, tag as Long)
    }

    private fun detachTag(tag: Any) {
        tagViewModel.onDetach(_noteId, tag as Long)
    }
}

