package com.swallow.cracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swallow.cracker.R 
import com.swallow.cracker.domain.usecase.GetPostsUseCase
import com.swallow.cracker.ui.model.Message
import com.swallow.cracker.ui.model.RedditItem
import com.swallow.cracker.ui.model.Subreddit
import com.swallow.cracker.utils.set
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

class PostDetailViewModel constructor(
    private val getPostsUseCase: GetPostsUseCase
) : ViewModel() {

    private var savePostChannel = Channel<Boolean>(Channel.BUFFERED)
    private var savePostIsClickableMutableStateFlow = MutableStateFlow(true)
    private var votePostChannel = Channel<Boolean>(Channel.BUFFERED)
    private var voteIsClickableMutableStateFlow = MutableStateFlow(true)
    private var eventMessageChannel = Channel<Message<*>>(Channel.BUFFERED)
    private var subredditInfoChannel = Channel<Subreddit>(Channel.BUFFERED)
    private var subscribeChannel = Channel<Boolean>(Channel.BUFFERED)

    val savePost: Flow<Boolean>
        get() = savePostChannel.receiveAsFlow()

    val savePostIsClickable: StateFlow<Boolean>
        get() = savePostIsClickableMutableStateFlow

    val eventMessage: Flow<Message<*>>
        get() = eventMessageChannel.receiveAsFlow()

    val votePost: Flow<Boolean>
        get() = votePostChannel.receiveAsFlow()

    val votePostIsClickable: StateFlow<Boolean>
        get() = voteIsClickableMutableStateFlow

    val subredditInfo: Flow<Subreddit>
        get() = subredditInfoChannel.receiveAsFlow()

    val subscribe: Flow<Boolean>
        get() = subscribeChannel.receiveAsFlow()

    private var currentSavePostJob: Job? = null
    private var currentVotePostJob: Job? = null
    private var subscribeJob: Job? = null
    private var subredditInfoJob: Job? = null

    fun savePost(item: RedditItem) {
        savePostIsClickableMutableStateFlow.set(false)
        currentSavePostJob?.cancel()
        currentSavePostJob = viewModelScope.launch {
            getPostsUseCase.savePost(item)
                .map { it }
                .flowOn(Dispatchers.IO)
                .catch {
                    savePostIsClickableMutableStateFlow.set(true)
                    eventMessageChannel.send(Message(R.string.post_saved_error))
                }
                .flowOn(Dispatchers.Main)
                .collect {
                    savePostIsClickableMutableStateFlow.set(true)
                    savePostChannel.send(true)
                    eventMessageChannel.send(Message(R.string.post_saved))
                }
        }
    }

    fun unSavePost(item: RedditItem) {
        savePostIsClickableMutableStateFlow.set(false)
        currentSavePostJob?.cancel()
        currentSavePostJob = viewModelScope.launch {
            getPostsUseCase.unSavePost(item)
                .map { it }
                .flowOn(Dispatchers.IO)
                .catch {
                    savePostIsClickableMutableStateFlow.set(true)
                    eventMessageChannel.send(Message(R.string.post_unsaved_error))
                }
                .flowOn(Dispatchers.Main)
                .collect {
                    savePostIsClickableMutableStateFlow.set(true)
                    savePostChannel.send(false)
                    eventMessageChannel.send(Message(R.string.post_unsaved))
                }
        }
    }

    fun votePost(item: RedditItem, likes: Boolean) {
        voteIsClickableMutableStateFlow.set(false)
        currentVotePostJob?.cancel()
        currentVotePostJob = viewModelScope.launch {
            getPostsUseCase.votePost(item, likes)
                .map { it }
                .flowOn(Dispatchers.IO)
                .catch {
                    voteIsClickableMutableStateFlow.set(true)
                    eventMessageChannel.send(Message(R.string.vote_error))
                }
                .flowOn(Dispatchers.Main)
                .collect {
                    voteIsClickableMutableStateFlow.set(true)
                    votePostChannel.send(likes)
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

    fun getSubredditInfo(subredditName: String){
        subredditInfoJob?.cancel()
        subredditInfoJob = viewModelScope.launch {
            getPostsUseCase.getSubredditInfo(subredditName)
                .map { it }
                .catch { Timber.tag("ERROR").d(it) }
                .flowOn(Dispatchers.IO)
                .collect {
                    subredditInfoChannel.send(it)
                }

        }
    }

}

