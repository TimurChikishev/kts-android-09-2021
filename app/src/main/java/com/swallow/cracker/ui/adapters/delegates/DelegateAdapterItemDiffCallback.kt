package com.swallow.cracker.ui.adapters.delegates

import androidx.recyclerview.widget.DiffUtil
import com.swallow.cracker.ui.model.RedditItems
import com.swallow.cracker.utils.id

internal class DelegateAdapterItemDiffCallback : DiffUtil.ItemCallback<RedditItems>() {

    override fun areItemsTheSame(oldItem: RedditItems, newItem: RedditItems): Boolean {
        return oldItem.id() == newItem.id()
    }

    override fun areContentsTheSame(oldItem: RedditItems, newItem: RedditItems): Boolean {
        return oldItem == newItem
    }
}