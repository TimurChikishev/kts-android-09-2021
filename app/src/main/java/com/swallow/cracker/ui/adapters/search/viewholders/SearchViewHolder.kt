package com.swallow.cracker.ui.adapters.search.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.swallow.cracker.R
import com.swallow.cracker.databinding.SearchListItemBinding
import com.swallow.cracker.ui.model.Subreddit

class SearchViewHolder(
    private val viewBinding: SearchListItemBinding
) : RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(subreddit: Subreddit) = with(subreddit) {
        setSubredditName(displayName)
        setMembers(subscribers ?: 0)
        setSubredditIcon(communityIcon)
    }

    private fun setSubredditIcon(communityIcon: String) {
        Glide.with(viewBinding.subredditImageView)
            .load(communityIcon)
            .circleCrop()
            .error(R.drawable.ic_subreddit_512dp)
            .into(viewBinding.subredditImageView)
    }

    private fun setMembers(subscriberCount: Int) {
        viewBinding.membersTextView.text = subscriberCount.toString()
    }

    private fun setSubredditName(name: String) {
        viewBinding.nameSubredditTextView.text = name
    }
}