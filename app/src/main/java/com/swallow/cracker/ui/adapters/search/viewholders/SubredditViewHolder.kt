package com.swallow.cracker.ui.adapters.search.viewholders

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.swallow.cracker.R
import com.swallow.cracker.databinding.SearchSubredditListItemBinding
import com.swallow.cracker.ui.adapters.search.delegates.EventDelegateListAdapter
import com.swallow.cracker.ui.model.Subreddit

class SubredditViewHolder(
    private val viewBinding: SearchSubredditListItemBinding,
    private val eventDelegate: EventDelegateListAdapter?
) : RecyclerView.ViewHolder(viewBinding.root) {

    private var item: Subreddit? = null
    private var context: Context? = null

    init {
        viewBinding.container.setOnClickListener {
            item?.let { eventDelegate?.onItemClick(it) }
        }
    }

    fun bind(subreddit: Subreddit) = with(subreddit) {
        context = viewBinding.root.context
        item = subreddit
        setSubredditName(displayName)
        setMembers(subscribers ?: 0)
        setSubredditIcon(communityIcon)
    }

    private fun setSubredditIcon(communityIcon: String) {
        Glide.with(viewBinding.subredditImageView)
            .load(communityIcon)
            .circleCrop()
            .error(R.drawable.ic_subreddit_24)
            .into(viewBinding.subredditImageView)
    }

    private fun setMembers(subscriberCount: Int) {
        viewBinding.membersTextView.text = context?.getString(R.string.members, subscriberCount)
    }

    private fun setSubredditName(name: String) {
        viewBinding.nameSubredditTextView.text = name
    }
}