package com.swallow.cracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swallow.cracker.R
import com.swallow.cracker.data.repository.RedditRepository
import com.swallow.cracker.ui.model.Message
import com.swallow.cracker.ui.model.RedditItem
import com.swallow.cracker.utils.set
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PostDetailViewModel : ViewModel() {

    private val repository = RedditRepository()

    private var savePostMutableStateFlow = Channel<Boolean>(Channel.BUFFERED)
    private var savePostIsClickableMutableStateFlow = MutableStateFlow(true)
    private var votePostMutableStateFlow = Channel<Boolean>(Channel.BUFFERED)
    private var voteIsClickableMutableStateFlow = MutableStateFlow(true)
    private var eventMessageMutableStateFlow = Channel<Message<*>>(Channel.BUFFERED)

    val savePost: Flow<Boolean>
        get() = savePostMutableStateFlow.receiveAsFlow()

    val savePostIsClickable: StateFlow<Boolean>
        get() = savePostIsClickableMutableStateFlow

    val eventMessage: Flow<Message<*>>
        get() = eventMessageMutableStateFlow.receiveAsFlow()

    val votePost: Flow<Boolean>
        get() = votePostMutableStateFlow.receiveAsFlow()

    val votePostIsClickable: StateFlow<Boolean>
        get() = voteIsClickableMutableStateFlow

    private var currentSavePostJob: Job? = null
    private var currentVotePostJob: Job? = null


    fun savePost(item: RedditItem) {
        savePostIsClickableMutableStateFlow.set(false)
        currentSavePostJob?.cancel()
        currentSavePostJob = viewModelScope.launch {
            repository.savePost(item)
                .map { it }
                .flowOn(Dispatchers.IO)
                .catch {
                    savePostIsClickableMutableStateFlow.set(true)
                    eventMessageMutableStateFlow.send(Message(R.string.post_saved_error))
                }
                .flowOn(Dispatchers.Main)
                .collect {
                    savePostIsClickableMutableStateFlow.set(true)
                    savePostMutableStateFlow.send(true)
                    eventMessageMutableStateFlow.send(Message(R.string.post_saved))
                }
        }
    }

    fun unSavePost(item: RedditItem) {
        savePostIsClickableMutableStateFlow.set(false)
        currentSavePostJob?.cancel()
        currentSavePostJob = viewModelScope.launch {
            repository.unSavePost(item)
                .map { it }
                .flowOn(Dispatchers.IO)
                .catch {
                    savePostIsClickableMutableStateFlow.set(true)
                    eventMessageMutableStateFlow.send(Message(R.string.post_unsaved_error))
                }
                .flowOn(Dispatchers.Main)
                .collect {
                    savePostIsClickableMutableStateFlow.set(true)
                    savePostMutableStateFlow.send(false)
                    eventMessageMutableStateFlow.send(Message(R.string.post_unsaved))
                }
        }
    }

    fun votePost(item: RedditItem, likes: Boolean) {
        voteIsClickableMutableStateFlow.set(false)
        currentVotePostJob?.cancel()
        currentVotePostJob = viewModelScope.launch {
            repository.votePost(item, likes)
                .map { it }
                .flowOn(Dispatchers.IO)
                .catch {
                    voteIsClickableMutableStateFlow.set(true)
                    eventMessageMutableStateFlow.send(Message(R.string.vote_error))
                }
                .flowOn(Dispatchers.Main)
                .collect {
                    voteIsClickableMutableStateFlow.set(true)
                    votePostMutableStateFlow.send(likes)
                }
        }
    }
}

