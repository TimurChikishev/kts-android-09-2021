package com.swallow.cracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swallow.cracker.R
import com.swallow.cracker.data.repository.RedditRepository
import com.swallow.cracker.data.repository.Repository
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val userPreferences = Repository.userPreferencesRepository
    private val redditRepository = RedditRepository()

    private val toastChannel = Channel<Int>(Channel.BUFFERED)
    private var logoutStateFlow = Channel<Boolean>(Channel.BUFFERED)

    val toastStateFlow: Flow<Int>
        get() = toastChannel.receiveAsFlow()

    val logout: Flow<Boolean>
        get() = logoutStateFlow.receiveAsFlow()

    private var logoutJob: Job? = null

    fun logout() {
        logoutJob?.cancel()
        logoutJob = viewModelScope.launch {
            runCatching {
                redditRepository.clearDataBase()
                userPreferences.clearAuthToken()
                userPreferences.clearAuthRefreshToken()
            }.onSuccess {
                logoutStateFlow.send(true)
            }.onFailure {
                toastChannel.send(R.string.logout_failure)
            }
        }
    }
}