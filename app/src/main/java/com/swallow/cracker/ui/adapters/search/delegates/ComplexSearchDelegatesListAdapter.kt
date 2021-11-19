package com.swallow.cracker.ui.adapters.search.delegates

import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.swallow.cracker.ui.adapters.search.delegates.items.SearchQueryItemDelegate
import com.swallow.cracker.ui.adapters.search.delegates.items.SubredditItemDelegate

class ComplexSearchDelegatesListAdapter() : AsyncListDifferDelegationAdapter<Any>(ComplexSearchDiffCallback()) {

    private var eventDelegate: EventDelegateListAdapter? = null

    fun initEventDelegate(eventDelegate: EventDelegateListAdapter) {
        this.eventDelegate = eventDelegate
    }

    fun init(){
        delegatesManager
            .addDelegate(SubredditItemDelegate(eventDelegate))
            .addDelegate(SearchQueryItemDelegate(eventDelegate))
    }

    fun removeItem(item: Any) {
        val newItems = items.toMutableList().apply {
            remove(item)
        }
        items = newItems
    }
}