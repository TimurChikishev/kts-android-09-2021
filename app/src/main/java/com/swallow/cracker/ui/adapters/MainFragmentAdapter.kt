package com.swallow.cracker.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.swallow.cracker.ui.fragments.HomeFragment
import com.swallow.cracker.ui.fragments.PopularFragment

class MainFragmentAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount() = ITEM_COUNT

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> HomeFragment()
            else -> PopularFragment()
        }
    }

    companion object {
        private const val ITEM_COUNT = 2
    }
}