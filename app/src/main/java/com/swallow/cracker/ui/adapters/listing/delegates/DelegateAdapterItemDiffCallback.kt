package com.swallow.cracker.ui.adapters.listing.delegates

import androidx.recyclerview.widget.DiffUtil
import com.swallow.cracker.ui.model.RedditItem

internal class DelegateAdapterItemDiffCallback : DiffUtil.ItemCallback<RedditItem>() {
    override fun areItemsTheSame(oldItem: RedditItem, newItem: RedditItem): Boolean {
        return oldItem::class == newItem::class && oldItem.prefixId == newItem.prefixId
    }

    override fun areContentsTheSame(oldItem: RedditItem, newItem: RedditItem): Boolean {
        return oldItem.equalsContent(newItem)
    }

    override fun getChangePayload(oldItem: RedditItem, newItem: RedditItem): Any {
        return oldItem.payload(newItem)
    }
}