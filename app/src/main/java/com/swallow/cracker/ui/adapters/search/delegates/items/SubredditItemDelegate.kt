package com.swallow.cracker.ui.adapters.search.delegates.items

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.swallow.cracker.databinding.SearchListItemBinding
import com.swallow.cracker.ui.adapters.search.viewholders.SearchViewHolder
import com.swallow.cracker.ui.model.Subreddit

class SubredditItemDelegate : AbsListItemAdapterDelegate<Any, Any, SearchViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is Subreddit
    }

    override fun onCreateViewHolder(parent: ViewGroup) = SearchViewHolder(
        SearchListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(item: Any, holder: SearchViewHolder, payloads: MutableList<Any>) {
        holder.bind(item as Subreddit)
    }
}