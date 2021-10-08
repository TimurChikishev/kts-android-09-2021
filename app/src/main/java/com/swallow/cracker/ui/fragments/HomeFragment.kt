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
import com.swallow.cracker.ui.adapters.delegates.RedditListItemWithImageDelegateAdapter
import com.swallow.cracker.ui.adapters.delegates.RedditListSimpleItemDelegateAdapter
import com.swallow.cracker.ui.model.RedditList
import com.swallow.cracker.ui.model.RedditListItemWithImage
import com.swallow.cracker.ui.model.RedditListSimpleItem
import com.swallow.cracker.ui.viewmodels.PostViewModel
import com.swallow.cracker.ui.viewmodels.RedditListViewModel
import com.swallow.cracker.utils.autoCleared
import com.swallow.cracker.utils.showMessage

class HomeFragment : Fragment(R.layout.fragment_home) {
    private val redditViewModel: RedditListViewModel by viewModels()
    private val postViewModel: PostViewModel by viewModels()
    private val viewBinding by viewBinding(FragmentHomeBinding::bind)
    private var redditAdapter: ComplexDelegatesRedditListAdapter by autoCleared()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        bindingViewModel()
        bindingOfClick()
    }

    private fun bindingOfClick() {
        redditAdapter.attachClickDelegate(object : ComplexDelegateAdapterClick {
            // TODO: переделать когда появится Room и Mediator
            override fun onVoteClick(position: Int, likes: Boolean) {
                val item = redditAdapter.snapshot()[position] ?: return
                postViewModel.votePost(item = item, likes = likes, position = position)
            }

            override fun navigateTo(item: RedditList) {
                when(item){
                    is RedditListSimpleItem -> {
                        val action = HomeFragmentDirections.actionHomeFragmentToDetailPostSimpleItem(item)
                        findNavController().navigate(action)
                    }
                    is RedditListItemWithImage -> {
                        val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(item)
                        findNavController().navigate(action)
                    }
                }
            }

            override fun shared(url: String) {
                val intent = postViewModel.shared(url)
                startActivity(intent)
            }
        })
    }

    private fun bindingViewModel() {
        redditViewModel.posts.observe(viewLifecycleOwner, {
            redditAdapter.submitData(lifecycle, it)
        })

        postViewModel.eventMessage.observe(viewLifecycleOwner, { it?.let { msg -> showMessage(msg) } })

        // TODO: переделать когда появится Room и Mediator
        postViewModel.votePost.observe(viewLifecycleOwner, {
            it?.let { it.position?.let {
                position -> redditAdapter.onLikeClick(position = position, likes = it.likes)
            }}
        })
    }

    private fun initAdapter() {
        redditAdapter = ComplexDelegatesRedditListAdapter.Builder()
            .add(RedditListSimpleItemDelegateAdapter())
            .add(RedditListItemWithImageDelegateAdapter())
            .build()

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