package com.swallow.cracker.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
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
import com.swallow.cracker.ui.model.RedditListQuery
import com.swallow.cracker.utils.set
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import timber.log.Timber

open class RedditListViewModel constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getPostsUseCase: GetPostsUseCase
) : ViewModel() {

    private var checkedItemSavedState = savedStateHandle.get<Int>(SORTED_KEY) ?: 0
        set(value) {
            field = value
            savedStateHandle.set(SORTED_KEY, value)
        }

    private val checkedItemMutableStateFlow = MutableStateFlow(checkedItemSavedState)
    val checkedItem: MutableStateFlow<Int>
        get() = checkedItemMutableStateFlow

    fun updateCheckedItem(index: Int){
        checkedItemMutableStateFlow.set(index)
    }

    private val _query = MutableStateFlow(RedditListQuery())
    val query: StateFlow<RedditListQuery> = _query.asStateFlow()

    private var eventMessageMutableStateFlow = Channel<Message<*>> (Channel.BUFFERED)

    val eventMessage: Flow<Message<*>>
        get() = eventMessageMutableStateFlow.receiveAsFlow()

    private var currentSavePostJob: Job? = null
    private var currentVotePostJob: Job? = null

    @ExperimentalCoroutinesApi
    @OptIn(FlowPreview::class)
    val listingItems = query.map { q -> getPostsUseCase.getNewListingPager(q.fullQuery) }
        .flatMapLatest { pager -> pager.flow }
        .map {  RedditMapper.mapPagingDataRemoteRedditPostToUi(it.filter { item -> item.query == query.value.fullQuery }) }
        .catch { Timber.tag("ERROR").d(it) }
        .cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    @ExperimentalCoroutinesApi
    @OptIn(FlowPreview::class)
    val searchItems = query.map { q -> getPostsUseCase.getNewSearchPager(q.fullQuery) }
        .flatMapLatest { pager -> pager.flow }
        .map {  RedditMapper.mapPagingDataRemoteRedditPostToUi(it.filter { item -> item.query == query.value.fullQuery }) }
        .catch { Timber.tag("ERROR").d(it) }
        .cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())


    fun setQuery(query: String) {
        _query.tryEmit(RedditListQuery(query = query))
    }

    fun setSorted(sorted: String) {
        _query.tryEmit(
            RedditListQuery(
                query = _query.value.query,
                sorted = sorted
            )
        )
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

    companion object {
        private const val SORTED_KEY = "SORTED_KEY"
    }
}


