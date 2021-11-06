package io.github.dnloop.noteapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import io.github.dnloop.noteapp.ui.EditNoteFragment
import io.github.dnloop.noteapp.ui.TagFragment

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var _noteId: Long = 0L
    private var _archived: Boolean = false

    fun setParameters(noteId: Long?, archived: Boolean) {
        if(noteId != null)
            _noteId = noteId
        _archived = archived
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {

            0 -> EditNoteFragment(_noteId, _archived)
            else -> TagFragment(_noteId)
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