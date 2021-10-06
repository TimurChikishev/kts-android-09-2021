package com.swallow.cracker.ui.viewmodels

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.swallow.cracker.data.RedditRepository
import com.swallow.cracker.ui.modal.QuerySubreddit
import com.swallow.cracker.utils.set
import kotlinx.coroutines.Job

class RedditListViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val repository = RedditRepository()
    private var currentSearchJob: Job? = null

    private val currentQuery =
        savedStateHandle.getLiveData(QUERY_SUBREDDIT, QuerySubreddit("kotlin", "", "20"))

    val posts = currentQuery.switchMap { querySub ->
        repository.getSubreddit(querySub.subreddit, querySub.category, querySub.limit)
            .cachedIn(viewModelScope)
    }

    fun updateQuerySubreddit(subreddit: String, category: String, limit: String) {
        currentQuery.set(QuerySubreddit(subreddit, category, limit))
    }

    companion object {
        private const val QUERY_SUBREDDIT = "QUERY_SUBREDDIT"
    }
}




