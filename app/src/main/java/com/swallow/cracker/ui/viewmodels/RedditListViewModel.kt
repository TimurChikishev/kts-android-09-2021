package com.swallow.cracker.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.swallow.cracker.data.RedditRepository
import com.swallow.cracker.ui.model.QuerySubreddit

class RedditListViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val repository = RedditRepository()

    private val currentQuery =
        savedStateHandle.getLiveData(QUERY_SUBREDDIT, QuerySubreddit("popular", "", "20"))

    val posts = currentQuery.switchMap { querySub ->
        repository.getSubreddit(querySub.subreddit, querySub.category, querySub.limit)
            .cachedIn(viewModelScope)
    }

    companion object {
        private const val QUERY_SUBREDDIT = "QUERY_SUBREDDIT"
    }
}




