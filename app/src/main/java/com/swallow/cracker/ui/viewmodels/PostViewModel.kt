package com.swallow.cracker.ui.viewmodels

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swallow.cracker.R
import com.swallow.cracker.data.RedditRepository
import com.swallow.cracker.ui.model.Message
import com.swallow.cracker.ui.model.PBTransition
import com.swallow.cracker.ui.model.RedditItems
import com.swallow.cracker.utils.default
import com.swallow.cracker.utils.getVoteDir
import com.swallow.cracker.utils.id
import com.swallow.cracker.utils.set
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {

    private val repository = RedditRepository()

    private var savePostMutableLiveData = MutableLiveData<PBTransition?>().default(null)
    private var savePostIsClickableMutableLiveData = MutableLiveData<Boolean>().default(true)
    private var votePostMutableLiveData = MutableLiveData<PBTransition?>().default(null)
    private var voteIsClickableMutableLiveData = MutableLiveData<Boolean>().default(true)
    private var eventMessageMutableLiveData = MutableLiveData<Message<*>?>().default(null)

    val savePost: LiveData<PBTransition?>
        get() = savePostMutableLiveData

    val savePostIsClickable: LiveData<Boolean>
        get() = savePostIsClickableMutableLiveData

    val eventMessage: LiveData<Message<*>?>
        get() = eventMessageMutableLiveData

    val votePost: LiveData<PBTransition?>
        get() = votePostMutableLiveData

    val votePostIsClickable: LiveData<Boolean>
        get() = voteIsClickableMutableLiveData

    private var currentSavePostJob: Job? = null
    private var currentVotePostJob: Job? = null

    fun savePost(category: String?, id: String, position: Int? = null) {
        savePostIsClickableMutableLiveData.set(false)
        currentSavePostJob?.cancel()
        currentSavePostJob = viewModelScope.launch {
            runCatching {
                repository.savePost(category = category, id = id)
            }.onSuccess {
                savePostIsClickableMutableLiveData.set(true)
                savePostMutableLiveData.set(PBTransition(flag = true, position = position))
                savePostMutableLiveData.postValue(null)
                eventMessageMutableLiveData.set(Message(R.string.post_saved))
            }.onFailure {
                savePostIsClickableMutableLiveData.set(true)
                eventMessageMutableLiveData.set(Message(R.string.post_saved_error))
            }
            eventMessageMutableLiveData.postValue(null) // чтобы сообщение не появилось при перевороте
        }
    }

    fun unSavePost(id: String, position: Int? = null) {
        savePostIsClickableMutableLiveData.postValue(false)
        currentSavePostJob?.cancel()
        currentSavePostJob = viewModelScope.launch {
            runCatching {
                repository.unSavePost(id = id)
            }.onSuccess {
                savePostIsClickableMutableLiveData.set(true)
                savePostMutableLiveData.set(PBTransition(flag = false, position = position))
                savePostMutableLiveData.postValue(null)
                eventMessageMutableLiveData.set(Message(R.string.post_unsaved))
            }.onFailure {
                savePostIsClickableMutableLiveData.set(true)
                eventMessageMutableLiveData.set(Message(R.string.post_unsaved_error))
            }
            eventMessageMutableLiveData.postValue(null) // чтобы сообщение не появилось при перевороте
        }
    }

    fun votePost(item: RedditItems, likes: Boolean, position: Int? = null) {
        voteIsClickableMutableLiveData.set(false)
        currentVotePostJob?.cancel()
        currentVotePostJob = viewModelScope.launch {
            runCatching {
                repository.votePost(dir = item.getVoteDir(likes), id = item.id())
            }.onSuccess {
                voteIsClickableMutableLiveData.set(true)
                votePostMutableLiveData.set(PBTransition(flag = likes, position = position))
                votePostMutableLiveData.postValue(null) // чтобы не изменялось состояние vote при перевороте
            }.onFailure {
                voteIsClickableMutableLiveData.set(true)
                eventMessageMutableLiveData.set(Message(R.string.vote_error))
                eventMessageMutableLiveData.set(null)
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

