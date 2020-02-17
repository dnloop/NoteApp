package io.github.dnloop.noteapp.archive


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.dnloop.noteapp.R

/**
 * A simple [Fragment] subclass.
 */
class ArchiveFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_archive, container, false)
        // TODO archive logic
        return root
    }


}
