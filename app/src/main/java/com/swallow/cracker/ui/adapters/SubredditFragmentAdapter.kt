package com.swallow.cracker.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.swallow.cracker.ui.fragments.SubredditAboutFragment
import com.swallow.cracker.ui.fragments.SubredditListFragment

class SubredditFragmentAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount() = ITEM_COUNT

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> SubredditListFragment()
            else -> SubredditAboutFragment()
        }
    }

    companion object {
        private const val ITEM_COUNT = 2
    }
}