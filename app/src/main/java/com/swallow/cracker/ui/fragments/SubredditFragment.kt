package com.swallow.cracker.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
import kotlinx.coroutines.flow.collect

class SubredditFragment : Fragment(R.layout.fragment_subreddit) {

    companion object {
        private val tabTitles = listOf("Posts")
    }

    private val args by navArgs<SubredditFragmentArgs>()
    private val subredditName by lazy { args.subreddit }
    private val subredditPrefixName by lazy { "r/${subredditName}" }
    private val subredditViewModel: SubredditViewModel by viewModels()
    private val viewBinding by viewBinding(FragmentSubredditBinding::bind)

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

    private fun bindOfClick() = with(viewBinding) {
        includeAppBar.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun bindViewModel() = with(viewLifecycleOwner.lifecycleScope) {
        launchWhenStarted {
            subredditName?.let {
                subredditViewModel.getSubredditInfo(it)
            }
        }

        launchWhenStarted {
            subredditViewModel.subredditInfo.collect(::setAppBarContent)
        }

        launchWhenStarted {
            subredditViewModel.isLoading.collect {
                it?.let {
                    viewBinding.containerCoordinatorLayout.isVisible = true
                    viewBinding.progressBar.isVisible = false
                }
            }
        }
    }

    private fun setAppBarContent(subreddit: Subreddit?) = with(viewBinding.includeAppBar) {
        subreddit ?: return@with

        setSubredditAvatar(subreddit.communityIcon, avatarImageView)
        nameTextView.text = subreddit.displayNamePrefixed
        membersTextView.text = context?.getString(R.string.members, subreddit.subscribers)
        onlineCountTextView.text = context?.getString(R.string.online, subreddit.activeUserCount)

        if (subreddit.userIsSubscriber == true)
            bottomJoin.text = resources.getString(R.string.joined)
    }

    private fun setSubredditAvatar(communityIcon: String, imageView: ImageView) {
        Glide.with(this@SubredditFragment)
            .load(communityIcon)
            .circleCrop()
            .error(R.drawable.ic_subreddit_24)
            .into(imageView)
    }
}