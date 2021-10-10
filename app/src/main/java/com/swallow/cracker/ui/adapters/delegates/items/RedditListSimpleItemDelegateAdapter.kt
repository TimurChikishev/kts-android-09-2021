package com.swallow.cracker.ui.adapters.delegates.items

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.swallow.cracker.databinding.RedditListSimpleItemBinding
import com.swallow.cracker.ui.adapters.delegates.ComplexDelegateAdapterClick
import com.swallow.cracker.ui.adapters.delegates.DelegateAdapter
import com.swallow.cracker.ui.adapters.viewholders.RedditSimpleItemViewHolder
import com.swallow.cracker.ui.model.RedditItems
import com.swallow.cracker.ui.model.RedditListSimpleItem

class RedditListSimpleItemDelegateAdapter() :
    DelegateAdapter<RedditListSimpleItem, RedditSimpleItemViewHolder>(RedditListSimpleItem::class.java) {

    override fun createViewHolder(
        parent: ViewGroup,
        clickDelegate: ComplexDelegateAdapterClick?
    ): RecyclerView.ViewHolder =
        RedditSimpleItemViewHolder(
            RedditListSimpleItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            clickDelegate
        )

    override fun bindViewHolder(
        model: RedditListSimpleItem,
        viewHolder: RedditSimpleItemViewHolder,
        payloads: List<RedditItems>
    ) {
        viewHolder.bind(model)
    }
}