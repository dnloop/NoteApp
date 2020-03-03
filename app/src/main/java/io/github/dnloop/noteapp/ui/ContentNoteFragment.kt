package io.github.dnloop.noteapp.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import io.github.dnloop.noteapp.MainActivity
import io.github.dnloop.noteapp.R
import io.github.dnloop.noteapp.adapter.ViewPagerAdapter
import io.github.dnloop.noteapp.ui.ContentNoteFragmentArgs


/**
 * A simple [Fragment] subclass.
 */
class ContentNoteFragment : Fragment() {

    private lateinit var tabLayout : TabLayout

    private lateinit var viewPager : ViewPager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.content_note, container, false)

        initComponents(root)

        return root
    }

    override fun onDetach() {
        super.onDetach()
        (activity as MainActivity?)?.getFloatingActionButton()?.show()
    }

    private fun initComponents(root: View) {
        viewPager = root.findViewById(R.id.viewpager)

        tabLayout = root.findViewById(R.id.tabLayout)

        val arguments =
            ContentNoteFragmentArgs.fromBundle(
                arguments!!
            )

        val pagerAdapter = ViewPagerAdapter(childFragmentManager)

        pagerAdapter.setNoteId(arguments.noteId)

        viewPager.adapter = pagerAdapter

        tabLayout.setupWithViewPager(viewPager)

        (activity as MainActivity?)?.getFloatingActionButton()?.hide()
    }
}
