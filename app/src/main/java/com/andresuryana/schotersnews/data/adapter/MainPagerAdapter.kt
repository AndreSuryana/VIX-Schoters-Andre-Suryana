package com.andresuryana.schotersnews.data.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.andresuryana.schotersnews.ui.bookmark.BookmarkFragment
import com.andresuryana.schotersnews.ui.home.HomeFragment
import com.andresuryana.schotersnews.ui.profile.ProfileFragment
import com.andresuryana.schotersnews.ui.search.SearchFragment

class MainPagerAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = SCREEN_COUNT

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            SCREEN_HOME -> HomeFragment()
            SCREEN_SEARCH -> SearchFragment()
            SCREEN_BOOKMARK -> BookmarkFragment()
            SCREEN_PROFILE -> ProfileFragment()
            else -> HomeFragment()
        }
    }

    companion object {
        const val SCREEN_COUNT = 4
        const val SCREEN_HOME = 0
        const val SCREEN_SEARCH = 1
        const val SCREEN_BOOKMARK = 2
        const val SCREEN_PROFILE = 3
    }
}