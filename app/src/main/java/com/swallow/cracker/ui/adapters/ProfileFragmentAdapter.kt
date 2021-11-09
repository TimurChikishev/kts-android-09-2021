package com.swallow.cracker.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.swallow.cracker.ui.fragments.ProfileAboutFragment

class ProfileFragmentAdapter  (
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount() = ITEM_COUNT

    override fun createFragment(position: Int): Fragment {
        return ProfileAboutFragment()
    }

    companion object {
        private const val ITEM_COUNT = 1
        private const val KEY_QUERY = "KEY_QUERY"
    }
}