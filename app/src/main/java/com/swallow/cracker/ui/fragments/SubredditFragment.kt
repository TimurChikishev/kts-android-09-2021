package com.swallow.cracker.ui.fragments

import android.graphics.drawable.Drawable
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
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.swallow.cracker.R
import com.swallow.cracker.databinding.FragmentSubredditBinding
import com.swallow.cracker.ui.model.Subreddit
import com.swallow.cracker.ui.viewmodels.SubredditViewModel
import com.swallow.cracker.utils.bottomNavigationGone
import kotlinx.coroutines.flow.collect

class SubredditFragment : Fragment(R.layout.fragment_subreddit) {

    private val args by navArgs<SubredditFragmentArgs>()
    private val currentSubreddit by lazy { args.subreddit }
    private val subredditViewModel: SubredditViewModel by viewModels()
    private val viewBinding by viewBinding(FragmentSubredditBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavigationGone()
        bindOfClick()
        setAppBarContent(currentSubreddit)
        bindViewModel()
    }

    private fun bindOfClick() = with(viewBinding) {
        includeAppBar.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun bindViewModel() = with(viewLifecycleOwner.lifecycleScope) {
        launchWhenStarted {
            currentSubreddit?.displayName?.let {
                subredditViewModel.getSubredditInfo(it)
            }
        }

        launchWhenStarted {
            subredditViewModel.subredditInfo.collect(::setAppBarContent)
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

    private fun setSubredditAvatar(communityIcon: String, imageView: ImageView) = with(viewBinding) {
        Glide.with(this@SubredditFragment)
            .load(communityIcon)
            .circleCrop()
            .error(R.drawable.ic_subreddit_24)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar.isVisible = false
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar.isVisible = false
                    containerCoordinatorLayout.isVisible = true
                    return false
                }
            })
            .into(imageView)
    }
}