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
import io.github.dnloop.noteapp.R
import io.github.dnloop.noteapp.data.NoteDatabase
import io.github.dnloop.noteapp.data.Tag
import io.github.dnloop.noteapp.databinding.ChipItemBinding
import io.github.dnloop.noteapp.databinding.FragmentContentTagBinding
import io.github.dnloop.noteapp.ui.viewmodel.TagViewModel
import io.github.dnloop.noteapp.ui.viewmodel.TagViewModelFactory


/**
 * A simple [Fragment] subclass.
 */
class TagFragment(private val _noteId: Long) : Fragment() {

    private lateinit var binding: FragmentContentTagBinding

    private lateinit var chipCheckedBinding: ChipItemBinding

    private lateinit var chipAllBinding: ChipItemBinding

    private lateinit var tagViewModel: TagViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_content_tag, container, false
        )

        chipCheckedBinding = DataBindingUtil.inflate(
            inflater, R.layout.chip_item, binding.selectedChipGroup, false
        )

        chipAllBinding = DataBindingUtil.inflate(
            inflater, R.layout.chip_item, binding.chipGroup, false
        )

        tagViewModel = init()

        tagViewModel.noteDataSource.getNoteWithTags(_noteId).observe(viewLifecycleOwner, Observer {
            it.let {
                it.tags.forEach { tag ->
                    addCheckedChip(tag)
                }
            }
        })

        tagViewModel.tagDataSource.getAllTags().observe(viewLifecycleOwner, Observer {
            it.let {
                it.forEach { tag ->
                    addAllChip(tag)
                }
            }
        })

        binding.setBinding()

        return binding.root
    }

    private fun addCheckedChip(tag: Tag) {
        chipCheckedBinding.tag = tag
        chipCheckedBinding.chipItem.tag = tag.id
        chipCheckedBinding.chipItem.isChecked = true
        chipCheckedBinding.chipItem.setOnCloseIconClickListener {
            binding.selectedChipGroup.removeView(it)
        }
        if (chipCheckedBinding.chipItem.parent != null)
            (chipCheckedBinding.chipItem.parent as ViewGroup).removeView(chipCheckedBinding.chipItem)
        binding.selectedChipGroup.addView(chipCheckedBinding.chipItem)
    }

    private fun addAllChip(tag: Tag) {
        chipAllBinding.tag = tag
        chipAllBinding.chipItem.tag = tag.id
        chipAllBinding.chipItem.setOnCloseIconClickListener {
            binding.chipGroup.removeView(it)
        }
        if (chipAllBinding.chipItem.parent != null)
            (chipAllBinding.chipItem.parent as ViewGroup).removeView(chipAllBinding.chipItem)
        binding.chipGroup.addView(chipAllBinding.chipItem)
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
        binding.tagViewModel = this@TagFragment.tagViewModel

        binding.lifecycleOwner = this@TagFragment

        btnAttach.setOnClickListener {
            val counter = chipGroup.childCount
            if (counter > 0) {
                for (chip in chipGroup) {
                    chip as Chip
                    if (chip.isChecked) {
                        attachTag(chip.tag)
                    }
                }
                Toast.makeText(activity, "Tags added $counter", Toast.LENGTH_SHORT)
                    .show()
                chipGroup.clearCheck()
            }
        }
        btnDetach.setOnClickListener {
            val counter = selectedChipGroup.childCount
            if (counter > 0) {
                for (chip in selectedChipGroup) {
                    chip as Chip
                    if (chip.isChecked) {
                        selectedChipGroup.removeView(chip)
                        detachTag(chip.tag)
                    }
                }
                Toast.makeText(activity, "Tags removed $counter", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun attachTag(tag: Any) {
        tagViewModel.onAttach(_noteId, tag as Long)
    }

    private fun detachTag(tag: Any) {
        tagViewModel.onDetach(_noteId, tag as Long)
    }
}

