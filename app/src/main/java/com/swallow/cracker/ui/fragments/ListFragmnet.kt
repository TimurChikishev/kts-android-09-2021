package com.swallow.cracker.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.swallow.cracker.R
import com.swallow.cracker.databinding.FragmentListBinding
import com.swallow.cracker.ui.adapters.LoadStateAdapter
import com.swallow.cracker.ui.adapters.listing.delegates.ComplexDelegateAdapterClick
import com.swallow.cracker.ui.adapters.listing.delegates.ComplexDelegatesRedditListAdapter
import com.swallow.cracker.ui.adapters.listing.delegates.items.RedditListItemImageDelegateAdapter
import com.swallow.cracker.ui.adapters.listing.delegates.items.RedditListSimpleItemDelegateAdapter
import com.swallow.cracker.ui.model.RedditItem
import com.swallow.cracker.ui.model.RedditListItemImage
import com.swallow.cracker.ui.model.RedditListSimpleItem
import com.swallow.cracker.ui.viewmodels.RedditListViewModel
import com.swallow.cracker.utils.asMergedLoadStates
import com.swallow.cracker.utils.getDataFormCacheSnackBar
import com.swallow.cracker.utils.sharedUrl
import com.swallow.cracker.utils.showMessage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

open class ListFragment : Fragment(R.layout.fragment_list) {
    protected val redditViewModel: RedditListViewModel by viewModel()
    protected val redditAdapter: ComplexDelegatesRedditListAdapter by lazy {
        ComplexDelegatesRedditListAdapter.Builder()
            .add(RedditListSimpleItemDelegateAdapter())
            .add(RedditListItemImageDelegateAdapter())
            .build()
    }

    private val viewBinding by viewBinding(FragmentListBinding::bind)
    private var dataFromCacheSnackBar: Snackbar? = null

    protected fun setQuery(query: String) {
        redditViewModel.setQuery(query)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        bindingViewModel()
        bindingOfClick()
        initSnackBar()
        initSwipeRefreshLayout()
    }

    private fun initSnackBar() = with(viewBinding) {
        dataFromCacheSnackBar = context?.let { getDataFormCacheSnackBar(root) }
    }

    private fun initSwipeRefreshLayout() = with(viewBinding) {
        swipeContainer.setOnRefreshListener(redditAdapter::refresh)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    protected open fun bindingViewModel() = with(viewLifecycleOwner.lifecycleScope) {
        launchWhenCreated { redditViewModel.listingItems.collectLatest { redditAdapter.submitData(it) } }

        launchWhenStarted { redditViewModel.eventMessage.collect(::showMessage) }
    }

    private fun bindingOfClick() {
        viewBinding.includeRetry.buttonRetry.setOnClickListener { redditAdapter.retry() }

        redditAdapter.attachClickDelegate(object : ComplexDelegateAdapterClick {
            override fun onVoteClick(item: RedditItem, likes: Boolean) {
                redditViewModel.votePost(item, likes)
            }

            override fun onSavedClick(item: RedditItem, saved: Boolean) = when (saved) {
                true -> redditViewModel.savePost(item)
                false -> redditViewModel.unSavePost(item)
            }

            override fun navigateTo(item: RedditItem) = when (item) {
                is RedditListSimpleItem -> navigateToDetailSimple(item)
                is RedditListItemImage -> navigateToDetailsImage(item)
            }

            override fun onSubredditIconClick(item: RedditItem) {
                navigateToSubredditFragment(item.subreddit)
            }

            override fun shared(url: String): Unit = startActivity(sharedUrl(url))
        })
    }

    private fun initAdapter() {
        with(viewBinding.redditRecyclerView) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = redditAdapter
            adapter = redditAdapter.withLoadStateHeaderAndFooter(
                header = LoadStateAdapter { redditAdapter.retry() },
                footer = LoadStateAdapter { redditAdapter.retry() }
            )
        }

        redditAdapter.addLoadStateListener(::loadStateListener)
        initAdapterRefreshListener()
    }

    private fun initAdapterRefreshListener() {
        viewLifecycleOwner.lifecycleScope.launch {
            redditAdapter.loadStateFlow
                // Use a state-machine to track LoadStates such that we only transition to
                // NotLoading from a RemoteMediator load if it was also presented to UI.
                .asMergedLoadStates()
                // Only emit when REFRESH changes, as we only want to react on loads replacing the
                // list.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where REFRESH completes
                .filter {
                    it.refresh is LoadState.NotLoading
                            && it.prepend == LoadState.NotLoading(endOfPaginationReached = true)
                }
                // Scroll to top is synchronous with UI updates, even if remote load was triggered.
                .collect {
                    if (it.prepend == LoadState.NotLoading(endOfPaginationReached = true))
                        viewBinding.redditRecyclerView.scrollToPosition(0)

                    viewBinding.swipeContainer.isRefreshing = false
                }
        }
    }

    private fun loadStateListener(loadState: CombinedLoadStates) = with(viewBinding) {
        val isEmptyCache =
            loadState.source.refresh is LoadState.NotLoading && redditAdapter.itemCount == 0

        val isFullCache = loadState.source.refresh is LoadState.NotLoading
                && redditAdapter.itemCount != 0

        val isRemoteRefreshFailed = loadState.mediator?.refresh is LoadState.Error

        textViewError.isVisible = loadState.refresh is LoadState.NotLoading
                && loadState.append.endOfPaginationReached && redditAdapter.itemCount < 1

        redditRecyclerView.isVisible = isFullCache || loadState.refresh is LoadState.NotLoading

        if (isRemoteRefreshFailed && isFullCache && dataFromCacheSnackBar?.isShown == false) {
            dataFromCacheSnackBar?.show()
        }

        progressIndicator.isVisible = loadState.mediator?.refresh is LoadState.Loading

        includeRetry.retryLinearLayout.isVisible = isRemoteRefreshFailed && isEmptyCache
    }

    protected open fun navigateToDetailsImage(item: RedditListItemImage) {
        val action = MainFragmentDirections.actionMainFragmentToDetailsImageFragment(item)
        findNavController().navigate(action)
    }

    protected open fun navigateToDetailSimple(item: RedditListSimpleItem) {
        val action = MainFragmentDirections.actionMainFragmentToDetailsPostSimpleFragment(item)
        findNavController().navigate(action)
    }

    protected open  fun navigateToSubredditFragment(subreddit: String) {
        val action = MainFragmentDirections.actionMainFragmentToSubredditFragment(subreddit)
        findNavController().navigate(action)
    }
}
