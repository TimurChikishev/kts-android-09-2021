package com.swallow.cracker.ui.adapters.delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.swallow.cracker.databinding.RedditListItemWithImageBinding
import com.swallow.cracker.ui.adapters.viewholders.RedditItemWithImageViewHolder
import com.swallow.cracker.ui.modal.RedditList
import com.swallow.cracker.ui.modal.RedditListItemWithImage

class RedditListItemWithImageDelegateAdapter(private val onLikeClick: (Int, Boolean) -> Unit) :
    DelegateAdapter<RedditListItemWithImage, RedditItemWithImageViewHolder>(RedditListItemWithImage::class.java) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        RedditItemWithImageViewHolder(
            RedditListItemWithImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ) { position, likes -> onLikeClick(position, likes) }

    override fun bindViewHolder(
        model: RedditListItemWithImage,
        viewHolder: RedditItemWithImageViewHolder,
        payloads: List<RedditList>
    ) {
        viewHolder.bind(model)
    }
}