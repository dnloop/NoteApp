package io.github.dnloop.noteapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import io.github.dnloop.noteapp.R
import io.github.dnloop.noteapp.adapter.NoteAdapter
import io.github.dnloop.noteapp.databinding.FragmentHomeBinding
import io.github.dnloop.noteapp.ui.viewmodel.NoteViewModel

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: NoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(NoteViewModel::class.java)
        val binding: FragmentHomeBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false)

        val adapter = NoteAdapter()

        binding.noteList.adapter = adapter

        binding.lifecycleOwner = this

        homeViewModel.getNotes().observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it
            }
        })

        return binding.root
    }
}