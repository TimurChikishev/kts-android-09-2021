package com.swallow.cracker.ui.adapters.listing.viewholders

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.swallow.cracker.R
import com.swallow.cracker.data.model.listing.RedditChildrenPreview
import com.swallow.cracker.databinding.RedditListImageItemBinding
import com.swallow.cracker.ui.adapters.listing.delegates.ComplexDelegateAdapterClick
import com.swallow.cracker.ui.model.RedditItem
import com.swallow.cracker.ui.model.RedditListItemImage
import timber.log.Timber

class RedditItemImageViewHolder(
    private val viewBinding: RedditListImageItemBinding,
    private val clickDelegate: ComplexDelegateAdapterClick?
) : RecyclerView.ViewHolder(viewBinding.root) {

    private var item: RedditListItemImage? = null
    private var context: Context = viewBinding.root.context

    init {
        viewBinding.likesImageView.setOnClickListener {
            item?.let {
                clickDelegate?.onVoteClick(it, true)
            }
        }

        viewBinding.dislikesImageView.setOnClickListener {
            item?.let {
                clickDelegate?.onVoteClick(it, false)
            }
        }

        viewBinding.savedImageView.setOnClickListener {
            item?.let {
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

    fun bind(modal: RedditListItemImage) = with(modal) {
        item = this

        setAvatar(communityIcon)
        setSubreddit(subreddit)
        setPublisher(author)
        setTitle(title)
        setCreated(time)
        setNumScore(score.toString())
        setNumComments(numComments.toString())
        setThumbnail(thumbnail = thumbnail, preview = preview)

        setScoreStyle(likes)
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

    private fun setAvatar(communityIcon: String?) = with(viewBinding) {
        if (communityIcon.isNullOrEmpty()) {
            avatarImageView.setImageResource(R.drawable.ic_subreddit_24)
        } else {
            Glide.with(context)
                .load(communityIcon)
                .circleCrop()
                .error(R.drawable.ic_subreddit_24)
                .into(avatarImageView)
        }
    }

    private fun setThumbnail(thumbnail: String, preview: RedditChildrenPreview?) =
        with(viewBinding) {
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

    private fun setScoreStyle(likes: Boolean?) {
        when (likes) {
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

    fun bindLikes(likes: Boolean?, score: Int) {
        setScoreStyle(likes)
        viewBinding.scoreTextView.text = score.toString()
    }

    fun bindSaved(saved: Boolean) {
        setSavedStyle(saved)
    }
}