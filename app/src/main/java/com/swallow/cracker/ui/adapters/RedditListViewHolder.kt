package com.swallow.cracker.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.swallow.cracker.R
import com.swallow.cracker.databinding.RedditListItemBinding
import com.swallow.cracker.ui.modal.RedditListItem

class RedditListViewHolder(private val viewBinding: RedditListItemBinding) :
    RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(modal: RedditListItem) {
        viewBinding.apply {
            avatarImageView.setImageResource(R.drawable.ic_face_24)
            authorTextView.text = modal.author
            createdTextView.text = modal.time
            titleTextView.text = modal.title
            numUpsTextView.text = modal.ups.toString()
            numCommentsTextView.text = modal.numComments.toString()
        }
    }
}