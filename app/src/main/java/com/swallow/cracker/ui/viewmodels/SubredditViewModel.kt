package com.swallow.cracker.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swallow.cracker.domain.usecase.GetPostsUseCase
import com.swallow.cracker.ui.model.Subreddit
import com.swallow.cracker.utils.set
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

class SubredditViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getPostsUseCase: GetPostsUseCase
) : ViewModel() {

    private var subredditInfoSavedState = savedStateHandle.get<Subreddit>(KEY_SUBREDDIT_INFO)
        set(value) {
            field = value
            savedStateHandle.set(KEY_SUBREDDIT_INFO, value)
        }

    private val subredditInfoMutableStateFlow = MutableStateFlow(subredditInfoSavedState)
    private val isLoadingMutableStateFlow = MutableStateFlow<Boolean?>(null)

    val subredditInfo: StateFlow<Subreddit?>
        get() = subredditInfoMutableStateFlow

    val isLoading: StateFlow<Boolean?>
        get() = isLoadingMutableStateFlow

    private var subredditInfoJob: Job? = null

    @OptIn(InternalCoroutinesApi::class)
    fun getSubredditInfo(subredditName: String){
        isLoadingMutableStateFlow.set(true)
        subredditInfoJob?.cancel()
        subredditInfoJob = viewModelScope.launch {
            getPostsUseCase.getSubredditInfo(subredditName)
                .catch { Timber.tag("TAG").d(it) }
                .flowOn(Dispatchers.IO)
                .collect {
                    subredditInfoMutableStateFlow.set(it)
                    isLoadingMutableStateFlow.set(false)
                }
        }
    }

    companion object {
        private const val KEY_SUBREDDIT_INFO = "KEY_SUBREDDIT_INFO"
    }
}