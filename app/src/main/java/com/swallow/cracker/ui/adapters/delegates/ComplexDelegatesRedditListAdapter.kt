package com.swallow.cracker.ui.adapters.delegates

import android.util.SparseArray
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.swallow.cracker.ui.modal.RedditList
import com.swallow.cracker.utils.updateScore

class ComplexDelegatesRedditListAdapter(
    private val delegates: SparseArray<DelegateAdapter<RedditList, RecyclerView.ViewHolder>>
) :
    PagingDataAdapter<RedditList, RecyclerView.ViewHolder>(DelegateAdapterItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegates[viewType].createViewHolder(parent = parent)
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

    // этот метод реализует лайк
    fun onLikeClick(position: Int, likes: Boolean) {
        snapshot()[position]?.updateScore(likes)
        notifyItemChanged(position)
    }

    class Builder {
        private var count: Int = 0
        private val delegates: SparseArray<DelegateAdapter<RedditList, RecyclerView.ViewHolder>> =
            SparseArray()

        fun add(delegateAdapter: DelegateAdapter<out RedditList, *>): Builder {
            delegates.put(
                count++,
                delegateAdapter as DelegateAdapter<RedditList, RecyclerView.ViewHolder>
            )
            return this
        }

        fun build(): ComplexDelegatesRedditListAdapter {
            require(count != 0) { "Register at least one adapter" }
            return ComplexDelegatesRedditListAdapter(delegates)
        }
    }
}

