package com.swallow.cracker.ui.adapters.listing.delegates.items

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.swallow.cracker.databinding.RedditListImageItemBinding
import com.swallow.cracker.ui.adapters.listing.delegates.ComplexDelegateAdapterClick
import com.swallow.cracker.ui.adapters.listing.delegates.DelegateAdapter
import com.swallow.cracker.ui.adapters.listing.viewholders.RedditItemImageViewHolder
import com.swallow.cracker.ui.model.ChangePayload
import com.swallow.cracker.ui.model.RedditItem
import com.swallow.cracker.ui.model.RedditListItemImage

class RedditListItemImageDelegateAdapter :
    DelegateAdapter<RedditListItemImage, RedditItemImageViewHolder>(RedditListItemImage::class.java) {

    override fun createViewHolder(
        parent: ViewGroup,
        clickDelegate: ComplexDelegateAdapterClick?
    ): RecyclerView.ViewHolder =
        RedditItemImageViewHolder(
            RedditListImageItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            clickDelegate
        )

    override fun bindViewHolder(
        model: RedditListItemImage,
        viewHolder: RedditItemImageViewHolder,
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