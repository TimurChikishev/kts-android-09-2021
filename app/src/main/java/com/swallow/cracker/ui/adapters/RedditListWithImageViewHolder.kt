package com.swallow.cracker.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.swallow.cracker.R
import com.swallow.cracker.databinding.RedditListItemWithImageBinding
import com.swallow.cracker.ui.modal.RedditListItemWithImage

class RedditListWithImageViewHolder(private val viewBinding: RedditListItemWithImageBinding) :
    RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(modal: RedditListItemWithImage) {
        viewBinding.apply {
            avatarImageView.setImageResource(R.drawable.ic_face_24)
            authorTextView.text = modal.author
            createdTextView.text = modal.time
            titleTextView.text = modal.title
            numUpsTextView.text = modal.ups.toString()
            numCommentsTextView.text = modal.numComments.toString()
            Glide.with(itemView)
                .load(modal.thumbnail)
                .into(thumbnailImageView)
        }
    }
}