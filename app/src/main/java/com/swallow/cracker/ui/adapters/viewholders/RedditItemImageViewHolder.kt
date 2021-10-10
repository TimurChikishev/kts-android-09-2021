package com.swallow.cracker.ui.adapters.viewholders

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.swallow.cracker.R
import com.swallow.cracker.databinding.RedditListImageItemBinding
import com.swallow.cracker.ui.adapters.delegates.ComplexDelegateAdapterClick
import com.swallow.cracker.ui.model.RedditItems
import com.swallow.cracker.ui.model.RedditListItemImage

class RedditItemImageViewHolder(
    private val viewBinding: RedditListImageItemBinding,
    private val clickDelegate: ComplexDelegateAdapterClick?
) :
    RecyclerView.ViewHolder(viewBinding.root) {

    private lateinit var item: RedditListItemImage

    init {
        viewBinding.likesImageView.setOnClickListener {
            viewBinding.likesImageView.isClickable = false
            clickDelegate?.onVoteClick(layoutPosition, true)
        }

        viewBinding.dislikesImageView.setOnClickListener {
            viewBinding.dislikesImageView.isClickable = false
            clickDelegate?.onVoteClick(layoutPosition, false)
        }

        viewBinding.itemContainer.setOnClickListener {
            clickDelegate?.navigateTo(item as RedditItems)
        }

        viewBinding.savedImageView.setOnClickListener {
            viewBinding.savedImageView.isClickable = false
            when (!item.saved) {
                true -> clickDelegate?.onSavedClick(category = null, id = item.t3_id, position = layoutPosition, saved = true)
                false -> clickDelegate?.onSavedClick(category = null, id = item.t3_id, position = layoutPosition, saved = false)
            }
        }

        viewBinding.shareImageView.setOnClickListener {
            clickDelegate?.shared(item.url)
        }
    }

    fun bind(modal: RedditListItemImage) = with(modal){
        item = this

        setClickable()
        setAvatar(R.drawable.ic_face_24)
        setSubreddit(subreddit)
        setPublisher(author)
        setTitle(title)
        setCreated(time)
        setNumScore(score.toString())
        setNumComments(numComments.toString())
        setThumbnail(thumbnail = thumbnail)

        setScoreStyle(this)
        setSavedStyle(saved)
    }

    private fun setPublisher(author: String) {
        viewBinding.publisherTextView.text =
            viewBinding.root.context.getString(R.string.posted_by, author)
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

    private fun setThumbnail(thumbnail: String) = with(viewBinding){
        Glide.with(thumbnailImageView)
            .load(thumbnail)
            .override(500, 300)
            .into(thumbnailImageView)
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

    private fun setScoreStyle(modal: RedditListItemImage) {
        val context = viewBinding.root.context

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