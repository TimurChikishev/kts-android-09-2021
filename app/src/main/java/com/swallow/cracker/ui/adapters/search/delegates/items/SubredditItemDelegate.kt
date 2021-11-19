package com.swallow.cracker.ui.adapters.search.delegates.items

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.swallow.cracker.databinding.SearchSubredditListItemBinding
import com.swallow.cracker.ui.adapters.search.delegates.EventDelegateListAdapter
import com.swallow.cracker.ui.adapters.search.viewholders.SubredditViewHolder
import com.swallow.cracker.ui.model.Subreddit

class SubredditItemDelegate(
    private val eventDelegate: EventDelegateListAdapter?
) : AbsListItemAdapterDelegate<Any, Any, SubredditViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is Subreddit
    }

    override fun onCreateViewHolder(parent: ViewGroup) = SubredditViewHolder(
        SearchSubredditListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),
        eventDelegate
    )

    override fun onBindViewHolder(item: Any, holder: SubredditViewHolder, payloads: MutableList<Any>) {
        holder.bind(item as Subreddit)
    }
}