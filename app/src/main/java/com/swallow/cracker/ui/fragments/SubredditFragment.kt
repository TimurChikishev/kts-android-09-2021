package com.swallow.cracker.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.swallow.cracker.R
import com.swallow.cracker.databinding.FragmentSubredditBinding
import com.swallow.cracker.ui.model.Subreddit
import com.swallow.cracker.ui.viewmodels.SubredditViewModel
import com.swallow.cracker.utils.bottomNavigationGone
import kotlinx.coroutines.flow.collect
import timber.log.Timber

class SubredditFragment : Fragment(R.layout.fragment_subreddit) {

    private val args by navArgs<SubredditFragmentArgs>()
    private val subredditViewModel: SubredditViewModel by viewModels()
    private val viewBinding by viewBinding(FragmentSubredditBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavigationGone()
        setAppBarContent(args.subreddit)
        bindViewModel()
    }

    private fun bindViewModel() = with(viewLifecycleOwner.lifecycleScope) {
        Timber.tag("TAG").d("args.subreddit.name ${args.subreddit.displayName}")
        launchWhenStarted {
            subredditViewModel.getSubredditInfo(args.subreddit.displayName)
        }

        launchWhenStarted {
            subredditViewModel.subredditInfo.collect(::setAppBarContent)
        }
    }

    private fun setAppBarContent(subreddit: Subreddit?) = with(viewBinding.includeAppBar) {
        subreddit ?: return@with
        Timber.tag("TAG").d("setAppBarContent $subreddit")

        setSubredditAvatar(subreddit.communityIcon, avatarImageView)
        setBannerImage(subreddit.bannerImg, bannerImageView)
        nameTextView.text = subreddit.displayNamePrefixed
        membersTextView.text = context?.getString(R.string.members, subreddit.subscribers)
        onlineCountTextView.text = context?.getString(R.string.online, subreddit.activeUserCount)

        if (subreddit.userIsSubscriber == true)
            bottomJoin.text = resources.getString(R.string.joined)
    }

    private fun setBannerImage(bannerImg: String?, bannerImageView: ImageView) {
        Glide.with(this@SubredditFragment)
            .load(bannerImg)
            .circleCrop()
            .error(R.drawable.header_image)
            .into(bannerImageView)
    }

    private fun setSubredditAvatar(communityIcon: String, imageView: ImageView) {
        Glide.with(this@SubredditFragment)
            .load(communityIcon)
            .circleCrop()
            .error(R.drawable.ic_subreddit_256dp)
            .into(imageView)
    }
}