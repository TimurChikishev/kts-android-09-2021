package com.swallow.cracker.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swallow.cracker.R
import com.swallow.cracker.data.mapper.RedditMapper
import com.swallow.cracker.data.repository.RedditRepository
import com.swallow.cracker.data.repository.Repository
import com.swallow.cracker.ui.model.RedditProfile
import com.swallow.cracker.utils.set
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

class ProfileViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var profileInfo = savedStateHandle.get<RedditProfile>(KEY_PROFILE_INFO)
        set(value) {
            field = value
            savedStateHandle.set(KEY_PROFILE_INFO, value)
        }

    private val userPreferencesRepo = Repository.userPreferencesRepository
    private val redditRepository = RedditRepository()

    private val toastChannel = Channel<Int>(Channel.BUFFERED)
    private var logoutChannel = Channel<Boolean>(Channel.BUFFERED)
    private var profileInfoMutableStateFlow = MutableStateFlow(profileInfo)

    val toastFlow: Flow<Int>
        get() = toastChannel.receiveAsFlow()

    val logoutFlow: Flow<Boolean>
        get() = logoutChannel.receiveAsFlow()

    val profileInfoFlow: StateFlow<RedditProfile?>
        get() = profileInfoMutableStateFlow

    private var initJob: Job? = null
    private var logoutJob: Job? = null
    private var profileInfoJob: Job? = null

    fun init() {
        initJob?.cancel()
        initJob = viewModelScope.launch {
            userPreferencesRepo.userPreferencesFlow.take(1).collect { pref -> 
                redditRepository.getProfileFromDB(pref.currentAccountId)
                    .map {
                        it ?: throw  NullPointerException(
                            "There is no information about a user with an ID equal to ${pref.currentAccountId} in the database"
                        )

                        RedditMapper.remoteProfileToUi(it)
                    }
                    .catch { Timber.tag("ERROR").d(it) }
                    .flowOn(Dispatchers.IO)
                    .collect {
                        it.let { profileInfoMutableStateFlow.set(it) }
                    }
            }
        }
    }

    fun logout() {
        logoutJob?.cancel()
        logoutJob = viewModelScope.launch {
            runCatching {
                redditRepository.clearDataBase()
                userPreferencesRepo.clearAuthToken()
                userPreferencesRepo.clearAuthRefreshToken()
                userPreferencesRepo.clearCurrentAccountId()
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
                .map { RedditMapper.remoteProfileToUi(it) }
                .catch {
                    toastChannel.send(R.string.data_acquisition_error)
                }
                .flowOn(Dispatchers.IO)
                .collect { me ->
                    profileInfoMutableStateFlow.set(me)
                    userPreferencesRepo.updateAccountId(me.id)
                }
        }
    }

    companion object {
        private const val KEY_PROFILE_INFO = "KEY_PROFILE_INFO"
    }
}