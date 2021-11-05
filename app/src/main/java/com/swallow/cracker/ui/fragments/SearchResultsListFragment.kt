package com.swallow.cracker.ui.fragments

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.swallow.cracker.ui.model.RedditListItemImage
import com.swallow.cracker.ui.model.RedditListSimpleItem
import com.swallow.cracker.utils.showMessage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

// TODO(переписать получение данных без сохранения в базу данных)
// Из-за сохранения данных происходит refresh в remote mediator,
// что приводит к обновлению всех списков
class SearchResultsListFragment : ListFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.getString(KEY_QUERY)?.let {
            setQuery(it)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun bindingViewModel() = with(viewLifecycleOwner.lifecycleScope) {
        launchWhenCreated { redditViewModel.searchItems.collectLatest { redditAdapter.submitData(it) } }

        launchWhenStarted { redditViewModel.eventMessage.collect(::showMessage) }
    }

    override fun navigateToDetailsImage(item: RedditListItemImage) {
        val action = SearchResultsFragmentDirections.actionSearchResultFragmentToDetailsPostImageFragment(item)
        findNavController().navigate(action)
    }

    override fun navigateToDetailSimple(item: RedditListSimpleItem) {
        val action = SearchResultsFragmentDirections.actionSearchResultFragmentToDetailsPostSimpleFragment(item)
        findNavController().navigate(action)
    }

    override fun navigateToSubredditFragment(subreddit: String) {
        val action = SearchResultsFragmentDirections.actionSearchResultFragmentToSubredditFragment(subreddit)
        findNavController().navigate(action)
    }

    companion object {
        private const val KEY_QUERY = "KEY_QUERY"
    }
}