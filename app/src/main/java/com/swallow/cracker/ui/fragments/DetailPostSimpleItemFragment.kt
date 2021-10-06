package com.swallow.cracker.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.swallow.cracker.R
import com.swallow.cracker.databinding.FragmentDetailsBinding
import com.swallow.cracker.ui.modal.LoadState
import com.swallow.cracker.ui.modal.RedditListSimpleItem
import com.swallow.cracker.ui.modal.VoteState
import com.swallow.cracker.ui.viewmodels.PostViewModel
import com.swallow.cracker.utils.setSavedStatus
import com.swallow.cracker.utils.updateScore

class DetailPostSimpleItemFragment : Fragment(R.layout.fragment_details) {
    private val args by navArgs<DetailPostSimpleItemFragmentArgs>()
    private val viewBinding by viewBinding(FragmentDetailsBinding::bind)
    private val viewModel: PostViewModel by viewModels()
    private lateinit var item: RedditListSimpleItem

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        item = args.post
        bindViewModel()
        initContent()
    }

    private fun bindViewModel() {
        viewModel.savePost.observe(viewLifecycleOwner, { state ->
            when (state) {
                is LoadState.OnDefault -> {
                    setSaved()
                }
                is LoadState.OnSuccess -> {
                    item.setSavedStatus(!item.saved)
                    setSaved()
                }
                is LoadState.OnError<*> -> {
                    when (state.message) {
                        is Int -> Toast.makeText(context, getString(state.message), Toast.LENGTH_SHORT).show()
                        is String -> Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        viewModel.votePost.observe(viewLifecycleOwner, { state ->
            when(state) {
                is VoteState.OnSuccess -> {
                    item.updateScore(state.likes)
                    setScore()
                }
                is VoteState.OnError<*> -> {
                    when (state.message) {
                        is Int -> Toast.makeText(context, getString(state.message),Toast.LENGTH_SHORT).show()
                        is String -> Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is VoteState.OnDefault -> {}
            }
        })
    }

    @SuppressLint("StringFormatMatches")
    private fun initContent() = with(viewBinding) {

        if (item.selftext.isEmpty())
            selfTextView.visibility = View.GONE

        thumbnailImageView.visibility = View.GONE

        avatarImageView.setImageResource(R.drawable.ic_face_24)
        postedByTextView.text = getString(R.string.posted_by, item.author, 0) // StringFormatMatches
        numCommentsTextView.text = item.numComments.toString()
        selfTextView.text = item.selftext
        authorTextView.text = item.subreddit
        createdTextView.text = item.time
        titleTextView.text = item.title

        bindingOfClicks()
        setScore()
    }

    private fun bindingOfClicks() = with(viewBinding) {
        // button save/unsave
        favoriteImageView.setOnClickListener {
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
            viewModel.vote(item, true)
        }

        // button dislike
        dislikesImageView.setOnClickListener {
            viewModel.vote(item, false)
        }

        shareImageView.setOnClickListener {
            val intent = viewModel.shared(item.url)
            startActivity(intent)
        }
    }

    // setting the style for save/unsave buttons
    private fun setSaved() = with(viewBinding) {
        when (item.saved) {
            true -> {
                val color = ContextCompat.getColor(requireContext(), R.color.red)
                favoriteImageView.setColorFilter(color)
            }
            false -> {
                favoriteImageView.colorFilter = null
            }
        }
    }

    // setting the style for rating buttons
    private fun setScore() = with(viewBinding) {
        val color = ContextCompat.getColor(requireContext(),R.color.red)

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