package com.swallow.cracker.ui.adapters.delegates

import com.swallow.cracker.ui.model.RedditItem

interface ComplexDelegateAdapterClick {
    fun onVoteClick(position: Int, likes: Boolean)
    fun onSavedClick(category: String?, id: String, position: Int?, saved: Boolean)
    fun navigateTo(item: RedditItem)
    fun shared(url: String)
}