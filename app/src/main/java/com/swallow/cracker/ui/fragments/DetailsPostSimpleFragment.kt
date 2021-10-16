package com.swallow.cracker.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.swallow.cracker.R
import com.swallow.cracker.databinding.FragmentDetailsBinding
import com.swallow.cracker.ui.model.RedditListSimpleItem
import com.swallow.cracker.ui.viewmodels.PostViewModel
import com.swallow.cracker.utils.setSavedStatus
import com.swallow.cracker.utils.showMessage
import com.swallow.cracker.utils.updateScore

class DetailsPostSimpleFragment : Fragment(R.layout.fragment_details) {
    private val args by navArgs<DetailsPostSimpleFragmentArgs>()
    private val viewBinding by viewBinding(FragmentDetailsBinding::bind)
    private val viewModel: PostViewModel by viewModels()
    private lateinit var item: RedditListSimpleItem

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        item = args.post
        bindViewModel()
        initContent()
    }

    private fun initContent() = with(item) {
        setAvatar(R.drawable.ic_face_24)
        setSubreddit(subreddit)
        setPublisher(author)
        setNumComments(numComments.toString())
        setSelfText(selftext)
        setCreated(time)
        setTitle(title)

        bindingOfClicks()
        setScore()
    }

    private fun bindViewModel() {
        viewModel.eventMessage.observe(viewLifecycleOwner, { it?.let { msg -> showMessage(msg) } })

        viewModel.savePost.observe(viewLifecycleOwner, {
            setSavedStyle(it?.flag ?: item.saved)
            it?.let { item.setSavedStatus(it.flag) }
        })

        viewModel.savePostIsClickable.observe(viewLifecycleOwner, {
            viewBinding.savedImageView.isClickable = it
        })

        viewModel.votePost.observe(viewLifecycleOwner, {
            it?.let { item.updateScore(it.flag) }
            setScore()
        })

        viewModel.votePostIsClickable.observe(viewLifecycleOwner, {
            viewBinding.likesImageView.isClickable = it
            viewBinding.dislikesImageView.isClickable = it
        })
    }

    private fun bindingOfClicks() = with(viewBinding) {
        // button save/unsave
        savedImageView.setOnClickListener {
            when (!item.saved) {
                true -> viewModel.savePost(category = null, id = item.t3_id)
                false -> viewModel.unSavePost(id = item.t3_id)
            }
        }

        // button back
        backImageView.setOnClickListener {
            findNavController().popBackStack()
        }

        // button like
        likesImageView.setOnClickListener {
            viewModel.votePost(item, true)
        }

        // button dislike
        dislikesImageView.setOnClickListener {
            viewModel.votePost(item, false)
        }

        // shared to internet
        shareImageView.setOnClickListener {
            val intent = viewModel.shared(item.url)
            startActivity(intent)
        }
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

    private fun setAvatar(res: Int) {
        viewBinding.avatarImageView.setImageResource(res)
    }

    // setting the style for save/unsave buttons
    private fun setSavedStyle(boolean: Boolean) = with(viewBinding) {
        when (boolean) {
            true -> {
                val color = ContextCompat.getColor(requireContext(), R.color.red)
                savedImageView.setColorFilter(color)
            }
            false -> {
                savedImageView.colorFilter = null
            }
        }
    }

    // setting the style for rating buttons
    private fun setScore() = with(viewBinding) {
        val color = ContextCompat.getColor(requireContext(), R.color.red)

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
}