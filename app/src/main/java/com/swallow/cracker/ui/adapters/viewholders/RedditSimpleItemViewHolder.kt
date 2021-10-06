package com.swallow.cracker.ui.adapters.viewholders

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.swallow.cracker.R
import com.swallow.cracker.databinding.RedditListItemBinding
import com.swallow.cracker.ui.adapters.delegates.ComplexDelegateAdapterClick
import com.swallow.cracker.ui.modal.RedditList
import com.swallow.cracker.ui.modal.RedditListSimpleItem

class RedditSimpleItemViewHolder(
    private val viewBinding: RedditListItemBinding,
    clickDelegate: ComplexDelegateAdapterClick?
) :
    RecyclerView.ViewHolder(viewBinding.root) {

    private var item: RedditListSimpleItem? = null

    init {
        viewBinding.likesImageView.setOnClickListener {
            clickDelegate?.onLikeClick(layoutPosition, true)
        }
        viewBinding.dislikesImageView.setOnClickListener {
            clickDelegate?.onLikeClick(layoutPosition, false)
        }

        viewBinding.itemContainer.setOnClickListener {
            item?.let { clickDelegate?.navigateTo(it as RedditList) }
        }

        viewBinding.shareImageView.setOnClickListener {
            item?.let { clickDelegate?.shared(it.url) }
        }
    }

    fun bind(modal: RedditListSimpleItem) {
        item = modal
        viewBinding.apply {
            avatarImageView.setImageResource(R.drawable.ic_face_24)
            authorTextView.text = modal.author
            createdTextView.text = modal.time
            titleTextView.text = modal.title
            scoreTextView.text = modal.score.toString()
            numCommentsTextView.text = modal.numComments.toString()

            setScoreStyle(modal = modal)
        }
    }

    private fun setScoreStyle(modal: RedditListSimpleItem) {
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