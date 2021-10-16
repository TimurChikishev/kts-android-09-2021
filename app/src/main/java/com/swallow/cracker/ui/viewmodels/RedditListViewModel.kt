package com.swallow.cracker.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.swallow.cracker.data.RedditRepository
import com.swallow.cracker.ui.model.QuerySubreddit
import com.swallow.cracker.ui.model.RedditItems
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

class RedditListViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val repository = RedditRepository()

    private var querySavedState =
        savedStateHandle.get<QuerySubreddit>(QUERY_SUBREDDIT) ?: QuerySubreddit(
            subreddit = "Popular",
            category = "top",
            limit = "20"
        )
        set(value) {
            field = value
            savedStateHandle.set(QUERY_SUBREDDIT, value)
        }

    private val queryMutableStateFlow = MutableStateFlow(querySavedState)

    @OptIn(ExperimentalCoroutinesApi::class)
    val posts: StateFlow<PagingData<RedditItems>> = queryMutableStateFlow
        .map(::newPager)
        .flatMapLatest { pager -> pager.flow }
        .catch { error(it) }
        .cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.Eagerly, PagingData.empty())

    private fun newPager(query: QuerySubreddit): Pager<String, RedditItems> {
        return repository.getPager(query)
    }

    companion object {
        private const val QUERY_SUBREDDIT = "QUERY_SUBREDDIT"
    }
}




