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
import io.github.dnloop.noteapp.data.NoteDatabase
import io.github.dnloop.noteapp.databinding.FragmentHomeBinding
import io.github.dnloop.noteapp.ui.viewmodel.NoteViewModel
import io.github.dnloop.noteapp.ui.viewmodel.NoteViewModelFactory

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentHomeBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = NoteDatabase.getInstance(application).noteDao

        val viewModelFactory = NoteViewModelFactory(dataSource, application)

        val homeViewModel =
            ViewModelProvider(this, viewModelFactory).get(NoteViewModel::class.java)

        val adapter = NoteAdapter()

        binding.noteList.adapter = adapter

        binding.homeViewModel = homeViewModel

        binding.lifecycleOwner = this

        homeViewModel.allNotes.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it
            }
        })

        return binding.root
    }
}