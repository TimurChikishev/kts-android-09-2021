package com.swallow.cracker.ui.adapters.viewholders

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.swallow.cracker.R
import com.swallow.cracker.databinding.RedditListItemWithImageBinding
import com.swallow.cracker.ui.adapters.delegates.ComplexDelegateAdapterClick
import com.swallow.cracker.ui.model.RedditList
import com.swallow.cracker.ui.model.RedditListItemWithImage

class RedditItemWithImageViewHolder(
    private val viewBinding: RedditListItemWithImageBinding,
    private val clickDelegate: ComplexDelegateAdapterClick?
) :
    RecyclerView.ViewHolder(viewBinding.root) {

    private var item: RedditListItemWithImage? = null

    init {
        viewBinding.likesImageView.setOnClickListener {
            clickDelegate?.onVoteClick(layoutPosition, true)
        }

        viewBinding.dislikesImageView.setOnClickListener {
            clickDelegate?.onVoteClick(layoutPosition, false)
        }

        viewBinding.itemContainer.setOnClickListener {
            item?.let { clickDelegate?.navigateTo(it as RedditList) }
        }

        viewBinding.shareImageView.setOnClickListener {
            item?.let { clickDelegate?.shared(it.url) }
        }
    }

    fun bind(modal: RedditListItemWithImage) = with(modal){
        item = this

        setAvatar(R.drawable.ic_face_24)
        setSubreddit(subreddit)
        setPublisher(author)
        setTitle(title)
        setCreated(time)
        setNumScore(score.toString())
        setNumComments(numComments.toString())
        setThumbnail(thumbnail, url)

        setScoreStyle(this)
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

    private fun setThumbnail(thumbnail: String, url: String? = null) = with(viewBinding){
        Glide.with(thumbnailImageView)
            .load(url)
            .thumbnail(Glide.with(thumbnailImageView).load(thumbnail))
            .into(thumbnailImageView)
    }

    private fun setScoreStyle(modal: RedditListItemWithImage) {
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