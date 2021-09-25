package com.swallow.cracker.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.swallow.cracker.R
import com.swallow.cracker.databinding.FragmentHomeBinding
import com.swallow.cracker.ui.adapters.RedditListAdapter
import com.swallow.cracker.ui.viewmodels.RedditListViewModel
import com.swallow.cracker.utils.autoCleared

class HomeFragment : Fragment(R.layout.fragment_home) {
    private val viewModel: RedditListViewModel by viewModels()
    private val viewBinding by viewBinding(FragmentHomeBinding::bind)
    private var redditAdapter: RedditListAdapter by autoCleared()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSubredditList()
        bindingViewModel()
    }

    private fun bindingViewModel() {
        viewModel.posts.observe(viewLifecycleOwner, {
            redditAdapter.submitData(lifecycle, it)
        })
    }

    private fun initSubredditList() {
        redditAdapter = RedditListAdapter()

        with(viewBinding.redditRecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = redditAdapter
            setHasFixedSize(true)
        }
    }
}