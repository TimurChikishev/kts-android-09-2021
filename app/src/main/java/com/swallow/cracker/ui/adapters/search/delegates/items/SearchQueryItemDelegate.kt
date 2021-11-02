package com.swallow.cracker.ui.adapters.search.delegates.items

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.swallow.cracker.databinding.SearchQueryListItemBinding
import com.swallow.cracker.ui.adapters.search.delegates.EventSearchDelegateListAdapter
import com.swallow.cracker.ui.adapters.search.viewholders.SearchQueryViewHolder
import com.swallow.cracker.ui.model.SearchQuery

class SearchQueryItemDelegate(
    private val eventDelegate: EventSearchDelegateListAdapter?
) : AbsListItemAdapterDelegate<Any, Any, SearchQueryViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is SearchQuery
    }

    override fun onCreateViewHolder(parent: ViewGroup) = SearchQueryViewHolder(
        SearchQueryListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),
        eventDelegate
    )

    override fun onBindViewHolder(item: Any, holder: SearchQueryViewHolder, payloads: MutableList<Any>) {
        holder.bind(item as SearchQuery)
    }
}