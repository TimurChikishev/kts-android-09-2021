package com.swallow.cracker.ui.adapters.search.delegates

import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.swallow.cracker.ui.adapters.search.delegates.items.SearchQueryItemDelegate
import com.swallow.cracker.ui.adapters.search.delegates.items.SubredditItemDelegate

class ComplexSearchDelegatesListAdapter() : AsyncListDifferDelegationAdapter<Any>(ComplexSearchDiffCallback()) {

    private var eventDelegate: EventSearchDelegateListAdapter? = null

    fun initEventDelegate(eventDelegate: EventSearchDelegateListAdapter) {
        this.eventDelegate = eventDelegate
    }

    fun init(){
        delegatesManager
            .addDelegate(SubredditItemDelegate())
            .addDelegate(SearchQueryItemDelegate(eventDelegate))
    }

    fun removeItem(item: Any) {
        val newItems = items.toMutableList().apply {
            remove(item)
        }
        items = newItems
    }
}