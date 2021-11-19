package com.swallow.cracker.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.swallow.cracker.ui.model.RedditListItemImage
import com.swallow.cracker.ui.model.RedditListSimpleItem
import com.swallow.cracker.ui.viewmodels.SharedSubredditViewModel
import kotlinx.coroutines.flow.collect

// TODO(переписать получение данных без сохранения в базу данных)
// Из-за сохранения данных происходит refresh в remote mediator,
// что приводит к обновлению всех списков
class SubredditListFragment : ListFragment() {

    private val sharedViewModel: SharedSubredditViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindSharedViewModel()
    }

    private fun bindSharedViewModel() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            sharedViewModel.queryStateFlow.collect { it?.let { setQuery(it) }}
        }
    }

    override fun navigateToDetailsImage(item: RedditListItemImage) {
        val action = SubredditFragmentDirections.actionSubredditFragmentToDetailsPostImageFragment(item)
        findNavController().navigate(action)
    }

    override fun navigateToDetailSimple(item: RedditListSimpleItem) {
        val action = SubredditFragmentDirections.actionSubredditFragmentToDetailsPostSimpleFragment(item)
        findNavController().navigate(action)
    }

    override fun navigateToSubredditFragment(subreddit: String) = Unit
}