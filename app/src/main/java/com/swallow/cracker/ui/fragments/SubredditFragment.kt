package com.swallow.cracker.ui.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.swallow.cracker.R
import com.swallow.cracker.databinding.FragmentSubredditBinding
import com.swallow.cracker.ui.adapters.SubredditFragmentAdapter
import com.swallow.cracker.ui.model.Subreddit
import com.swallow.cracker.ui.viewmodels.SharedSubredditViewModel
import com.swallow.cracker.ui.viewmodels.SubredditViewModel
import com.swallow.cracker.utils.bottomNavigationGone
import com.swallow.cracker.utils.sharedUrl
import com.swallow.cracker.utils.showMessage
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent
import timber.log.Timber

class SubredditFragment : Fragment(R.layout.fragment_subreddit), KoinComponent {

    companion object {
        private val tabTitles = listOf("Posts", "About")
    }

    private val viewBinding by viewBinding(FragmentSubredditBinding::bind)

    private val viewModel: SubredditViewModel by viewModel()
    private val sharedViewModel: SharedSubredditViewModel by activityViewModels()

    private val args by navArgs<SubredditFragmentArgs>()
    private val subredditName by lazy { args.subreddit }
    private val subredditPrefixName by lazy { "r/${subredditName}" }

    private var currentSubreddit: Subreddit? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        bottomNavigationGone()
        bindOfClick()
        bindViewModel()
        initTabBar()
        initTopAppBar()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.top_app_bar_subreddit, menu)
    }

    private fun initTopAppBar() = with(viewBinding) {
        (activity as AppCompatActivity).setSupportActionBar(includeAppBar.topAppBar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        includeAppBar.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.sharedAction -> {
                    startActivity(sharedUrl("https://www.reddit.com/$subredditPrefixName/"))
                    true
                }
                else -> false
            }
        }
    }

    private fun initTabBar() = with(viewBinding) {
        viewPager.adapter =
            SubredditFragmentAdapter(childFragmentManager, lifecycle)

        TabLayoutMediator(includeAppBar.headerTabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
            viewPager.setCurrentItem(tab.position, false)
        }.attach()
    }

    private fun bindOfClick() = with(viewBinding.includeAppBar) {
        topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        subscribeButton.setOnClickListener {
            currentSubreddit?.let { subreddit ->
                when (subscribeButton.text) {
                    resources.getString(R.string.join) -> {
                        viewModel.subscribeToSubreddit(subreddit)
                    }
                    resources.getString(R.string.joined) -> {
                        viewModel.unsubscribeFromSubreddit(subreddit)
                    }
                    else -> Timber.tag("ERROR").d("There is no such action for subscribe")
                }
            }
        }
    }

    private fun bindViewModel() = with(viewLifecycleOwner.lifecycleScope) {
        sharedViewModel.setQuery(subredditPrefixName)

        launchWhenStarted {
            subredditName?.let {
                viewModel.getSubredditInfo(it)
            }
        }

        launchWhenStarted {
            viewModel.eventMessage.collect(::showMessage)
        }

        launchWhenStarted {
            viewModel.subscribe.collect(::setSubscriberButtonStyle)
        }

        launchWhenStarted {
            viewModel.subredditInfo.collect(::setAppBarContent)
        }

        launchWhenStarted {
            viewModel.isLoading.collect {
                it?.let { isLoading ->
                    viewBinding.containerCoordinatorLayout.isVisible = !isLoading
                    viewBinding.progressBar.isVisible = isLoading
                }
            }
        }
    }

    private fun setSubscriberButtonStyle(action: Boolean) = with(viewBinding) {
        when (action) {
            true -> includeAppBar.subscribeButton.text = resources.getString(R.string.joined)
            false -> includeAppBar.subscribeButton.text = resources.getString(R.string.join)
        }
    }

    private fun setAppBarContent(subreddit: Subreddit?) = with(viewBinding.includeAppBar) {
        subreddit?.let {
            sharedViewModel.setSubredditInfo(it)
            currentSubreddit = it
            setSubredditAvatar(it.communityIcon, avatarImageView)
            nameTextView.text = it.displayNamePrefixed
            membersTextView.text = context?.getString(R.string.members, it.subscribers)
            onlineCountTextView.text = context?.getString(R.string.online, it.activeUserCount)

            setSubscriberButtonStyle(it.userIsSubscriber)
        }
    }

    private fun setSubredditAvatar(communityIcon: String, imageView: ImageView) {
        Glide.with(this@SubredditFragment)
            .load(communityIcon)
            .circleCrop()
            .error(R.drawable.ic_subreddit_24)
            .into(imageView)
    }
}