package com.swallow.cracker.ui.adapters.delegates

import androidx.recyclerview.widget.DiffUtil
import com.swallow.cracker.ui.model.RedditList
import com.swallow.cracker.utils.id

internal class DelegateAdapterItemDiffCallback : DiffUtil.ItemCallback<RedditList>() {

    override fun areItemsTheSame(oldItem: RedditList, newItem: RedditList): Boolean {
        return oldItem.id() == newItem.id()
    }

    override fun areContentsTheSame(oldItem: RedditList, newItem: RedditList): Boolean {
        return oldItem == newItem
    }
}