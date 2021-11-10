package com.swallow.cracker.ui.fragments

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.swallow.cracker.R
import com.swallow.cracker.databinding.FragmentSubscriptionsBinding
import com.swallow.cracker.ui.adapters.LoadStateAdapter
import com.swallow.cracker.ui.adapters.search.delegates.EventDelegateListAdapter
import com.swallow.cracker.ui.adapters.subscriptions.SubscriptionsPagingDataAdapter
import com.swallow.cracker.ui.model.Subreddit
import com.swallow.cracker.ui.viewmodels.SubscriptionsViewModel
import com.swallow.cracker.utils.bottomNavigationVisible
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class SubscriptionsFragment : Fragment(R.layout.fragment_subscriptions) {

    private val viewModel: SubscriptionsViewModel by viewModel()
    private val listAdapter by lazy { SubscriptionsPagingDataAdapter() }
    private val viewBinding by viewBinding(FragmentSubscriptionsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        bottomNavigationVisible()
        bindViewModel()
        initSwipeRefreshLayout()
        initAdapter()
        bindingOfClick()
        initTopAppBar()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.top_app_bar_subscriptions, menu)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun bindViewModel() = with(viewLifecycleOwner.lifecycleScope) {
        launchWhenCreated { viewModel.subscriptions.collectLatest { listAdapter.submitData(it) } }
    }

    private fun initSwipeRefreshLayout() = with(viewBinding) {
        swipeContainer.setOnRefreshListener(listAdapter::refresh)
    }

    private fun initTopAppBar() = with(viewBinding) {
        (activity as AppCompatActivity).setSupportActionBar(includeAppBar.topAppBar)

        includeAppBar.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.searchAction -> {
                    navigateToSearchFragment()
                    true
                }
                else -> false
            }
        }
    }

    private fun bindingOfClick() {
        viewBinding.includeRetry.buttonRetry.setOnClickListener { listAdapter.retry() }

        listAdapter.attachEventDelegate(object : EventDelegateListAdapter{
            override fun removeItem(item: Any) = Unit

            override fun onItemClick(item: Any) {
                when(item){
                    is Subreddit -> navigateToSubredditFragment(item.displayName)
                    else -> Timber.tag("ERROR").d("$item has no navigation action")
                }
            }

        })
    }

    private fun initAdapter() {
        with(viewBinding.recyclerView) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = listAdapter
            adapter = listAdapter.withLoadStateHeaderAndFooter(
                header = LoadStateAdapter { listAdapter.retry() },
                footer = LoadStateAdapter { listAdapter.retry() }
            )
        }

        listAdapter.addLoadStateListener(::loadStateListener)
        initAdapterRefreshListener()
    }

    private fun initAdapterRefreshListener() {
        viewLifecycleOwner.lifecycleScope.launch {
            listAdapter.loadStateFlow
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
                        viewBinding.recyclerView.scrollToPosition(0)

                    viewBinding.swipeContainer.isRefreshing = false
                }
        }
    }

    private fun loadStateListener(loadState: CombinedLoadStates) = with(viewBinding) {
        val isEmptyCache =
            loadState.source.refresh is LoadState.NotLoading && listAdapter.itemCount == 0

        val isFullCache = loadState.source.refresh is LoadState.NotLoading
                && listAdapter.itemCount != 0

        val isRemoteRefreshFailed = loadState.mediator?.refresh is LoadState.Error

        textViewError.isVisible = loadState.refresh is LoadState.NotLoading
                && loadState.append.endOfPaginationReached && listAdapter.itemCount < 1

        recyclerView.isVisible = isFullCache || loadState.refresh is LoadState.NotLoading


        includeAppBar.progressIndicator.isVisible = loadState.mediator?.refresh is LoadState.Loading

        includeRetry.retryLinearLayout.isVisible = isRemoteRefreshFailed && isEmptyCache
    }

    private fun navigateToSubredditFragment(subreddit: String){
        val action = SubscriptionsFragmentDirections.actionSubscriptionsFragmentToSubredditFragment(subreddit)
        findNavController().navigate(action)
    }

    private fun navigateToSearchFragment() {
        val uri = Uri.parse("android-app://com.swallow.cracker/searchFragment")
        findNavController().navigate(uri)
    }
}