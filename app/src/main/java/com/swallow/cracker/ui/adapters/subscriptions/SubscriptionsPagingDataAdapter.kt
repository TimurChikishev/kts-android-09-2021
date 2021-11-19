package com.swallow.cracker.ui.adapters.subscriptions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.swallow.cracker.databinding.SearchSubredditListItemBinding
import com.swallow.cracker.ui.adapters.search.delegates.EventDelegateListAdapter
import com.swallow.cracker.ui.adapters.search.viewholders.SubredditViewHolder
import com.swallow.cracker.ui.model.Subreddit

class SubscriptionsPagingDataAdapter : PagingDataAdapter<Subreddit, SubredditViewHolder>(SubscriptionsItemDiffCallback()) {

    private var eventDelegate: EventDelegateListAdapter? = null

    fun attachEventDelegate(eventDelegate: EventDelegateListAdapter) {
        this.eventDelegate = eventDelegate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubredditViewHolder {
        return SubredditViewHolder(
            SearchSubredditListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            eventDelegate
        )
    }

    override fun onBindViewHolder(holder: SubredditViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    internal class SubscriptionsItemDiffCallback : DiffUtil.ItemCallback<Subreddit>() {
        override fun areItemsTheSame(oldItem: Subreddit, newItem: Subreddit): Boolean {
            return oldItem::class == newItem::class && oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Subreddit, newItem: Subreddit): Boolean {
            return oldItem == newItem
        }
    }
}
