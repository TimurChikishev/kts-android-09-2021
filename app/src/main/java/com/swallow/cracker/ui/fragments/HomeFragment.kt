package com.swallow.cracker.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.swallow.cracker.R
import com.swallow.cracker.databinding.FragmentHomeBinding
import com.swallow.cracker.ui.adapters.LoadStateAdapter
import com.swallow.cracker.ui.adapters.delegates.ComplexDelegateAdapterClick
import com.swallow.cracker.ui.adapters.delegates.ComplexDelegatesRedditListAdapter
import com.swallow.cracker.ui.adapters.delegates.RedditListSimpleItemDelegateAdapter
import com.swallow.cracker.ui.adapters.delegates.RedditListItemWithImageDelegateAdapter
import com.swallow.cracker.ui.modal.RedditListItemWithImage
import com.swallow.cracker.ui.modal.RedditListSimpleItem
import com.swallow.cracker.ui.viewmodels.RedditListViewModel
import com.swallow.cracker.utils.autoCleared

class HomeFragment : Fragment(R.layout.fragment_home) {
    private val viewModel: RedditListViewModel by viewModels()
    private val viewBinding by viewBinding(FragmentHomeBinding::bind)
    private var redditAdapter: ComplexDelegatesRedditListAdapter by autoCleared()

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
        redditAdapter = ComplexDelegatesRedditListAdapter.Builder()
            .add(RedditListSimpleItemDelegateAdapter())
            .add(RedditListItemWithImageDelegateAdapter())
            .build()

        redditAdapter.attachClickDelegate(object : ComplexDelegateAdapterClick {
            override fun onLikeClick(position: Int, likes: Boolean) {
                redditAdapter.onLikeClick(position, likes)
            }

            override fun navigateToDetailsWithImage(item: RedditListItemWithImage) {
                val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(item)
                findNavController().navigate(action)
            }

            override fun navigateToDetailsSimple(item: RedditListSimpleItem) {
                val action = HomeFragmentDirections.actionHomeFragmentToDetailPostSimpleItem(item)
                findNavController().navigate(action)
            }
        })

        with(viewBinding) {
            redditRecyclerView.setHasFixedSize(true)
            redditRecyclerView.layoutManager = LinearLayoutManager(context)
            redditRecyclerView.adapter = redditAdapter.withLoadStateHeaderAndFooter(
                header = LoadStateAdapter { redditAdapter.retry() },
                footer = LoadStateAdapter { redditAdapter.retry() }
            )

            includedLoadState.buttonRetry.setOnClickListener {
                redditAdapter.retry()
            }
        }

        redditAdapter.addLoadStateListener { loadState ->
            viewBinding.apply {
                redditRecyclerView.isVisible =
                    loadState.source.refresh is LoadState.NotLoading
                includedLoadState.progressBar.isVisible =
                    loadState.source.refresh is LoadState.Loading
                includedLoadState.buttonRetry.isVisible =
                    loadState.source.refresh is LoadState.Error
                includedLoadState.textViewError.isVisible =
                    loadState.source.refresh is LoadState.Error

                // for empty view
                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    redditAdapter.itemCount < 1
                ) {
                    viewBinding.redditRecyclerView.isVisible = false
                    includedLoadState.textViewError.isVisible = true
                } else {
                    includedLoadState.textViewError.isVisible = false
                }
            }
        }
    }
}