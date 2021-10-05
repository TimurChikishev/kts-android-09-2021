package com.swallow.cracker.ui.adapters.delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.swallow.cracker.databinding.RedditListItemBinding
import com.swallow.cracker.ui.adapters.viewholders.RedditSimpleItemViewHolder
import com.swallow.cracker.ui.modal.RedditList
import com.swallow.cracker.ui.modal.RedditListSimpleItem

class RedditListSimpleItemDelegateAdapter() :
    DelegateAdapter<RedditListSimpleItem, RedditSimpleItemViewHolder>(RedditListSimpleItem::class.java) {

    override fun createViewHolder(
        parent: ViewGroup,
        clickDelegate: ComplexDelegateAdapterClick?
    ): RecyclerView.ViewHolder =
        RedditSimpleItemViewHolder(
            RedditListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            clickDelegate
        )

    override fun bindViewHolder(
        model: RedditListSimpleItem,
        viewHolder: RedditSimpleItemViewHolder,
        payloads: List<RedditList>
    ) {
        viewHolder.bind(model)
    }
}