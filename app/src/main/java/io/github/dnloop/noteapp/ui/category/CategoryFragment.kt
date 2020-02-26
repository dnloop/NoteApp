package io.github.dnloop.noteapp.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import io.github.dnloop.noteapp.R
import io.github.dnloop.noteapp.ui.viewmodel.CategoryViewModel

class CategoryFragment : Fragment() {

    private lateinit var categoryViewModel: CategoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        categoryViewModel =
                ViewModelProviders.of(this).get(CategoryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_category, container, false)

        return root
    }
}