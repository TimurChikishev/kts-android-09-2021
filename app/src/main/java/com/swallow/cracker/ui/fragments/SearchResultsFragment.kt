package com.swallow.cracker.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.swallow.cracker.R
import com.swallow.cracker.databinding.FragmentSearchResultBinding
import com.swallow.cracker.ui.adapters.SearchResultsFragmentAdapter
import com.swallow.cracker.utils.bottomNavigationGone

class SearchResultsFragment : Fragment(R.layout.fragment_search_result) {
    private val args: SearchResultsFragmentArgs by navArgs()
    private val query: String by lazy { args.query }
    private val viewBinding by viewBinding(FragmentSearchResultBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavigationGone()
        initAppBarContent()
        initTabBar()
    }

    private fun initAppBarContent() = with(viewBinding.includeAppBar) {
        if (query.isEmpty())
            queryTextView.text = resources.getString(R.string.home)
        else
            queryTextView.text = query

        topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initTabBar() = with(viewBinding) {
        viewPager.adapter = SearchResultsFragmentAdapter(query, childFragmentManager, lifecycle)

        TabLayoutMediator(includeAppBar.headerTabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
            viewPager.setCurrentItem(tab.position, false)
        }.attach()
    }

    companion object {
        private val tabTitles = listOf("Posts")
    }
}