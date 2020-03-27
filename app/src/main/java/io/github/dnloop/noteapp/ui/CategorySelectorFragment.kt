package io.github.dnloop.noteapp.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import io.github.dnloop.noteapp.R
import io.github.dnloop.noteapp.adapter.CategoryDialogAdapter
import io.github.dnloop.noteapp.adapter.CategorySelectorListListener
import io.github.dnloop.noteapp.data.Category
import io.github.dnloop.noteapp.data.NoteDatabase
import io.github.dnloop.noteapp.databinding.DialogCategoryListBinding
import io.github.dnloop.noteapp.ui.viewmodel.CategorySelectorViewModel
import io.github.dnloop.noteapp.ui.viewmodel.CategorySelectorViewModelFactory

class CategorySelectorFragment(private var _category: Category) : DialogFragment() {
    internal lateinit var listener: CategorySelectorListener

    private lateinit var binding: DialogCategoryListBinding

    private lateinit var categorySelectorViewModel: CategorySelectorViewModel

    /**
     * The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it.
     * */
    interface CategorySelectorListener {
        fun onDialogPositiveClick(dialog: DialogFragment, category: Category)
        fun onDialogNeutralClick(dialog: DialogFragment, category: Category)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater

            binding = DataBindingUtil.inflate(
                inflater, R.layout.dialog_category_list, null, false
            )

            categorySelectorViewModel = init()

            val adapter =  CategoryDialogAdapter(CategorySelectorListListener {
                    catId -> categorySelectorViewModel.onCategorySelected(catId)
            })

            binding.setBinding(adapter)

            categorySelectorViewModel.loadCategories().observe(this, Observer { categoryList ->
                categoryList?.let {
                    adapter.submitList(categoryList)
                    categoryList.forEach { category ->
                        categorySelectorViewModel.onNoteCount(category)
                    }
                }
            })

            categorySelectorViewModel.selectedCategory.observe(this, Observer { category ->
                _category = category
            })

            categorySelectorViewModel.badgeCounter.observe(this, Observer {notes ->
                notes?.let { number ->
                    adapter.setBadgeCounter(number)
                }
            })
            val view: View = binding.root
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.confirm) { _, _ ->
                    listener.onDialogPositiveClick(this, _category)
                }
                .setNeutralButton(R.string.btn_remove_category) {_,_ ->
                    if(_category.id == -1L)
                        listener.onDialogNeutralClick(this, _category)
                    dialog?.dismiss()
                }
                .setNegativeButton(R.string.cancel) { _, _ ->
                    dialog?.cancel()
                    listener.onDialogNegativeClick(this)
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun init(): CategorySelectorViewModel {
        val application = requireNotNull(this.activity).application

        val dataSource = NoteDatabase.getInstance(application).categoryDao

        val viewModelFactory = CategorySelectorViewModelFactory(dataSource, application)

        return ViewModelProvider(this, viewModelFactory).get(CategorySelectorViewModel::class.java)
    }

    private fun DialogCategoryListBinding.setBinding(adapter: CategoryDialogAdapter) {
        binding.categorySimpleList.adapter = adapter

        binding.categorySelector = this@CategorySelectorFragment.categorySelectorViewModel

        binding.lifecycleOwner = this@CategorySelectorFragment

        selectedCategory.text = _category.name
    }
}