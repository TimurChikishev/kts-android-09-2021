package com.swallow.cracker.ui.adapters.listing.delegates.items

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.swallow.cracker.databinding.RedditListPlaceholderItemBinding
import com.swallow.cracker.ui.adapters.listing.delegates.ComplexDelegateAdapterClick
import com.swallow.cracker.ui.adapters.listing.delegates.DelegateAdapter
import com.swallow.cracker.ui.adapters.listing.viewholders.RedditListPlaceholderItemViewHolder
import com.swallow.cracker.ui.model.RedditItem
import com.swallow.cracker.ui.model.RedditListPlaceholder

class RedditListPlaceholderItem :
    DelegateAdapter<RedditListPlaceholder, RedditListPlaceholderItemViewHolder>(RedditListPlaceholder::class.java) {

    override fun createViewHolder(
        parent: ViewGroup,
        clickDelegate: ComplexDelegateAdapterClick?
    ): RecyclerView.ViewHolder =
        RedditListPlaceholderItemViewHolder(
            RedditListPlaceholderItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun bindViewHolder(
        model: RedditListPlaceholder,
        viewHolder: RedditListPlaceholderItemViewHolder,
        payloads: List<RedditItem>
    ) = Unit
}