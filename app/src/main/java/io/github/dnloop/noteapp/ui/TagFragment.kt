package io.github.dnloop.noteapp.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.iterator
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.chip.Chip
import io.github.dnloop.noteapp.R
import io.github.dnloop.noteapp.data.NoteDatabase
import io.github.dnloop.noteapp.databinding.FragmentContentTagBinding
import io.github.dnloop.noteapp.ui.viewmodel.TagViewModel
import io.github.dnloop.noteapp.ui.viewmodel.TagViewModelFactory
import kotlinx.android.synthetic.main.chip_item.*


/**
 * A simple [Fragment] subclass.
 */
class TagFragment(val _noteId: Long) : Fragment() {

    private lateinit var binding: FragmentContentTagBinding

    private lateinit var tagViewModel: TagViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_content_tag, container, false
        )

        tagViewModel = init()

        binding.setBinding()

        return binding.root
    }

    private fun init(): TagViewModel {
        val application = requireNotNull(this.activity).application

        val tagDataSource = NoteDatabase.getInstance(application).tagDao
        val noteTagDataSource = NoteDatabase.getInstance(application).noteTagDao

        val tagViewModelFactory = TagViewModelFactory(_noteId, tagDataSource, noteTagDataSource)

        return ViewModelProvider(this, tagViewModelFactory).get(TagViewModel::class.java)
    }

    private fun FragmentContentTagBinding.setBinding() {
        val inflater: LayoutInflater = LayoutInflater.from(activity)
        binding.tagViewModel = this@TagFragment.tagViewModel

        binding.lifecycleOwner = this@TagFragment

        btnAdd.setOnClickListener {
            val tags: List<String> = inputTag.text.toString().split(" ")
            for (text: String in tags) {
                val chip: Chip = inflater.inflate(
                    R.layout.chip_item, chipGroup, false
                ) as Chip
                chip.text = text
                chip.setOnCloseIconClickListener {
                    chipGroup.removeView(it)
                }
                if (chip.text.isNotBlank())
                    chipGroup.addView(chip)
            }
            inputTag.setText("")
        }

        btnSave.setOnClickListener {
            val counter = chipGroup.childCount
            if (counter > 0) {
                for (chip in chipGroup) {
                    chip as Chip
                    if (chip.isChecked)
                        saveTags()
                }
//                Toast.makeText(activity, "Tags added $counter", Toast.LENGTH_SHORT)
//                    .show()
            }
        }
    }

    private fun saveTags() {
        var id = tagViewModel.onInsertDummy(_noteId)
        Toast.makeText(activity, "Tag added $id, $_noteId", Toast.LENGTH_SHORT)
            .show()
    }
}