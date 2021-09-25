package com.swallow.cracker.ui.viewmodels

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.swallow.cracker.data.RedditRepository
import com.swallow.cracker.ui.modal.RedditList
import com.swallow.cracker.ui.modal.RedditMapper
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

class RedditListViewModel: ViewModel() {

    private val repository = RedditRepository()

    private val currentQuery = MutableLiveData(DEFAULT_LIMIT_VALUE)

    val posts = currentQuery.switchMap {
        repository.getTop(it).cachedIn(viewModelScope)
    }

    fun updateLimitValue(limit: String){
        currentQuery.value = limit
    }

    companion object {
        private const val DEFAULT_LIMIT_VALUE = "20"
    }
}

