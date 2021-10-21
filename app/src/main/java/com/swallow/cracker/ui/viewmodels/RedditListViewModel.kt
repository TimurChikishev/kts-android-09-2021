package com.swallow.cracker.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.swallow.cracker.R
import com.swallow.cracker.data.RedditMapper
import com.swallow.cracker.data.repository.RedditRepository
import com.swallow.cracker.ui.model.Message
import com.swallow.cracker.ui.model.QuerySubreddit
import com.swallow.cracker.ui.model.RedditItem
import com.swallow.cracker.utils.set
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RedditListViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val redditRepository = RedditRepository()

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

    val items = redditRepository.getPostPager(querySavedState)
        .map { RedditMapper.replaceRedditPostToRedditItem(it) }
        .cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())


    private var eventMessageMutableStateFlow = MutableStateFlow<Message<*>?>(null)

    val eventMessage: StateFlow<Message<*>?>
        get() = eventMessageMutableStateFlow

    private var currentSavePostJob: Job? = null
    private var currentVotePostJob: Job? = null

    fun savePost(item: RedditItem) {
        currentSavePostJob?.cancel()
        currentSavePostJob = viewModelScope.launch {
            runCatching {
                redditRepository.savePost(item)
                    .map { it }
                    .flowOn(Dispatchers.IO)
                    .catch {
                        eventMessageMutableStateFlow.set(Message(R.string.post_unsaved_error))
                        eventMessageMutableStateFlow.set(null)
                    }
                    .flowOn(Dispatchers.Main)
                    .collect {
                        eventMessageMutableStateFlow.set(Message(R.string.post_unsaved))
                        eventMessageMutableStateFlow.set(null)
                    }
            }
        }
    }

    fun unSavePost(item: RedditItem) {
        currentSavePostJob?.cancel()
        currentSavePostJob = viewModelScope.launch {
            runCatching {
                redditRepository.unSavePost(item)
                    .map { it }
                    .flowOn(Dispatchers.IO)
                    .catch {
                        eventMessageMutableStateFlow.set(Message(R.string.post_unsaved_error))
                        eventMessageMutableStateFlow.set(null)
                    }
                    .flowOn(Dispatchers.Main)
                    .collect {
                        eventMessageMutableStateFlow.set(Message(R.string.post_unsaved))
                        eventMessageMutableStateFlow.set(null)
                    }
            }
        }
    }

    fun votePost(item: RedditItem, likes: Boolean) {
        currentVotePostJob?.cancel()
        currentVotePostJob = viewModelScope.launch {
            runCatching {
                redditRepository.votePost(item, likes)
                    .map { it }
                    .flowOn(Dispatchers.IO)
                    .catch {
                        eventMessageMutableStateFlow.set(Message(R.string.vote_error))
                        eventMessageMutableStateFlow.set(null)
                    }
                    .flowOn(Dispatchers.Main)
                    .collect()
            }
        }
    }

    companion object {
        private const val QUERY_SUBREDDIT = "QUERY_SUBREDDIT"
    }
}




