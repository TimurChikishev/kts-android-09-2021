package com.swallow.cracker.ui.adapters.delegates

import com.swallow.cracker.ui.model.RedditItem

interface ComplexDelegateAdapterClick {
    fun onVoteClick(item: RedditItem, likes: Boolean)
    fun onSavedClick(item: RedditItem, saved: Boolean)
    fun navigateTo(item: RedditItem)
    fun shared(url: String)
}