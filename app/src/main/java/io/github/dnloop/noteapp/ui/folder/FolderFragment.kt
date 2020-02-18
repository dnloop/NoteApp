package io.github.dnloop.noteapp.ui.folder


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.dnloop.noteapp.R

/**
 * A simple [Fragment] subclass.
 */
class FolderFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_folder, container, false)

        return root
    }


}
