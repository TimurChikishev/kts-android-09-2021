package com.swallow.cracker.ui.viewmodels

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swallow.cracker.R
import com.swallow.cracker.data.repository.RedditRepository
import com.swallow.cracker.ui.model.Message
import com.swallow.cracker.ui.model.PBTransition
import com.swallow.cracker.ui.model.RedditItem
import com.swallow.cracker.utils.getVoteDir
import com.swallow.cracker.utils.set
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PostViewModel() : ViewModel() {

    private val repository = RedditRepository()

    private var savePostMutableStateFlow = MutableStateFlow<PBTransition?>(null)
    private var savePostIsClickableMutableStateFlow = MutableStateFlow(true)
    private var votePostMutableStateFlow = MutableStateFlow<PBTransition?>(null)
    private var voteIsClickableMutableStateFlow = MutableStateFlow(true)
    private var eventMessageMutableStateFlow = MutableStateFlow<Message<*>?>(null)

    val savePost: StateFlow<PBTransition?>
        get() = savePostMutableStateFlow

    val savePostIsClickable: StateFlow<Boolean>
        get() = savePostIsClickableMutableStateFlow

    val eventMessage: StateFlow<Message<*>?>
        get() = eventMessageMutableStateFlow

    val votePost: StateFlow<PBTransition?>
        get() = votePostMutableStateFlow

    val votePostIsClickable: StateFlow<Boolean>
        get() = voteIsClickableMutableStateFlow

    private var currentSavePostJob: Job? = null
    private var currentVotePostJob: Job? = null

    fun savePost(category: String?, id: String, position: Int? = null) {
        savePostIsClickableMutableStateFlow.set(false)
        currentSavePostJob?.cancel()
        currentSavePostJob = viewModelScope.launch {
            runCatching {
                repository.savePost(category = category, id = id)
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
                        savePostMutableStateFlow.set(PBTransition(flag = true, position = position))
                        eventMessageMutableStateFlow.set(Message(R.string.post_saved))
                        savePostMutableStateFlow.set(null)
                        eventMessageMutableStateFlow.set(null)
                    }
            }
        }
    }

    fun unSavePost(id: String, position: Int? = null) {
        savePostIsClickableMutableStateFlow.set(false)
        currentSavePostJob?.cancel()
        currentSavePostJob = viewModelScope.launch {
            runCatching {
                repository.unSavePost(id = id)
                    .map { it }
                    .flowOn(Dispatchers.IO)
                    .catch {
                        savePostIsClickableMutableStateFlow.set(true)
                        eventMessageMutableStateFlow.set(Message(R.string.post_unsaved_error))
                        eventMessageMutableStateFlow.set(null) // чтобы сообщение не появилось при перевороте
                    }
                    .flowOn(Dispatchers.Main)
                    .collect {
                        savePostIsClickableMutableStateFlow.set(true)
                        savePostMutableStateFlow.set(
                            PBTransition(
                                flag = false,
                                position = position
                            )
                        )
                        eventMessageMutableStateFlow.set(Message(R.string.post_unsaved))
                        savePostMutableStateFlow.set(null)
                        eventMessageMutableStateFlow.set(null) // чтобы сообщение не появилось при перевороте
                    }
            }
        }
    }

    fun votePost(item: RedditItem, likes: Boolean, position: Int? = null) {
        voteIsClickableMutableStateFlow.set(false)
        currentVotePostJob?.cancel()
        currentVotePostJob = viewModelScope.launch {
            runCatching {
                repository.votePost(dir = item.getVoteDir(likes), id = item.id())
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
                        votePostMutableStateFlow.set(
                            PBTransition(
                                flag = likes,
                                position = position
                            )
                        )
                        votePostMutableStateFlow.set(null) // чтобы не изменялось состояние vote при перевороте
                    }
            }
        }
    }

    fun shared(url: String): Intent = Intent.createChooser(
        Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, url)
            type = "text/plain"
        },
        null
    )
}

