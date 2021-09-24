package com.swallow.cracker.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.swallow.cracker.R
import com.swallow.cracker.databinding.FragmentHomeBinding
import com.swallow.cracker.ui.adapters.SubredditListAdapter
import com.swallow.cracker.ui.modal.RedditListItem
import com.swallow.cracker.ui.modal.RedditListItemWithImage
import com.swallow.cracker.utils.autoCleared

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewBinding by viewBinding(FragmentHomeBinding::bind)
    private var subRedditAdapter: SubredditListAdapter by autoCleared()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSubredditList()
    }

    private fun initSubredditList() {
        subRedditAdapter = SubredditListAdapter()

        with(viewBinding.redditRecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = subRedditAdapter
        }

        val items = List(20) {
            when ((1..2).random()) {
                1 -> RedditListItem(
                    author = "author",
                    title = "title",
                    selftext = "selftext",
                    ups = 10,
                    numComments = 2,
                    created = 1632366141,
                    url = ""
                )
                2 -> RedditListItemWithImage(
                    author = "author",
                    title = "title",
                    selftext = "selftext",
                    ups = 10,
                    numComments = 2,
                    created = 1632366141,
                    thumbnail = "https://b.thumbs.redditmedia.com/S0mCFAfwkXHBKk6jzZzpZvdwAF7dTKIUxDajf8vayok.jpg",
                    url = ""
                )
                else -> error("Wrong random number")
            }
        }
        subRedditAdapter.setItems(items)
    }
}