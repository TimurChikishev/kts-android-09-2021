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

    // это метод реализует лайк. jetpack paging 3 сковывет (
    private fun scoreUpdate(position: Int, likes: Boolean) {
        val item = getItem(position)
        if (likes) {
            when (item!!.getLikeStatus()) {
                true -> {
                    item.setLikeStatus(null)
                    item.plusScore(-1)
                }
                false -> {
                    item.setLikeStatus(true)
                    item.plusScore(2)
                }
                null -> {
                    item.setLikeStatus(true)
                    item.plusScore(1)
                }
            }
        } else {
            when (item!!.getLikeStatus()) {
                true -> {
                    item.setLikeStatus(false)
                    item.plusScore(-2)
                }
                false -> {
                    item.setLikeStatus(null)
                    item.plusScore(1)
                }
                null -> {
                    item.setLikeStatus(false)
                    item.plusScore(-1)
                }
            }
        }

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

