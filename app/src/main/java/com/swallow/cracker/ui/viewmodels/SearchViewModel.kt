package com.swallow.cracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swallow.cracker.domain.usecase.GetPostsUseCase
import com.swallow.cracker.ui.model.SearchQuery
import com.swallow.cracker.ui.model.SearchQueryTransaction
import com.swallow.cracker.ui.model.Subreddit
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import timber.log.Timber

class SearchViewModel constructor(
    private val getPostsUseCase: GetPostsUseCase
) : ViewModel() {
    private val searchSubredditMutableSharedFlow = MutableSharedFlow<List<Subreddit>>()
    private val searchQueryMutableSharedFlow = MutableSharedFlow<List<SearchQuery>>()
    private val searchQueryIsSavedChannel = Channel<SearchQueryTransaction>(Channel.BUFFERED)
    private val searchQueryIsRemovedChannel = Channel<SearchQuery>(Channel.BUFFERED)

    val searchSubreddit: SharedFlow<List<Subreddit>>
        get() = searchSubredditMutableSharedFlow

    val searchQuery: SharedFlow<List<SearchQuery>>
        get() = searchQueryMutableSharedFlow

    val searchQueryIsSaved: Flow<SearchQueryTransaction>
        get() = searchQueryIsSavedChannel.receiveAsFlow()

    val searchQueryIsRemoved: Flow<SearchQuery>
        get() = searchQueryIsRemovedChannel.receiveAsFlow()

    private var searchQueryJob: Job? = null
    private var searchQuerySavedJob: Job? = null
    private var searchQueryRemovedJob: Job? = null

    fun init(): Unit = getSavedSearchQuery()

    @OptIn(FlowPreview::class)
    fun searchSubreddit(query: String) {
        searchQueryJob = viewModelScope.launch {
            getPostsUseCase.searchSubreddits(query)
                .debounce(500)
                .distinctUntilChanged()
                .catch { Timber.tag("ERROR").d(it) }
                .flowOn(Dispatchers.IO)
                .collect {
                    searchSubredditMutableSharedFlow.emit(it)
                }
        }
    }

    fun getSavedSearchQuery(timeMillis: Long = 0) {
        searchQueryJob?.cancel()
        searchQueryJob = viewModelScope.launch {
            getPostsUseCase.getSearchQuery()
                // if you quickly clear the line, then the data about the
                // subreddits will block the saved search queries, so you
                // need to postpone the call
                .onStart { delay(timeMillis) }
                .map { it ?: throw Exception("Failed to get search queries") }
                .catch { Timber.tag("ERROR").d(it) }
                .flowOn(Dispatchers.IO)
                .collect {
                    searchQueryMutableSharedFlow.emit(it)
                }
        }
    }

    fun savedSearchQuery(query: String, subreddit: Subreddit? = null) {
        searchQuerySavedJob?.cancel()
        searchQuerySavedJob = viewModelScope.launch {
            runCatching{
                getPostsUseCase.savedSearchQuery(SearchQuery(query))
            }.onSuccess {
                searchQueryIsSavedChannel.send(SearchQueryTransaction(query, subreddit, true))
            }.onFailure {
                searchQueryIsSavedChannel.send(SearchQueryTransaction(query, subreddit, false))
            }
        }
    }

    fun removeSearchQuery(item: SearchQuery) {
        searchQueryRemovedJob?.cancel()
        searchQueryRemovedJob = viewModelScope.launch {
            runCatching{
                getPostsUseCase.removeSearchQuery(item.query)
            }.onSuccess {
                searchQueryIsRemovedChannel.send(item)
            }.onFailure {
                Timber.tag("ERROR").d("Failed to delete a $item")
            }
        }
    }
}