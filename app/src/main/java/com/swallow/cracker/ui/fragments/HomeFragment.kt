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
import com.swallow.cracker.ui.adapters.delegates.items.RedditListItemImageDelegateAdapter
import com.swallow.cracker.ui.adapters.delegates.items.RedditListSimpleItemDelegateAdapter
import com.swallow.cracker.ui.model.RedditItems
import com.swallow.cracker.ui.model.RedditListItemImage
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

            // TODO: Переделать когда появится ROOM
            override fun onVoteClick(position: Int, likes: Boolean) {
                val item = redditAdapter.snapshot()[position] ?: return
                postViewModel.votePost(item = item, likes = likes, position = position)
            }

            // TODO: Переделать когда появится ROOM
            override fun onSavedClick(category: String?, id: String, position: Int?, saved: Boolean) {
                if (saved)
                    postViewModel.savePost(category = category, id = id, position = position)
                else
                    postViewModel.unSavePost(id = id, position = position)
            }

            override fun navigateTo(item: RedditItems) {
                when(item){
                    is RedditListSimpleItem -> {
                        val action = HomeFragmentDirections.actionHomeFragmentToDetailsPostSimple(item)
                        findNavController().navigate(action)
                    }
                    is RedditListItemImage -> {
                        val action = HomeFragmentDirections.actionHomeFragmentToDetailsImageFragment(item)
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
        postViewModel.eventMessage.observe(viewLifecycleOwner, { it?.let { msg -> showMessage(msg) } })

        redditViewModel.posts.observe(viewLifecycleOwner, {
            redditAdapter.submitData(lifecycle, it)
        })

        postViewModel.votePost.observe(viewLifecycleOwner, {
            it?.let { it.position?.let {
                position -> redditAdapter.onLikeClick(position = position, likes = it.flag)
            }}
        })

        postViewModel.savePost.observe(viewLifecycleOwner, {
            it?.let {
                it.position?.let { position -> redditAdapter.onSavedClick(position = position, it.flag) }
            }
        })

        postViewModel.savePostIsClickable.observe(viewLifecycleOwner, {
//            viewBinding.savedImageView.isClickable = it
        })
    }

    private fun initAdapter() {
        redditAdapter = ComplexDelegatesRedditListAdapter.Builder()
            .add(RedditListSimpleItemDelegateAdapter())
            .add(RedditListItemImageDelegateAdapter())
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