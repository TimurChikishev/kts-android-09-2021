package com.swallow.cracker.ui.adapters.search.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.swallow.cracker.databinding.SearchQueryListItemBinding
import com.swallow.cracker.ui.adapters.search.delegates.EventDelegateListAdapter
import com.swallow.cracker.ui.model.SearchQuery

class SearchQueryViewHolder(
    private val viewBinding: SearchQueryListItemBinding,
    private val eventDelegate: EventDelegateListAdapter?
) : RecyclerView.ViewHolder(viewBinding.root) {

    private var item: SearchQuery? = null

    init {
        viewBinding.buttonRemove.setOnClickListener {
            item?.let { eventDelegate?.removeItem(it) }
        }

        viewBinding.container.setOnClickListener {
            item?.let { eventDelegate?.onItemClick(it) }
        }
    }

    fun bind(subreddit: SearchQuery) = with(subreddit) {
        item = subreddit
        setSubredditName(query)
    }

    private fun setSubredditName(name: String) {
        viewBinding.nameSubredditTextView.text = name
    }
}