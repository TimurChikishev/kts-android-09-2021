package com.swallow.cracker.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.swallow.cracker.R
import com.swallow.cracker.ui.adapters.diff_util.DiffAdapter
import com.swallow.cracker.ui.adapters.diff_util.DiffCalculator
import com.swallow.cracker.ui.modal.RedditList
import com.swallow.cracker.ui.modal.RedditListItem
import com.swallow.cracker.ui.modal.RedditListItemWithImage

class SubredditListAdapter() : DiffAdapter<RedditList, RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.reddit_list_item -> RedditListViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.reddit_list_item, parent, false)
            )
            else -> RedditListWithImageViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.reddit_list_item_with_image, parent, false)
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is RedditListItem -> R.layout.reddit_list_item
            is RedditListItemWithImage -> R.layout.reddit_list_item_with_image
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RedditListViewHolder -> holder.bind(getItem(position) as RedditListItem)
            is RedditListWithImageViewHolder -> holder.bind(getItem(position) as RedditListItemWithImage)
        }
    }

    override fun getDiffCalculator(
        oldItems: List<RedditList>,
        newItems: List<RedditList>
    ): DiffCalculator<RedditList> = object : DiffCalculator<RedditList>(oldItems, newItems) {
        override fun areSame(first: RedditList, second: RedditList): Boolean {
            return first == second
        }

        override fun contentsAreSame(first: RedditList, second: RedditList): Boolean {
            return first == second
        }
    }
}