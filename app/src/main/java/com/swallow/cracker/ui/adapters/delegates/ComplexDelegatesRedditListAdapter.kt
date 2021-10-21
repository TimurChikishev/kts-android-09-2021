package com.swallow.cracker.ui.adapters.delegates

import android.util.SparseArray
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.swallow.cracker.ui.model.RedditItem

class ComplexDelegatesRedditListAdapter(
    private val delegates: SparseArray<DelegateAdapter<RedditItem, RecyclerView.ViewHolder>>
) :
    PagingDataAdapter<RedditItem, RecyclerView.ViewHolder>(DelegateAdapterItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegates[viewType].createViewHolder(parent = parent, clickDelegate = clickDelegate)
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
            ?: throw NullPointerException("Can not get viewType for position $position")
        for (i in 0 until delegates.size()) {
            if (delegates[i].modelClass == item.javaClass) {
                return delegates.keyAt(i)
            }
        }
        throw NullPointerException("Can not get viewType for position $position")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val delegateAdapter = delegates[getItemViewType(position)]
        val item = getItem(position) ?: return
        delegateAdapter.bindViewHolder(item, holder, emptyList())
    }

    var clickDelegate: ComplexDelegateAdapterClick? = null

    fun attachClickDelegate(clickDelegate: ComplexDelegateAdapterClick) {
        this.clickDelegate = clickDelegate
    }

    class Builder {
        private var count: Int = 0
        private val delegates: SparseArray<DelegateAdapter<RedditItem, RecyclerView.ViewHolder>> =
            SparseArray()

        @Suppress("UNCHECKED_CAST")
        fun add(delegateAdapter: DelegateAdapter<out RedditItem, *>): Builder {
            delegates.put(
                count++,
                delegateAdapter as DelegateAdapter<RedditItem, RecyclerView.ViewHolder>
            )
            return this
        }

        fun build(): ComplexDelegatesRedditListAdapter {
            require(count != 0) { "Register at least one adapter" }
            return ComplexDelegatesRedditListAdapter(delegates)
        }
    }
}


