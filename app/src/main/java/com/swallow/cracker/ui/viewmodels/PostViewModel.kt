package com.swallow.cracker.ui.viewmodels

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swallow.cracker.R
import com.swallow.cracker.data.RedditRepository
import com.swallow.cracker.ui.modal.LoadState
import com.swallow.cracker.ui.modal.RedditList
import com.swallow.cracker.ui.modal.VoteState
import com.swallow.cracker.utils.default
import com.swallow.cracker.utils.id
import com.swallow.cracker.utils.getVoteDir
import com.swallow.cracker.utils.set
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {

    private val repository = RedditRepository()
    private var currentSearchJob: Job? = null
    private var savePostMutableLiveData = MutableLiveData<LoadState>().default(LoadState.OnDefault)
    private var votePostMutableLiveData = MutableLiveData<VoteState>().default(VoteState.OnDefault)

    val savePost: LiveData<LoadState>
        get() = savePostMutableLiveData

    val votePost: LiveData<VoteState>
        get() = votePostMutableLiveData

    fun savePost(category: String?, id: String) {
        currentSearchJob = viewModelScope.launch {
            runCatching {
                repository.savePost(category = category, id = id)
            }.onSuccess {
                savePostMutableLiveData.set(LoadState.OnSuccess)
            }.onFailure {
                savePostMutableLiveData.set(LoadState.OnError(R.string.save_error))
            }
        }
    }

    fun unSavePost(id: String) {
        currentSearchJob = viewModelScope.launch {
            runCatching {
                repository.unSavePost(id = id)
            }.onSuccess {
                savePostMutableLiveData.set(LoadState.OnSuccess)
            }.onFailure {
                savePostMutableLiveData.set(LoadState.OnError(R.string.unsave_error))
            }
        }
    }

    private fun votePost(dir: Int, id: String, likes: Boolean, position: Int? = null) {
        currentSearchJob = viewModelScope.launch {
            runCatching {
                repository.votePost(dir = dir, id = id)
            }.onSuccess {
                votePostMutableLiveData.set(VoteState.OnSuccess(likes = likes, position = position))
                votePostMutableLiveData.set(VoteState.OnDefault)
            }.onFailure {
                votePostMutableLiveData.set(VoteState.OnError(message = R.string.vote_error))
            }
        }
    }

    fun vote(item: RedditList, likes: Boolean, position: Int? = null) {
        val dir = item.getVoteDir(likes)
        votePost(dir = dir, id = item.id(), likes = likes, position = position)
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

