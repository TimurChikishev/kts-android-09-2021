package com.swallow.cracker.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.swallow.cracker.R
import com.swallow.cracker.databinding.FragmentListBinding
import com.swallow.cracker.ui.adapters.LoadStateAdapter
import com.swallow.cracker.ui.adapters.listing.delegates.ComplexDelegateAdapterClick
import com.swallow.cracker.ui.adapters.listing.delegates.ComplexDelegatesRedditListAdapter
import com.swallow.cracker.ui.adapters.listing.delegates.items.RedditListItemImageDelegateAdapter
import com.swallow.cracker.ui.adapters.listing.delegates.items.RedditListPlaceholderItem
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
import timber.log.Timber
import java.util.*

open class ListFragment : Fragment(R.layout.fragment_list) {
    protected val redditViewModel: RedditListViewModel by viewModel()
    protected val redditAdapter: ComplexDelegatesRedditListAdapter by lazy {
        ComplexDelegatesRedditListAdapter.Builder()
            .add(RedditListPlaceholderItem()) // placeholder must be first added
            .add(RedditListSimpleItemDelegateAdapter())
            .add(RedditListItemImageDelegateAdapter())
            .build()
    }

    private val singleItems = arrayOf("Best", "Hot", "New", "Top", "Rising")
    private val viewBinding by viewBinding(FragmentListBinding::bind)
    private var dataFromCacheSnackBar: Snackbar? = null

    protected fun setQuery(query: String) {
        redditViewModel.setQuery(query)
    }

    private fun setSorted(sorted: String) {
        redditViewModel.setSorted(sorted)
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

    private fun showDialogChoiceItems() {
        MaterialAlertDialogBuilder(requireContext())
            .setSingleChoiceItems(singleItems, redditViewModel.checkedItem.value) { dialog, which ->
                redditViewModel.updateCheckedItem(which)
                setSortedPostsTextViewTitle(which)
                setSorted(singleItems[which].lowercase(Locale.getDefault()))

                dialog.dismiss()
            }.show()
    }

    private fun setSortedPostsTextViewTitle(which: Int = 0) {
        val itemTitle = singleItems[which].uppercase(Locale.getDefault())
        val title = resources.getString(R.string.sorted_posts, itemTitle)
        viewBinding.includeHeader.sortedByTextView.text = title
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    protected open fun bindingViewModel() = with(viewLifecycleOwner.lifecycleScope) {
        launchWhenCreated {
            redditViewModel.listingItems.collectLatest { redditAdapter.submitData(it) }
        }

        launchWhenStarted { redditViewModel.eventMessage.collect(::showMessage) }

        launchWhenStarted {
            redditViewModel.checkedItem.collect(::setSortedPostsTextViewTitle)
        }
    }

    private fun bindingOfClick() {
        viewBinding.includeRetry.buttonRetry.setOnClickListener { redditAdapter.retry() }

        viewBinding.includeHeader.container.setOnClickListener {
            showDialogChoiceItems()
        }

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
                else -> Timber.tag("ERROR").d("$item cannot have navigation!")
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

        viewBinding.redditRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                with(viewBinding.includeHeader) {
                    if (dy > 0 && container.isShown) {
                        recyclerView.setPadding(0, 0, 0, 0)
                        container.visibility = View.GONE
                    } else if (dy < 0) {
                        recyclerView.setPadding(0, 80, 0, 0)
                        container.visibility = View.VISIBLE
                    }
                }
            }
        })

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

    protected open fun navigateToSubredditFragment(subreddit: String) {
        val action = MainFragmentDirections.actionMainFragmentToSubredditFragment(subreddit)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dataFromCacheSnackBar = null
    }
}
