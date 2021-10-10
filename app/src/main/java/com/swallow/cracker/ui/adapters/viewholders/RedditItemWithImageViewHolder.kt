package com.swallow.cracker.ui.adapters.viewholders

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.swallow.cracker.R
import com.swallow.cracker.databinding.RedditListItemWithImageBinding
import com.swallow.cracker.ui.modal.RedditListItemWithImage
import timber.log.Timber

class RedditItemWithImageViewHolder(
    private val viewBinding: RedditListItemWithImageBinding,
    upsChange: (Int, Boolean) -> Unit
) :
    RecyclerView.ViewHolder(viewBinding.root) {

    init {
        viewBinding.likesImageView.setOnClickListener {
            upsChange.invoke(layoutPosition, true)
        }

        viewBinding.dislikesImageView.setOnClickListener {
            upsChange.invoke(layoutPosition, false)
        }
    }

    fun bind(modal: RedditListItemWithImage) {
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