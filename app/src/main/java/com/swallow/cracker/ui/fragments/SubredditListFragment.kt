package com.swallow.cracker.ui.fragments

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.swallow.cracker.ui.model.RedditListItemImage
import com.swallow.cracker.ui.model.RedditListSimpleItem

// TODO(переписать получение данных без сохранения в базу данных)
// Из-за сохранения данных происходит refresh в remote mediator,
// что приводит к обновлению всех списков
class SubredditListFragment : ListFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.getString(KEY_QUERY)?.let {
            setQuery(it)
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

    companion object {
        private const val KEY_QUERY = "KEY_QUERY"
    }
}