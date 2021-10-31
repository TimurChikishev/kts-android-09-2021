package com.swallow.cracker.ui.adapters.search.delegates

import android.annotation.SuppressLint
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.swallow.cracker.ui.adapters.search.delegates.items.SubredditItemDelegate

class ComplexSearchDelegatesListAdapter : AsyncListDifferDelegationAdapter<Any>(ComplexSearchDiffCallback()) {

    init {
        delegatesManager
            .addDelegate(SubredditItemDelegate())
    }


    private fun removeItem(item: Any) {
        val newItems = items.toMutableList().apply {
            remove(item)
        }
        items = newItems
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(newItems: List<Any>){
        items = newItems
        notifyDataSetChanged()
    }
}