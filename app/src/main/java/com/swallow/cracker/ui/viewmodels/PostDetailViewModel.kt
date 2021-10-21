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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PostDetailViewModel : ViewModel() {

    private val repository = RedditRepository()

    private var savePostMutableStateFlow = MutableStateFlow<Boolean?>(null)
    private var savePostIsClickableMutableStateFlow = MutableStateFlow(true)
    private var votePostMutableStateFlow = MutableStateFlow<Boolean?>(null)
    private var voteIsClickableMutableStateFlow = MutableStateFlow(true)
    private var eventMessageMutableStateFlow = MutableStateFlow<Message<*>?>(null)

    val savePost: StateFlow<Boolean?>
        get() = savePostMutableStateFlow

    val savePostIsClickable: StateFlow<Boolean>
        get() = savePostIsClickableMutableStateFlow

    val eventMessage: StateFlow<Message<*>?>
        get() = eventMessageMutableStateFlow

    val votePost: StateFlow<Boolean?>
        get() = votePostMutableStateFlow

    val votePostIsClickable: StateFlow<Boolean>
        get() = voteIsClickableMutableStateFlow

    private var currentSavePostJob: Job? = null
    private var currentVotePostJob: Job? = null


    fun savePost(item: RedditItem){
        savePostIsClickableMutableStateFlow.set(false)
        currentSavePostJob?.cancel()
        currentSavePostJob = viewModelScope.launch {
            runCatching {
                repository.savePost(item)
                    .map { it }
                    .flowOn(Dispatchers.IO)
                    .catch {
                        savePostIsClickableMutableStateFlow.set(true)
                        eventMessageMutableStateFlow.set(Message(R.string.post_saved_error))
                        eventMessageMutableStateFlow.set(null)
                    }
                    .flowOn(Dispatchers.Main)
                    .collect {
                        savePostIsClickableMutableStateFlow.set(true)
                        savePostMutableStateFlow.set(true)
                        eventMessageMutableStateFlow.set(Message(R.string.post_saved))
                        savePostMutableStateFlow.set(null)
                        eventMessageMutableStateFlow.set(null)
                    }
            }
        }
    }

    fun unSavePost(item: RedditItem) {
        savePostIsClickableMutableStateFlow.set(false)
        currentSavePostJob?.cancel()
        currentSavePostJob = viewModelScope.launch {
            runCatching {
                repository.unSavePost(item)
                    .map { it }
                    .flowOn(Dispatchers.IO)
                    .catch {
                        savePostIsClickableMutableStateFlow.set(true)
                        eventMessageMutableStateFlow.set(Message(R.string.post_unsaved_error))
                        eventMessageMutableStateFlow.set(null)
                    }
                    .flowOn(Dispatchers.Main)
                    .collect {
                        savePostIsClickableMutableStateFlow.set(true)
                        savePostMutableStateFlow.set(false)
                        eventMessageMutableStateFlow.set(Message(R.string.post_unsaved))
                        savePostMutableStateFlow.set(null)
                        eventMessageMutableStateFlow.set(null)
                    }
            }
        }
    }

    fun votePost(item: RedditItem, likes: Boolean) {
        voteIsClickableMutableStateFlow.set(false)
        currentVotePostJob?.cancel()
        currentVotePostJob = viewModelScope.launch {
            runCatching {
                repository.votePost(item, likes)
                    .map { it }
                    .flowOn(Dispatchers.IO)
                    .catch {
                        voteIsClickableMutableStateFlow.set(true)
                        eventMessageMutableStateFlow.set(Message(R.string.vote_error))
                        eventMessageMutableStateFlow.set(null)
                    }
                    .flowOn(Dispatchers.Main)
                    .collect {
                        voteIsClickableMutableStateFlow.set(true)
                        votePostMutableStateFlow.set(likes)
                        votePostMutableStateFlow.set(null)
                    }
            }
        }
    }
}

