package io.github.dnloop.noteapp.ui.note

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter

class ViewPagerAdapter(fm: FragmentManager, private val code: Int) : FragmentPagerAdapter(fm) {

    val ADD_CODE: Int = 0

    val EDIT_CODE: Int = 1

    override fun getItem(position: Int): Fragment {
        return when (position) {

            0 -> if (code == ADD_CODE) AddNoteFragment() else EditNoteFragment()
            else -> TagFragment()
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Note"
            else -> "Tags"

        }
    }
}