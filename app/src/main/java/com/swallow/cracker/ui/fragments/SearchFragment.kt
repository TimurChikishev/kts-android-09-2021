package com.swallow.cracker.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.swallow.cracker.R
import com.swallow.cracker.databinding.FragmentSearchBinding
import com.swallow.cracker.ui.adapters.search.delegates.ComplexSearchDelegatesListAdapter
import com.swallow.cracker.ui.adapters.search.delegates.EventSearchDelegateListAdapter
import com.swallow.cracker.ui.model.SearchQuery
import com.swallow.cracker.ui.model.SearchQueryTransaction
import com.swallow.cracker.ui.model.Subreddit
import com.swallow.cracker.ui.viewmodels.SearchViewModel
import com.swallow.cracker.utils.autoCleared
import com.swallow.cracker.utils.bottomNavigationGone
import kotlinx.coroutines.flow.collect
import timber.log.Timber

class SearchFragment : Fragment(R.layout.fragment_search) {
    private val searchViewModel: SearchViewModel by viewModels()
    private val viewBinding by viewBinding(FragmentSearchBinding::bind)
    private var searchAdapter: ComplexSearchDelegatesListAdapter by autoCleared()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavigationGone()
        initAdapter()
        bindingOfClick()
        bindSearchView()
        bindViewModel()
    }

    private fun initAdapter() {
        searchAdapter = ComplexSearchDelegatesListAdapter().apply {
            initEventDelegate(eventDelegateObject)
            init()
        }

        with(viewBinding.searchRecyclerView) {
            val orientation = RecyclerView.VERTICAL
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(context, orientation, false)
        }
    }

    private val eventDelegateObject = object : EventSearchDelegateListAdapter {
        override fun removeItem(item: Any) {
            when (item) {
                is SearchQuery -> searchViewModel.removeSearchQuery(item)
                else -> Timber.tag("ERROR").d("This type has no deletion method!")
            }
        }

        override fun onItemClick(item: Any) {
            when (item) {
                is Subreddit -> searchViewModel.savedSearchQuery(item.displayName, item)
                is SearchQuery ->searchViewModel.savedSearchQuery(item.query)
            }
        }
    }

    private fun bindViewModel() = with(viewLifecycleOwner.lifecycleScope) {
        searchViewModel.init()

        launchWhenStarted { searchViewModel.searchSubreddit.collect(::updateItems) }

        launchWhenStarted { searchViewModel.searchQuery.collect(::updateItems) }

        launchWhenStarted { searchViewModel.searchQueryIsSaved.collect(::searchQueryIsSaved) }

        launchWhenStarted { searchViewModel.searchQueryIsRemoved.collect(::searchQueryIsRemoved) }
    }

    private fun searchQueryIsRemoved(item: SearchQuery) {
        searchAdapter.removeItem(item)
    }

    private fun searchQueryIsSaved(transaction: SearchQueryTransaction) {
        if (transaction.subreddit == null) {
            navigateToSearchResultsFragment(transaction.query)
        } else {
            navigateToSubredditFragment(transaction.subreddit)
        }
    }

    private fun updateItems(items: List<Any>) {
        if (items.isNotEmpty()) searchAdapter.items = items
    }

    private fun bindSearchView() {
        viewBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrEmpty()) return false
                searchViewModel.savedSearchQuery(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                when (newText.isNullOrEmpty()) {
                    true -> searchViewModel.getSavedSearchQuery(500)
                    false -> searchViewModel.searchSubreddit(newText)
                }
                return true
            }
        })
    }

    private fun bindingOfClick() = with(viewBinding) {
        backMaterialButton.setOnClickListener { findNavController().popBackStack() }
    }

    private fun navigateToSearchResultsFragment(query: String) {
        val action = SearchFragmentDirections.actionSearchFragmentToSearchResultFragment(query)
        findNavController().navigate(action)
    }

    private fun navigateToSubredditFragment(item: Subreddit) {
        val action = SearchFragmentDirections.actionSearchFragmentToSubredditFragment(item.displayName)
        findNavController().navigate(action)
    }
}