package com.swallow.cracker.ui.adapters.delegates

import androidx.recyclerview.widget.DiffUtil
import com.swallow.cracker.ui.modal.RedditList
import com.swallow.cracker.utils.getItemId

internal class DelegateAdapterItemDiffCallback : DiffUtil.ItemCallback<RedditList>() {
    override fun areItemsTheSame(oldItem: RedditList, newItem: RedditList): Boolean {
        return oldItem.getItemId() == newItem.getItemId()
    }

    override fun areContentsTheSame(oldItem: RedditList, newItem: RedditList): Boolean {
        return oldItem == newItem
    }
}