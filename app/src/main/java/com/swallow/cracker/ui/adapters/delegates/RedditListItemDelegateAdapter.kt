package com.swallow.cracker.ui.adapters.delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.swallow.cracker.databinding.RedditListItemBinding
import com.swallow.cracker.ui.adapters.viewholders.RedditItemViewHolder
import com.swallow.cracker.ui.modal.RedditList
import com.swallow.cracker.ui.modal.RedditListItem

class RedditListItemDelegateAdapter(private val onLikeClick: (Int, Boolean) -> Unit) :
    DelegateAdapter<RedditListItem, RedditItemViewHolder>(RedditListItem::class.java) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        RedditItemViewHolder(
            RedditListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ) { position, likes -> onLikeClick(position, likes) }

    override fun bindViewHolder(
        model: RedditListItem,
        viewHolder: RedditItemViewHolder,
        payloads: List<RedditList>
    ) {
        viewHolder.bind(model)
    }
}