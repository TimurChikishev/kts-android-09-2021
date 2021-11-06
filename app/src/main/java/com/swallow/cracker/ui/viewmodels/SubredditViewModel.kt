package com.swallow.cracker.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swallow.cracker.R
import com.swallow.cracker.domain.usecase.GetPostsUseCase
import com.swallow.cracker.ui.model.Message
import com.swallow.cracker.ui.model.Subreddit
import com.swallow.cracker.utils.set
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
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
    private var eventMessageChannel = Channel<Message<*>>(Channel.BUFFERED)
    private var subscribeChannel = Channel<Boolean>(Channel.BUFFERED)

    val subredditInfo: StateFlow<Subreddit?>
        get() = subredditInfoMutableStateFlow

    val isLoading: StateFlow<Boolean?>
        get() = isLoadingMutableStateFlow

    val eventMessage: Flow<Message<*>>
        get() = eventMessageChannel.receiveAsFlow()

    val subscribe: Flow<Boolean>
        get() = subscribeChannel.receiveAsFlow()

    private var subredditInfoJob: Job? = null
    private var subscribeJob: Job? = null

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

    fun subscribeToSubreddit(subreddit: Subreddit){
        subscribe(subreddit,"sub")
    }

    fun unsubscribeFromSubreddit(subreddit: Subreddit){
        subscribe(subreddit,"unsub")
    }

    private fun subscribe(subreddit: Subreddit, action: String) {
        subscribeJob?.cancel()
        subscribeJob = viewModelScope.launch {
            getPostsUseCase.subscribeSubreddit(action, subreddit)
                .map { it }
                .flowOn(Dispatchers.IO)
                .catch {
                    eventMessageChannel.send(Message(R.string.error_subscribing_to_a_subreddit))
                }
                .flowOn(Dispatchers.Main)
                .collect {
                    when(action){
                        "sub" -> subscribeChannel.send(true)
                        "unsub" -> subscribeChannel.send(false)
                    }
                }
        }
    }

    companion object {
        private const val KEY_SUBREDDIT_INFO = "KEY_SUBREDDIT_INFO"
    }
}