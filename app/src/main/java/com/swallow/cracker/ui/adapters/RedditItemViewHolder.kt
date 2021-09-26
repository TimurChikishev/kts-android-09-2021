package com.swallow.cracker.ui.adapters

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.swallow.cracker.R
import com.swallow.cracker.databinding.RedditListItemBinding
import com.swallow.cracker.ui.modal.RedditListItem
import com.swallow.cracker.ui.modal.RedditListItemWithImage
import timber.log.Timber

class RedditItemViewHolder(
    private val viewBinding: RedditListItemBinding,
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

    fun bind(modal: RedditListItem) {
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

    private fun setScoreStyle(modal: RedditListItem) {
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