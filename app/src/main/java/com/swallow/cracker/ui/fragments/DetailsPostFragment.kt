package com.swallow.cracker.ui.fragments

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.swallow.cracker.R
import com.swallow.cracker.data.model.listing.RedditChildrenPreview
import com.swallow.cracker.databinding.FragmentDetailsBinding
import com.swallow.cracker.ui.model.RedditItem
import com.swallow.cracker.ui.viewmodels.NetworkStatusViewModel
import com.swallow.cracker.ui.viewmodels.PostDetailViewModel
import com.swallow.cracker.utils.*
import kotlinx.coroutines.flow.collect
import timber.log.Timber

open class DetailsPostFragment : Fragment(R.layout.fragment_details) {

    private val networkStatusViewModel: NetworkStatusViewModel by viewModels()
    private val viewBinding by viewBinding(FragmentDetailsBinding::bind)
    private val viewModel: PostDetailViewModel by viewModels()
    private lateinit var item: RedditItem
    private var noInternetSnackBar: Snackbar? = null
    private var savedItem: MenuItem? = null

    protected fun setItem(item: RedditItem) {
        this.item = item
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavigationGone()
        initTopAppBar()
        initNoInternetSnackBar()
        bindViewModel()
        initContent()
    }

    private fun initNoInternetSnackBar() {
        noInternetSnackBar = getNoInternetConnectionSnackBar(viewBinding.root)
        networkStatusViewModel.checkNetworkState()
    }

    private fun initContent() = with(item) {
        setAvatar(communityIcon)
        setSubreddit(subreddit)
        setPublisher(author)
        setNumComments(numComments.toString())
        setSelfText(selfText)
        setCreated(time)
        setTitle(title)
        setThumbnail(thumbnail = thumbnail(), preview = preview())

        bindingOfClicks()
        setScore()
    }

    private fun initTopAppBar() {
        savedItem = viewBinding.topAppBar.menu.findItem(R.id.saved)

        viewBinding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        viewBinding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.saved -> {
                    when (!item.saved) {
                        true -> viewModel.savePost(item)
                        false -> viewModel.unSavePost(item)
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            networkStatusViewModel.isNoNetwork.collect {
                showNetworkState(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventMessage.collect(::showMessage)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.savePost.collect { saved ->
                setSavedStyle(saved)
                item.saved = saved
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.savePostIsClickable.collect {
                savedItem?.isEnabled = it
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.votePost.collect { setScore() }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.votePostIsClickable.collect {
                viewBinding.likesImageView.isClickable = it
                viewBinding.dislikesImageView.isClickable = it
            }
        }
    }

    private fun showNetworkState(isNoInternet: Boolean) {
        when (isNoInternet) {
            true -> noInternetSnackBar?.show()
            false -> noInternetSnackBar?.dismiss()
        }
    }

    private fun bindingOfClicks() = with(viewBinding) {
        // button like
        likesImageView.setOnClickListener {
            viewModel.votePost(item, true)
        }

        // button dislike
        dislikesImageView.setOnClickListener {
            viewModel.votePost(item, false)
        }

        // shared to internet
        shareImageView.setOnClickListener { startActivity(sharedUrl(item.url)) }
    }

    private fun setTitle(title: String) {
        viewBinding.titleTextView.text = title
    }

    private fun setCreated(created: String) {
        viewBinding.createdTextView.text = created
    }

    private fun setSubreddit(subreddit: String) {
        viewBinding.subredditTextView.text = subreddit
    }

    private fun setSelfText(selfText: String) {
        if (item.selfText.isEmpty()) {
            viewBinding.selfTextView.visibility = View.GONE
            return
        }
        viewBinding.selfTextView.text = selfText
    }

    private fun setNumComments(num: String) {
        viewBinding.numCommentsTextView.text = num
    }

    private fun setPublisher(author: String) {
        viewBinding.publisherTextView.text = getString(R.string.posted_by, author)
    }

    private fun setAvatar(communityIcon: String?) = with(viewBinding) {
        if (communityIcon.isNullOrEmpty()) {
            avatarImageView.setImageResource(R.drawable.ic_subreddit_24)
        } else {
            Glide.with(avatarImageView)
                .load(communityIcon)
                .circleCrop()
                .error(R.drawable.ic_subreddit_24)
                .into(avatarImageView)
        }
    }

    private fun setThumbnail(thumbnail: String?, preview: RedditChildrenPreview?) =
        with(viewBinding) {
            if (!thumbnail.isNullOrEmpty()) {
                try {
                    val url = preview?.let { preview.images[0].source.urlNew } ?: thumbnail
                    Glide.with(thumbnailImageView)
                        .load(url)
                        .placeholder(R.drawable.ic_cookie_24)
                        .error(R.drawable.ic_error_24)
                        .thumbnail(Glide.with(thumbnailImageView).load(thumbnail))
                        .into(thumbnailImageView)
                } catch (exception: Throwable) {
                    Timber.tag("ERROR").d(exception)
                }
            } else {
                thumbnailImageView.visibility = View.GONE
            }
        }

    // setting the style for save/unsave buttons
    private fun setSavedStyle(boolean: Boolean) = when (boolean) {
        true -> savedItem?.icon?.setTint(getColor(requireContext(), R.color.red))
        false -> savedItem?.icon?.setTint(
            resolveColorAttr(
                requireContext(),
                R.attr.colorControlNormal
            )
        )
    }

    // setting the style for rating buttons
    private fun setScore() = with(viewBinding) {
        val color = getColor(requireContext(), R.color.red)

        when (item.likes) {
            true -> {
                likesImageView.setColorFilter(color)
                dislikesImageView.colorFilter = null
            }
            false -> {
                dislikesImageView.setColorFilter(color)
                likesImageView.colorFilter = null
            }
            null -> {
                dislikesImageView.colorFilter = null
                likesImageView.colorFilter = null
            }
        }

        scoreTextView.text = item.score.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        noInternetSnackBar = null
        savedItem = null
    }
}