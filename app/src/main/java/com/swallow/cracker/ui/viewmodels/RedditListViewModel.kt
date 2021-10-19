package com.swallow.cracker.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.swallow.cracker.data.RedditMapper
import com.swallow.cracker.data.RedditRemoteMediator
import com.swallow.cracker.data.api.Networking
import com.swallow.cracker.data.config.NetworkConfig
import com.swallow.cracker.data.database.RedditDatabase
import com.swallow.cracker.ui.model.QuerySubreddit
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class RedditListViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val defaultItems = QuerySubreddit(
        subreddit = "Popular",
        category = "top",
        limit = "10"
    )

    private var querySavedState =
        savedStateHandle.get<QuerySubreddit>(QUERY_SUBREDDIT) ?: defaultItems
        set(value) {
            field = value
            savedStateHandle.set(QUERY_SUBREDDIT, value)
        }

    private val redditDatabase = RedditDatabase.create(application)

    @OptIn(ExperimentalPagingApi::class)
    val items = Pager(
        config = PagingConfig(
            pageSize = NetworkConfig.PAGE_SIZE,
            enablePlaceholders = false,
            maxSize = NetworkConfig.MAX_SIZE,
            initialLoadSize = NetworkConfig.INITIAL_LOAD_SIZE,
            prefetchDistance = NetworkConfig.PAGE_SIZE / 2
        ),
        remoteMediator = RedditRemoteMediator(querySavedState, Networking.redditApiOAuth, redditDatabase),
        pagingSourceFactory = { redditDatabase.redditPostsDao().getPosts() }
    ).flow
        .map { RedditMapper.replaceRedditPostToRedditItem(it) }
        .cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    companion object {
        private const val QUERY_SUBREDDIT = "QUERY_SUBREDDIT"
    }
}




