package com.swallow.cracker.ui.adapters.listing.delegates

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.swallow.cracker.ui.model.RedditItem

abstract class DelegateAdapter<M, in VH : RecyclerView.ViewHolder>(val modelClass: Class<out M>) {

    abstract fun createViewHolder(
        parent: ViewGroup,
        clickDelegate: ComplexDelegateAdapterClick?
    ): RecyclerView.ViewHolder

    abstract fun bindViewHolder(model: M, viewHolder: VH, payloads: List<RedditItem>)
}