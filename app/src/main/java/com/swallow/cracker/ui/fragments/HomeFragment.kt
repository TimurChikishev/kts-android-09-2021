package com.swallow.cracker.ui.fragments

import android.os.Bundle
import android.view.View
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
import com.swallow.cracker.ui.viewmodels.PostViewModel
import com.swallow.cracker.ui.viewmodels.RedditListViewModel
import com.swallow.cracker.utils.autoCleared
import com.swallow.cracker.utils.getDataFormCacheSnackBar
import com.swallow.cracker.utils.getNoInternetConnectionSnackBar
import com.swallow.cracker.utils.showMessage
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

class HomeFragment : Fragment(R.layout.fragment_home) {
    private val redditViewModel: RedditListViewModel by viewModels()
    private val postViewModel: PostViewModel by viewModels()
    private val networkStatusViewModel: NetworkStatusViewModel by viewModels()
    private val viewBinding by viewBinding(FragmentHomeBinding::bind)
    private var redditAdapter: ComplexDelegatesRedditListAdapter by autoCleared()
    private var dataFromCache: Boolean? = null

    private var noInternetSnackBar: Snackbar? = null
    private var dataFromCacheSnackBar: Snackbar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNoInternetSnackBar()
        initAdapter()
        bindingViewModel()
        bindingOfClick()
    }

    private fun initNoInternetSnackBar() = with(viewBinding) {
        dataFromCacheSnackBar = getDataFormCacheSnackBar(root)
        noInternetSnackBar = getNoInternetConnectionSnackBar(root)
        networkStatusViewModel.checkNetworkState()
    }

    private fun bindingOfClick() {

        redditAdapter.attachClickDelegate(object : ComplexDelegateAdapterClick {

            // TODO: Переделать когда появится ROOM
            override fun onVoteClick(position: Int, likes: Boolean) {
                val item = redditAdapter.snapshot()[position] ?: return
                postViewModel.votePost(item = item, likes = likes, position = position)
            }

            // TODO: Переделать когда появится ROOM
            override fun onSavedClick(
                category: String?,
                id: String,
                position: Int?,
                saved: Boolean
            ) {
                if (saved)
                    postViewModel.savePost(category = category, id = id, position = position)
                else
                    postViewModel.unSavePost(id = id, position = position)
            }

            override fun navigateTo(item: RedditItem) {
                when (item) {
                    is RedditListSimpleItem -> {
                        val action =
                            HomeFragmentDirections.actionHomeFragmentToDetailsPostSimple(item)
                        findNavController().navigate(action)
                    }
                    is RedditListItemImage -> {
                        val action =
                            HomeFragmentDirections.actionHomeFragmentToDetailsImageFragment(item)
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
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            redditViewModel.items.collectLatest {
                redditAdapter.submitData(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            networkStatusViewModel.isNoNetwork.collect(::showNetworkState)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            postViewModel.eventMessage.collect { it?.let { msg -> showMessage(msg) } }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            postViewModel.votePost.collect {
                it?.let {
                    it.position?.let { position ->
                        redditAdapter.onLikeClick(position = position, likes = it.flag)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            postViewModel.savePost.collect {
                it?.let {
                    it.position?.let { position ->
                        redditAdapter.onSavedClick(
                            position = position,
                            it.flag
                        )
                    }
                }
            }
        }
    }

    private fun showNetworkState(isNoInternet: Boolean) {
        when (isNoInternet) {
            true -> noInternetSnackBar?.show()
            false -> {
                noInternetSnackBar?.dismiss()
                if (dataFromCache == true) {
                    redditAdapter.refresh()
                    dataFromCache = false
                }
            }
        }
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

        viewBinding.buttonRetry.setOnClickListener { redditAdapter.retry() }

        redditAdapter.addLoadStateListener(::loadStateListener)
    }

    private fun loadStateListener(loadState: CombinedLoadStates) = with(viewBinding) {
        val isEmptyList = loadState.refresh is LoadState.NotLoading
                && loadState.append.endOfPaginationReached && redditAdapter.itemCount == 0

        val isEmptyCache = loadState.source.refresh is LoadState.NotLoading && redditAdapter.itemCount == 0

        val isFullCache = loadState.source.refresh is LoadState.NotLoading
                && redditAdapter.itemCount != 0

        val isRemoteRefreshFailed = loadState.mediator?.refresh is LoadState.Error

        textViewError.isVisible = isEmptyList

        redditRecyclerView.isVisible = isFullCache || loadState.refresh is LoadState.NotLoading

        if (isRemoteRefreshFailed && isFullCache && dataFromCacheSnackBar?.isShown == false) {
            dataFromCache = true
            dataFromCacheSnackBar?.show()
        }

        progressBar.isVisible = loadState.mediator?.refresh is LoadState.Loading

        buttonRetry.isVisible = isRemoteRefreshFailed && isEmptyCache
    }

    override fun onDestroyView() {
        super.onDestroyView()
        noInternetSnackBar = null
        dataFromCacheSnackBar = null
    }
}