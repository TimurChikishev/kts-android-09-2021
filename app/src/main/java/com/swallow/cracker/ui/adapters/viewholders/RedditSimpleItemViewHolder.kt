package com.swallow.cracker.ui.adapters.viewholders

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.swallow.cracker.R
import com.swallow.cracker.databinding.RedditListSimpleItemBinding
import com.swallow.cracker.ui.adapters.delegates.ComplexDelegateAdapterClick
import com.swallow.cracker.ui.model.RedditItem
import com.swallow.cracker.ui.model.RedditListSimpleItem

class RedditSimpleItemViewHolder(
    private val viewBinding: RedditListSimpleItemBinding,
    clickDelegate: ComplexDelegateAdapterClick?
) : RecyclerView.ViewHolder(viewBinding.root) {

    private var item: RedditListSimpleItem? = null
    private var context: Context = viewBinding.root.context

    init {
        viewBinding.likesImageView.setOnClickListener {
            item?.let {
                viewBinding.likesImageView.isClickable = false
                clickDelegate?.onVoteClick(it, true)
            }
        }

        viewBinding.dislikesImageView.setOnClickListener {
            item?.let {
                viewBinding.dislikesImageView.isClickable = false
                clickDelegate?.onVoteClick(it, false)
            }
        }

        viewBinding.savedImageView.setOnClickListener {
            item?.let {
                viewBinding.savedImageView.isClickable = false
                clickDelegate?.onSavedClick(it, !it.saved)
            }
        }

        viewBinding.itemContainer.setOnClickListener {
            item?.let {
                viewBinding.itemContainer.isEnabled = false
                clickDelegate?.navigateTo(it as RedditItem)
            }
        }

        viewBinding.shareImageView.setOnClickListener {
            item?.let { clickDelegate?.shared(it.url) }
        }
    }

    fun bind(modal: RedditListSimpleItem) = with(modal) {
        item = this

        setClickable()
        setAvatar(R.drawable.ic_face_24)
        setSubreddit(subreddit)
        setPublisher(author)
        setTitle(title)
        setCreated(time)
        setNumScore(score.toString())
        setNumComments(numComments.toString())

        setScoreStyle(this)
        setSavedStyle(saved)
    }

    private fun setPublisher(author: String) {
        viewBinding.publisherTextView.text =
            context.getString(R.string.posted_by, author)
    }

    private fun setNumScore(score: String) {
        viewBinding.scoreTextView.text = score
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

    private fun setNumComments(num: String) {
        viewBinding.numCommentsTextView.text = num
    }

    private fun setAvatar(res: Int) {
        viewBinding.avatarImageView.setImageResource(res)
    }

    private fun setClickable() = with(viewBinding) {
        likesImageView.isClickable = true
        dislikesImageView.isClickable = true
        savedImageView.isClickable = true
    }

    // setting the style for save/unsave buttons
    private fun setSavedStyle(boolean: Boolean) = with(viewBinding) {
        when (boolean) {
            true -> {
                val color = ContextCompat.getColor(root.context, R.color.red)
                savedImageView.setColorFilter(color)
            }
            false -> {
                savedImageView.colorFilter = null
            }
        }
    }

    private fun setScoreStyle(modal: RedditListSimpleItem) {
        when (modal.likes) {
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