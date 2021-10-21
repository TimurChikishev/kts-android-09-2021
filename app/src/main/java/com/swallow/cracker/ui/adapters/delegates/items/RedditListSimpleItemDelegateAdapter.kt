package com.swallow.cracker.ui.adapters.delegates.items

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.swallow.cracker.databinding.RedditListSimpleItemBinding
import com.swallow.cracker.ui.adapters.delegates.ComplexDelegateAdapterClick
import com.swallow.cracker.ui.adapters.delegates.DelegateAdapter
import com.swallow.cracker.ui.adapters.viewholders.RedditSimpleItemViewHolder
import com.swallow.cracker.ui.model.ChangePayload
import com.swallow.cracker.ui.model.RedditItem
import com.swallow.cracker.ui.model.RedditListSimpleItem

class RedditListSimpleItemDelegateAdapter :
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
        payloads: List<RedditItem>
    ) {
        when (val payload = payloads.firstOrNull() as? ChangePayload) {
            is ChangePayload.LikeChanged ->
                viewHolder.bindLikes(payload.likes, payload.score)

            is ChangePayload.SavedChanged ->
                viewHolder.bindSaved(payload.saved)

            else -> viewHolder.bind(model)
        }
    }
}