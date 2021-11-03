package com.swallow.cracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swallow.cracker.data.repository.RedditRepository
import com.swallow.cracker.ui.model.Subreddit
import com.swallow.cracker.utils.set
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

class SubredditViewModel : ViewModel() {

    private val redditRepository = RedditRepository()

    private val subredditInfoMutableStateFlow = MutableStateFlow<Subreddit?>(null)

    val subredditInfo: StateFlow<Subreddit?>
        get() = subredditInfoMutableStateFlow

    private var subredditInfoJob: Job? = null

    @OptIn(InternalCoroutinesApi::class)
    fun getSubredditInfo(subredditName: String){
        subredditInfoJob?.cancel()
        subredditInfoJob = viewModelScope.launch {
            redditRepository.getSubredditInfo(subredditName)
                .catch { Timber.tag("TAG").d(it) }
                .flowOn(Dispatchers.IO)
                .collect {
                    Timber.tag("TAG").d("INFO $it")
                    subredditInfoMutableStateFlow.set(it)
                }
        }
    }
}