package io.github.dnloop.noteapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import io.github.dnloop.noteapp.ui.note.EditNoteFragment
import io.github.dnloop.noteapp.ui.tag.TagFragment

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {

            0 -> EditNoteFragment()
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