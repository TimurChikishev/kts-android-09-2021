package com.swallow.cracker.ui.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.swallow.cracker.R
import com.swallow.cracker.databinding.FragmentHomeBinding
import com.swallow.cracker.ui.adapters.LoadStateAdapter
import com.swallow.cracker.ui.adapters.delegates.ComplexDelegateAdapterClick
import com.swallow.cracker.ui.adapters.delegates.ComplexDelegatesRedditListAdapter
import com.swallow.cracker.ui.adapters.delegates.items.RedditListItemImageDelegateAdapter
import com.swallow.cracker.ui.adapters.delegates.items.RedditListSimpleItemDelegateAdapter
import com.swallow.cracker.ui.model.RedditItem
import com.swallow.cracker.ui.model.RedditListItemImage
import com.swallow.cracker.ui.model.RedditListSimpleItem
import com.swallow.cracker.ui.viewmodels.NetworkStatusViewModel
import com.swallow.cracker.ui.viewmodels.RedditListViewModel
import com.swallow.cracker.utils.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.fragment_home) {
    private val redditViewModel: RedditListViewModel by viewModels()
    private val networkStatusViewModel: NetworkStatusViewModel by viewModels()
    private val viewBinding by viewBinding(FragmentHomeBinding::bind)
    private var redditAdapter: ComplexDelegatesRedditListAdapter by autoCleared()
    private var noInternetSnackBar: Snackbar? = null
    private var dataFromCacheSnackBar: Snackbar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        initNoInternetSnackBar()
        initAdapter()
        bindingViewModel()
        bindingOfClick()
        initSwipeRefreshLayout()
        initTopAppBar()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.top_app_bar_home, menu)

        viewBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewBinding.searchView.clearFocus()
                    viewBinding.redditRecyclerView.scrollToPosition(0)
                    redditViewModel.searchPosts(it)
                    redditAdapter.refresh()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun initTopAppBar() {
        (activity as AppCompatActivity).setSupportActionBar(viewBinding.topAppBar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        viewBinding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.profile -> {
                    navigateToProfileFragment()
                    true
                }
                else -> false
            }
        }
    }

    private fun initSwipeRefreshLayout() = with(viewBinding) {
        swipeContainer.setOnRefreshListener(redditAdapter::refresh)
    }

    private fun initNoInternetSnackBar() = with(viewBinding) {
        dataFromCacheSnackBar = getDataFormCacheSnackBar(root)
        noInternetSnackBar = getNoInternetConnectionSnackBar(root)
        networkStatusViewModel.checkNetworkState()
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

            override fun shared(url: String): Unit = startActivity(sharedUrl(url))
        })
    }

    private fun bindingViewModel() = with(viewLifecycleOwner.lifecycleScope) {
        launchWhenStarted { redditViewModel.items.collectLatest { redditAdapter.submitData(it) } }

        launchWhenStarted { networkStatusViewModel.isNoNetwork.collect(::showNetworkState) }

        launchWhenStarted { redditViewModel.eventMessage.collect { it?.let { showMessage(it) } } }
    }

    private fun showNetworkState(isNoInternet: Boolean) = when (isNoInternet) {
        true -> noInternetSnackBar?.show()
        false -> noInternetSnackBar?.dismiss()
    }

    private fun initAdapter() {
        redditAdapter = ComplexDelegatesRedditListAdapter.Builder()
            .add(RedditListSimpleItemDelegateAdapter())
            .add(RedditListItemImageDelegateAdapter())
            .build()

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
                // Only react to cases where REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                // Scroll to top is synchronous with UI updates, even if remote load was triggered.
                .collect {
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

    private fun navigateToDetailsImage(item: RedditListItemImage) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailsImageFragment(item)
        findNavController().navigate(action)
    }

    private fun navigateToDetailSimple(item: RedditListSimpleItem) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailsPostSimple(item)
        findNavController().navigate(action)
    }

    private fun navigateToProfileFragment() {
        val action = HomeFragmentDirections.actionHomeFragmentToProfileFragment()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        noInternetSnackBar = null
        dataFromCacheSnackBar = null
    }
}