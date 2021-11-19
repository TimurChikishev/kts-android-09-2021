package com.swallow.cracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.swallow.cracker.data.mapper.RedditMapper
import com.swallow.cracker.domain.usecase.GetPostsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber

class SubscriptionsViewModel constructor(
    getPostsUseCase: GetPostsUseCase
) : ViewModel() {

    @ExperimentalCoroutinesApi
    @OptIn(FlowPreview::class)
    val subscriptions = getPostsUseCase.getNewMineSubscriptionsPager()
        .map { items -> items.map { RedditMapper.mapRemoteSubredditToUi(it) } }
        .catch { Timber.tag("ERROR").d(it) }
        .cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
}