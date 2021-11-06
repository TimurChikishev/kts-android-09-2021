package com.swallow.cracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.swallow.cracker.R
import com.swallow.cracker.data.mapper.RedditMapper
import com.swallow.cracker.domain.usecase.GetPostsUseCase
import com.swallow.cracker.ui.model.Message
import com.swallow.cracker.ui.model.RedditItem
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import timber.log.Timber

open class RedditListViewModel constructor(
    private val getPostsUseCase: GetPostsUseCase
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private var eventMessageMutableStateFlow = Channel<Message<*>> (Channel.BUFFERED)

    val eventMessage: Flow<Message<*>>
        get() = eventMessageMutableStateFlow.receiveAsFlow()

    private var currentSavePostJob: Job? = null
    private var currentVotePostJob: Job? = null

    @ExperimentalCoroutinesApi
    @OptIn(FlowPreview::class)
    val listingItems = query.map { q -> getPostsUseCase.getNewListingPager(q) }
        .flatMapLatest { pager -> pager.flow }
        .map {  RedditMapper.mapPagingDataRemoteRedditPostToUi(it.filter { item -> item.query == query.value }) }
        .catch { Timber.tag("ERROR").d(it) }
        .cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    @ExperimentalCoroutinesApi
    @OptIn(FlowPreview::class)
    val searchItems = query.map { q -> getPostsUseCase.getNewSearchPager(q) }
        .flatMapLatest { pager -> pager.flow }
        .map {  RedditMapper.mapPagingDataRemoteRedditPostToUi(it.filter { item -> item.query == query.value }) }
        .catch { Timber.tag("ERROR").d(it) }
        .cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())


    fun setQuery(query: String) {
        _query.tryEmit(query)
    }

    fun savePost(item: RedditItem) {
        currentSavePostJob?.cancel()
        currentSavePostJob = viewModelScope.launch {
            getPostsUseCase.savePost(item)
                .map { it }
                .flowOn(Dispatchers.IO)
                .catch {
                    eventMessageMutableStateFlow.send(Message(R.string.post_saved_error))
                }
                .flowOn(Dispatchers.Main)
                .collect {
                    eventMessageMutableStateFlow.send(Message(R.string.post_saved))
                }
        }
    }

    fun unSavePost(item: RedditItem) {
        currentSavePostJob?.cancel()
        currentSavePostJob = viewModelScope.launch {
            getPostsUseCase.unSavePost(item)
                .map { it }
                .flowOn(Dispatchers.IO)
                .catch {
                    eventMessageMutableStateFlow.send(Message(R.string.post_unsaved_error))
                }
                .flowOn(Dispatchers.Main)
                .collect {
                    eventMessageMutableStateFlow.send(Message(R.string.post_unsaved))
                }
        }
    }

    fun votePost(item: RedditItem, likes: Boolean) {
        currentVotePostJob?.cancel()
        currentVotePostJob = viewModelScope.launch {
            getPostsUseCase.votePost(item, likes)
                .map { it }
                .flowOn(Dispatchers.IO)
                .catch {
                    eventMessageMutableStateFlow.send(Message(R.string.vote_error))
                }
                .flowOn(Dispatchers.Main)
                .collect()
        }
    }
}




