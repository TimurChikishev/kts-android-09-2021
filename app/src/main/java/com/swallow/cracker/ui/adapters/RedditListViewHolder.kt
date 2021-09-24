package com.swallow.cracker.ui.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.swallow.cracker.R
import com.swallow.cracker.ui.modal.RedditListItem

class RedditListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val avatarImageView: ImageView = itemView.findViewById(R.id.avatarImageView)
    private val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)
    private val createdTextView: TextView = itemView.findViewById(R.id.createdTextView)
    private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
    private val numUpsTextView: TextView = itemView.findViewById(R.id.numUpsTextView)
    private val numCommentsTextView: TextView = itemView.findViewById(R.id.numCommentsTextView)

    fun bind(modal: RedditListItem) {
        avatarImageView.setImageResource(R.drawable.ic_face_24)
        authorTextView.text = modal.author
        createdTextView.text = modal.time
        titleTextView.text = modal.title
        numUpsTextView.text = modal.ups.toString()
        numCommentsTextView.text = modal.numComments.toString()
    }
}