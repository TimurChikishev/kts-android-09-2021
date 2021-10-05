package com.swallow.cracker.ui.adapters.viewholders

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.swallow.cracker.R
import com.swallow.cracker.databinding.RedditListItemWithImageBinding
import com.swallow.cracker.ui.adapters.delegates.ComplexDelegateAdapterClick
import com.swallow.cracker.ui.modal.RedditListItemWithImage
import timber.log.Timber

class RedditItemWithImageViewHolder(
    private val viewBinding: RedditListItemWithImageBinding,
    private val clickDelegate: ComplexDelegateAdapterClick?
) :
    RecyclerView.ViewHolder(viewBinding.root) {

    private var item: RedditListItemWithImage? = null

    init {
        viewBinding.likesImageView.setOnClickListener {
            clickDelegate?.onLikeClick(layoutPosition, true)
        }

        viewBinding.dislikesImageView.setOnClickListener {
            clickDelegate?.onLikeClick(layoutPosition, false)
        }

        viewBinding.itemContainer.setOnClickListener {
            item?.let { clickDelegate?.navigateToDetailsWithImage(it) }
        }
    }

    fun bind(modal: RedditListItemWithImage) {
        item = modal
        viewBinding.apply {
            avatarImageView.setImageResource(R.drawable.ic_face_24)
            authorTextView.text = modal.author
            createdTextView.text = modal.time
            titleTextView.text = modal.title
            scoreTextView.text = modal.score.toString()
            numCommentsTextView.text = modal.numComments.toString()
            Glide.with(itemView)
                .load(modal.thumbnail)
                .error(R.drawable.ic_error_24)
                .dontAnimate()
                .into(thumbnailImageView)

            setScoreStyle(modal = modal)
        }
    }

    private fun setScoreStyle(modal: RedditListItemWithImage) {
        val context = viewBinding.root.context
        Timber.d("${modal.likes}")
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