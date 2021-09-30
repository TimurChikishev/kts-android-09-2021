package com.swallow.cracker.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.swallow.cracker.R
import com.swallow.cracker.databinding.RedditListItemBinding
import com.swallow.cracker.databinding.RedditListItemWithImageBinding
import com.swallow.cracker.ui.modal.RedditList
import com.swallow.cracker.ui.modal.RedditListItem
import com.swallow.cracker.ui.modal.RedditListItemWithImage
import com.swallow.cracker.utils.getItemId
import com.swallow.cracker.utils.updateScore

class RedditListAdapter() :
    PagingDataAdapter<RedditList, RecyclerView.ViewHolder>(POST_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.reddit_list_item -> RedditItemViewHolder(
                RedditListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            ) { position, likes -> scoreUpdate(position = position, likes = likes) }
            else -> RedditItemWithImageViewHolder(
                RedditListItemWithImageBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            ) { position, likes -> scoreUpdate(position = position, likes = likes) }
        }
    }

    // это метод реализует лайк.
    private fun scoreUpdate(position: Int, likes: Boolean) {
        getItem(position)?.updateScore(likes)
        notifyItemChanged(position)
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is RedditListItem -> R.layout.reddit_list_item
            is RedditListItemWithImage -> R.layout.reddit_list_item_with_image
            else -> error("error")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RedditItemViewHolder -> holder.bind(getItem(position) as RedditListItem)
            is RedditItemWithImageViewHolder -> holder.bind(getItem(position) as RedditListItemWithImage)
        }
    }

    companion object {
        private val POST_COMPARATOR = object : DiffUtil.ItemCallback<RedditList>() {
            override fun areItemsTheSame(oldItem: RedditList, newItem: RedditList): Boolean {
                return oldItem.getItemId() == newItem.getItemId()
            }

            override fun areContentsTheSame(oldItem: RedditList, newItem: RedditList): Boolean {
                return oldItem == newItem
            }
        }
    }
}

