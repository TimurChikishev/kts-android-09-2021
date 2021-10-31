package com.swallow.cracker.ui.adapters.search.delegates

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.swallow.cracker.data.model.subreddit.RemoteSubreddit

class ComplexSearchDiffCallback : DiffUtil.ItemCallback<Any>() {
    override fun areItemsTheSame(first: Any, second: Any): Boolean {
        return first.javaClass == second.javaClass && when (first) {
            is RemoteSubreddit -> first.name == (second as RemoteSubreddit).name
            else -> true
        }
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(first: Any, second: Any): Boolean = first == second
}