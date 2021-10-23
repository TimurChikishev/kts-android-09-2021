package com.swallow.cracker.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swallow.cracker.R
import com.swallow.cracker.data.mapper.RedditMapper
import com.swallow.cracker.data.repository.RedditRepository
import com.swallow.cracker.data.repository.Repository
import com.swallow.cracker.ui.model.ProfileInfo
import com.swallow.cracker.utils.set
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var profileInfo = savedStateHandle.get<ProfileInfo>(KEY_PROFILE_INFO)
        set(value) {
            field = value
            savedStateHandle.set(KEY_PROFILE_INFO, value)
        }

    private val userPreferences = Repository.userPreferencesRepository
    private val redditRepository = RedditRepository()

    private val toastChannel = Channel<Int>(Channel.BUFFERED)
    private var logoutChannel = Channel<Boolean>(Channel.BUFFERED)
    private var profileInfoChannel = MutableStateFlow(profileInfo)

    val toastFlow: Flow<Int>
        get() = toastChannel.receiveAsFlow()

    val logoutFlow: Flow<Boolean>
        get() = logoutChannel.receiveAsFlow()

    val profileInfoFlow: StateFlow<ProfileInfo?>
        get() = profileInfoChannel

    private var logoutJob: Job? = null
    private var profileInfoJob: Job? = null

    fun logout() {
        logoutJob?.cancel()
        logoutJob = viewModelScope.launch {
            runCatching {
                redditRepository.clearDataBase()
                userPreferences.clearAuthToken()
                userPreferences.clearAuthRefreshToken()
            }.onSuccess {
                logoutChannel.send(true)
            }.onFailure {
                toastChannel.send(R.string.logout_failure)
            }
        }
    }

    fun getProfileInfo() {
        profileInfoJob?.cancel()
        profileInfoJob = viewModelScope.launch {
            redditRepository.getProfileInfo()
                .flowOn(Dispatchers.IO)
                .catch {
                    toastChannel.send(R.string.data_acquisition_error)
                }
                .flowOn(Dispatchers.Main)
                .collect { me ->
                    me?.let { profileInfoChannel.set(RedditMapper.remoteProfileToUi(me)) }
                }
        }
    }

    companion object {
        private const val KEY_PROFILE_INFO = "KEY_PROFILE_INFO"
    }
}