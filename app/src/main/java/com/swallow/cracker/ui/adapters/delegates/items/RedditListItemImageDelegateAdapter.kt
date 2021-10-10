package com.swallow.cracker.ui.adapters.delegates.items

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.swallow.cracker.databinding.RedditListImageItemBinding
import com.swallow.cracker.ui.adapters.delegates.ComplexDelegateAdapterClick
import com.swallow.cracker.ui.adapters.delegates.DelegateAdapter
import com.swallow.cracker.ui.adapters.viewholders.RedditItemImageViewHolder
import com.swallow.cracker.ui.model.RedditItems
import com.swallow.cracker.ui.model.RedditListItemImage

class RedditListItemImageDelegateAdapter() :
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
        payloads: List<RedditItems>
    ) {
        viewHolder.bind(model)
    }
}