package com.swallow.cracker.ui.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.swallow.cracker.R
import com.swallow.cracker.ui.modal.RedditListItemWithImage

class RedditListWithImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val avatarImageView: ImageView = itemView.findViewById(R.id.avatarImageView)
    private val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)
    private val createdTextView: TextView = itemView.findViewById(R.id.createdTextView)
    private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
    private val numUpsTextView: TextView = itemView.findViewById(R.id.numUpsTextView)
    private val numCommentsTextView: TextView = itemView.findViewById(R.id.numCommentsTextView)
    private val thumbnailImageView: ImageView = itemView.findViewById(R.id.thumbnailImageView)

    private val upsUpImageView: ImageView = itemView.findViewById(R.id.upsUpImageView)
    private val upsDownImageView: ImageView = itemView.findViewById(R.id.upsDownImageView)

    fun bind(modal: RedditListItemWithImage) {
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