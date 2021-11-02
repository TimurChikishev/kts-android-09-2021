package com.swallow.cracker.ui.adapters.search.delegates

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.swallow.cracker.data.model.subreddit.RemoteSubreddit
import com.swallow.cracker.ui.model.SearchQuery

class ComplexSearchDiffCallback : DiffUtil.ItemCallback<Any>() {
    override fun areItemsTheSame(first: Any, second: Any): Boolean {
        return first.javaClass == second.javaClass && when (first) {
            is RemoteSubreddit -> first.name == (second as RemoteSubreddit).name
            is SearchQuery -> first.query == (second as SearchQuery).query
            else -> true
        }
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(first: Any, second: Any): Boolean = first == second
}