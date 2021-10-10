package com.swallow.cracker.ui.adapters.delegates

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.swallow.cracker.ui.modal.RedditList

abstract class DelegateAdapter<M, in VH : RecyclerView.ViewHolder>(val modelClass: Class<out M>) {

    abstract fun createViewHolder(
        parent: ViewGroup,
        onLikeClick: (Int, Boolean) -> Unit
    ): RecyclerView.ViewHolder

    abstract fun bindViewHolder(model: M, viewHolder: VH, payloads: List<RedditList>)

    open fun onViewRecycled(viewHolder: VH) = Unit
    open fun onViewDetachedFromWindow(viewHolder: VH) = Unit
    open fun onViewAttachedToWindow(viewHolder: VH) = Unit
}