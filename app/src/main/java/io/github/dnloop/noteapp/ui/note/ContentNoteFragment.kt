package io.github.dnloop.noteapp.ui.note


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import io.github.dnloop.noteapp.MainActivity
import io.github.dnloop.noteapp.R


/**
 * A simple [Fragment] subclass.
 */
class ContentNoteFragment : Fragment() {

    private lateinit var tabLayout : TabLayout

    private lateinit var viewPager : ViewPager

    private var _code: Int = 0

    var code: Int
        get() {
            return _code
        }
        set(value) {
            _code = value
        }

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

        val pagerAdapter = ViewPagerAdapter(childFragmentManager, _code)

        viewPager.adapter = pagerAdapter

        tabLayout.setupWithViewPager(viewPager)

        (activity as MainActivity?)?.getFloatingActionButton()?.hide()

    }
}
