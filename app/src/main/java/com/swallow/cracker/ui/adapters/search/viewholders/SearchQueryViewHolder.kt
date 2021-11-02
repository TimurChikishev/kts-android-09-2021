package com.swallow.cracker.ui.adapters.search.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.swallow.cracker.databinding.SearchQueryListItemBinding
import com.swallow.cracker.ui.adapters.search.delegates.EventSearchDelegateListAdapter
import com.swallow.cracker.ui.model.SearchQuery

class SearchQueryViewHolder(
    private val viewBinding: SearchQueryListItemBinding,
    private val eventDelegate: EventSearchDelegateListAdapter?
) : RecyclerView.ViewHolder(viewBinding.root) {

    private var item: SearchQuery? = null

    init {
        viewBinding.buttonRemove.setOnClickListener {
            item?.let { eventDelegate?.removeItem(it) }
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