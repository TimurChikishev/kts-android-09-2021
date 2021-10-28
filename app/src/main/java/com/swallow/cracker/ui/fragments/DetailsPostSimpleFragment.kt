package com.swallow.cracker.ui.fragments

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.swallow.cracker.R
import com.swallow.cracker.databinding.FragmentDetailsBinding
import com.swallow.cracker.ui.model.RedditListSimpleItem
import com.swallow.cracker.ui.viewmodels.NetworkStatusViewModel
import com.swallow.cracker.ui.viewmodels.PostDetailViewModel
import com.swallow.cracker.utils.*
import kotlinx.coroutines.flow.collect

class DetailsPostSimpleFragment : Fragment(R.layout.fragment_details) {
    private val args by navArgs<DetailsPostSimpleFragmentArgs>()
    private val viewBinding by viewBinding(FragmentDetailsBinding::bind)
    private val viewModel: PostDetailViewModel by viewModels()
    private val networkStatusViewModel: NetworkStatusViewModel by viewModels()
    private lateinit var item: RedditListSimpleItem
    private var noInternetSnackBar: Snackbar? = null
    private var savedItem: MenuItem? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        item = args.post
        initTopAppBar()
        initNoInternetSnackBar()
        bindViewModel()
        initContent()
    }

    private fun initContent() = with(item) {
        setAvatar(communityIcon)
        setSubreddit(subreddit)
        setPublisher(author)
        setNumComments(numComments.toString())
        setSelfText(selftext)
        setCreated(time)
        setTitle(title)

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

    private fun initNoInternetSnackBar() {
        noInternetSnackBar = getNoInternetConnectionSnackBar(viewBinding.root)
        networkStatusViewModel.checkNetworkState()
    }

    private fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            networkStatusViewModel.isNoNetwork.collect {
                showNetworkState(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventMessage.collect {
                it?.let { msg -> showMessage(msg) }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.savePost.collect { saved ->
                setSavedStyle(saved ?: item.saved)
                saved?.let { item.setItemSaved(saved) }
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
        if (item.selftext.isEmpty()) {
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
            avatarImageView.setImageResource(R.drawable.ic_account_circle_24)
        } else {
            Glide.with(avatarImageView)
                .load(communityIcon)
                .circleCrop()
                .error(R.drawable.ic_account_circle_24)
                .into(avatarImageView)
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