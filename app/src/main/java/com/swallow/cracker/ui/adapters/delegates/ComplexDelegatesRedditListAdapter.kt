package com.swallow.cracker.ui.adapters.delegates

import android.util.SparseArray
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.swallow.cracker.ui.model.RedditItems
import com.swallow.cracker.utils.setSavedStatus
import com.swallow.cracker.utils.updateScore

class ComplexDelegatesRedditListAdapter(
    private val delegates: SparseArray<DelegateAdapter<RedditItems, RecyclerView.ViewHolder>>
) :
    PagingDataAdapter<RedditItems, RecyclerView.ViewHolder>(DelegateAdapterItemDiffCallback()) {

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

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        delegates[holder.itemViewType].onViewRecycled(holder)
        super.onViewRecycled(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        delegates[holder.itemViewType].onViewDetachedFromWindow(holder)
        super.onViewDetachedFromWindow(holder)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        delegates[holder.itemViewType].onViewAttachedToWindow(holder)
        super.onViewAttachedToWindow(holder)
    }

    fun onLikeClick(position: Int, likes: Boolean) {
        snapshot()[position]?.updateScore(likes)
        notifyItemChanged(position)
    }

    fun onSavedClick(position: Int, saved: Boolean) {
        snapshot()[position]?.setSavedStatus(saved)
        notifyItemChanged(position)
    }

    var clickDelegate: ComplexDelegateAdapterClick? = null

    fun attachClickDelegate(clickDelegate: ComplexDelegateAdapterClick) {
        this.clickDelegate = clickDelegate
    }

    class Builder {
        private var count: Int = 0
        private val delegates: SparseArray<DelegateAdapter<RedditItems, RecyclerView.ViewHolder>> =
            SparseArray()

        fun add(delegateAdapter: DelegateAdapter<out RedditItems, *>): Builder {
            delegates.put(
                count++,
                delegateAdapter as DelegateAdapter<RedditItems, RecyclerView.ViewHolder>
            )
            return this
        }

        fun build(): ComplexDelegatesRedditListAdapter {
            require(count != 0) { "Register at least one adapter" }
            return ComplexDelegatesRedditListAdapter(delegates)
        }
    }
}

interface ComplexDelegateAdapterClick {
    fun onVoteClick(position: Int, likes: Boolean)
    fun onSavedClick(category: String?, id: String, position: Int?, saved: Boolean)
    fun navigateTo(item: RedditItems)
    fun shared(url: String)
}

