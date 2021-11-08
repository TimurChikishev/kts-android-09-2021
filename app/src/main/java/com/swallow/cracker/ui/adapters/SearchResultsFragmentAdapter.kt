package com.swallow.cracker.ui.adapters

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.swallow.cracker.ui.fragments.SearchResultsListFragment

class SearchResultsFragmentAdapter constructor(
    private val query: String,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount() = ITEM_COUNT

    override fun createFragment(position: Int): Fragment {
        return buildFragment(SearchResultsListFragment())
    }

    private fun buildFragment(fragment: Fragment): Fragment {
        fragment.arguments = bundleOf(KEY_QUERY to query)
        return fragment
    }

    companion object {
        private const val ITEM_COUNT = 1
        private const val KEY_QUERY = "KEY_QUERY"
    }
}