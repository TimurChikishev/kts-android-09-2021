package com.swallow.cracker.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.swallow.cracker.R
import com.swallow.cracker.databinding.FragmentDetailsBinding
import com.swallow.cracker.ui.modal.RedditListItemWithImage
import com.swallow.cracker.utils.setSavedStatus
import timber.log.Timber

class DetailsPostWithImageFragment : Fragment(R.layout.fragment_details) {

    private val args by navArgs<DetailsPostWithImageFragmentArgs>()
    private val viewBinding by viewBinding(FragmentDetailsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initContent(item = args.post)
    }

    @SuppressLint("StringFormatMatches")
    private fun initContent(item: RedditListItemWithImage) = with(viewBinding) {

        if (item.selftext.isEmpty())
            selfTextView.visibility = View.GONE

        selfTextView.text = item.selftext
        avatarImageView.setImageResource(R.drawable.ic_face_24)
        authorTextView.text = item.subreddit
        postedByTextView.text = getString(R.string.posted_by, item.author, 0)
        createdTextView.text = item.time
        titleTextView.text = item.title
        scoreTextView.text = item.score.toString()
        numCommentsTextView.text = item.numComments.toString()

        thumbnailImageView.apply {
            Glide.with(this)
                .load(item.thumbnail)
                .error(R.drawable.ic_error_24)
                .into(this)
        }

        backImageView.setOnClickListener {
            findNavController().popBackStack()
        }

        favoriteImageView.setOnClickListener {
            item.setSavedStatus(!item.saved)
            setSavedStyle(item)
        }

        setScoreStyle(item = item)
        setSavedStyle(item = item)
    }

    private fun setSavedStyle(item: RedditListItemWithImage) {
        when (item.saved) {
            true -> {
                viewBinding.favoriteImageView.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.red
                    )
                )
            }
            false -> {
                viewBinding.favoriteImageView.colorFilter = null
            }
        }
    }

    private fun setScoreStyle(item: RedditListItemWithImage) {
        val context = viewBinding.root.context
        Timber.d("${item.likes}")
        when (item.likes) {
            true -> {
                viewBinding.likesImageView.setColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.red
                    )
                )
                viewBinding.dislikesImageView.colorFilter = null
            }
            false -> {
                viewBinding.dislikesImageView.setColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.red
                    )
                )
                viewBinding.likesImageView.colorFilter = null
            }
            null -> {
                viewBinding.dislikesImageView.colorFilter = null
                viewBinding.likesImageView.colorFilter = null
            }
        }
    }
}