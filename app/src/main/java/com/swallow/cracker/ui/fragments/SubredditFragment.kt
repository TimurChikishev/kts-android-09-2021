package com.swallow.cracker.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
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
import com.swallow.cracker.ui.viewmodels.SubredditViewModel
import com.swallow.cracker.utils.bottomNavigationGone
import com.swallow.cracker.utils.showMessage
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class SubredditFragment : Fragment(R.layout.fragment_subreddit) {

    companion object {
        private val tabTitles = listOf("Posts")
    }

    private val args by navArgs<SubredditFragmentArgs>()
    private val subredditName by lazy { args.subreddit }
    private val subredditPrefixName by lazy { "r/${subredditName}" }
    private val viewModel: SubredditViewModel by viewModel()
    private val viewBinding by viewBinding(FragmentSubredditBinding::bind)
    private var currentSubreddit: Subreddit? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavigationGone()
        bindOfClick()
        bindViewModel()
        initTabBar()
    }

    private fun initTabBar() = with(viewBinding) {
        viewPager.adapter = SubredditFragmentAdapter(subredditPrefixName, childFragmentManager, lifecycle)

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
                    resources.getString(R.string.join) -> viewModel.subscribeToSubreddit(subreddit)
                    resources.getString(R.string.joined) -> viewModel.unsubscribeFromSubreddit(subreddit)
                    else -> Timber.tag("ERROR").d("There is no such action for subscribe")
                }
            }
        }
    }

    private fun bindViewModel() = with(viewLifecycleOwner.lifecycleScope) {
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
                it?.let {
                    viewBinding.containerCoordinatorLayout.isVisible = true
                    viewBinding.progressBar.isVisible = false
                }
            }
        }
    }

    private fun setSubscriberButtonStyle(action: Boolean) = with(viewBinding) {
        when(action){
            true -> includeAppBar.subscribeButton.text = resources.getString(R.string.joined)
            false -> includeAppBar.subscribeButton.text = resources.getString(R.string.join)
        }
    }


    private fun setAppBarContent(subreddit: Subreddit?) = with(viewBinding.includeAppBar) {
        subreddit?.let {
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