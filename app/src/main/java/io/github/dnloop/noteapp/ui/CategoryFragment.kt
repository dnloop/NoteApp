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
import io.github.dnloop.noteapp.adapter.CategoryAdapter
import io.github.dnloop.noteapp.adapter.CategoryListener
import io.github.dnloop.noteapp.data.Category
import io.github.dnloop.noteapp.data.NoteDatabase
import io.github.dnloop.noteapp.databinding.FragmentCategoryBinding
import io.github.dnloop.noteapp.ui.viewmodel.CategoryViewModel
import io.github.dnloop.noteapp.ui.viewmodel.CategoryViewModelFactory
import timber.log.Timber

class CategoryFragment : Fragment(), CategoryDialogFragment.CategoryDialogListener {

    private lateinit var binding: FragmentCategoryBinding

    private lateinit var categoryViewModel: CategoryViewModel

    private var _category: Category = Category()

    private fun showCategoryDialog(Category: Category) {
        val dialog = CategoryDialogFragment(Category)
        dialog.listener = this
        dialog.show(childFragmentManager, "CategoryDialogFragment")
    }

    override fun onDialogPositiveClick(dialog: DialogFragment, category: Category) {
        categoryViewModel.onUpdate(category)
        Toast.makeText(context, "Category edited", Toast.LENGTH_SHORT).show()
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        categoryViewModel = init()

        val adapter =  CategoryAdapter(CategoryListener {
            catId -> categoryViewModel.onCategorySelected(catId)
        })

        adapter.clickListener.setOnDeleteClickListener { category ->
            category.let {
                categoryViewModel.onDelete(it)
                Toast.makeText(context, "Tag deleted: ${it.name}", Toast.LENGTH_SHORT).show()
            }
        }

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_category, container, false
        )

        binding.setBinding(adapter)

        categoryViewModel.categoryDataSource.getAllCategories().observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        categoryViewModel.openDialogEditor.observe(viewLifecycleOwner, Observer {tag ->
            tag?.let {
                showTagDialog(it)
            }
        })

        return binding.root
    }

    private fun showTagDialog(category: Category) {
        val dialog = CategoryDialogFragment(category)
        dialog.listener = this
        dialog.show(childFragmentManager, "CategoryDialogFragment")
    }

    private fun FragmentCategoryBinding.setBinding(adapter: CategoryAdapter) {
        (activity as MainActivity?)?.getFloatingActionButton()?.hide()

        binding.categoryList.adapter = adapter

        binding.categoryViewModel = this@CategoryFragment.categoryViewModel

        binding.lifecycleOwner = this@CategoryFragment

        btnAdd.setOnClickListener {
            checkCategory()
        }
    }

    private fun checkCategory() {
        _category.name = binding.categoryInput.text.toString()
        if (_category.name.isNotBlank()) {
            categoryViewModel.onInsert(_category)
            Timber.i("Tag '${_category.name}' Inserted")
        }
    }

    private fun init(): CategoryViewModel {
        val application = requireNotNull(this.activity).application

        val dataSource = NoteDatabase.getInstance(application).categoryDao

        val viewModelFactory = CategoryViewModelFactory(dataSource, application)

        return ViewModelProvider(this, viewModelFactory).get(CategoryViewModel::class.java)
    }
}