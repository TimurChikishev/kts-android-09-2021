package com.swallow.cracker.ui.adapters.delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.swallow.cracker.databinding.RedditListItemWithImageBinding
import com.swallow.cracker.ui.adapters.viewholders.RedditItemWithImageViewHolder
import com.swallow.cracker.ui.modal.RedditList
import com.swallow.cracker.ui.modal.RedditListItemWithImage

class RedditListItemWithImageDelegateAdapter() :
    DelegateAdapter<RedditListItemWithImage, RedditItemWithImageViewHolder>(RedditListItemWithImage::class.java) {

    override fun createViewHolder(
        parent: ViewGroup,
        clickDelegate: ComplexDelegateAdapterClick?
    ): RecyclerView.ViewHolder =
        RedditItemWithImageViewHolder(
            RedditListItemWithImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            clickDelegate
        )

    override fun bindViewHolder(
        model: RedditListItemWithImage,
        viewHolder: RedditItemWithImageViewHolder,
        payloads: List<RedditList>
    ) {
        viewHolder.bind(model)
    }
}