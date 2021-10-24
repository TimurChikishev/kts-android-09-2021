package com.swallow.cracker.ui.viewmodels

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.swallow.cracker.R
import com.swallow.cracker.data.repository.AuthRepository
import com.swallow.cracker.data.repository.Repository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationService
import net.openid.appauth.TokenRequest

class AuthViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private var loadingSavedState = savedStateHandle.get<Boolean>(LOADING_KEY) ?: false
        set(value) {
            field = value
            savedStateHandle.set(LOADING_KEY, value)
        }

    private val loadingMutableStateFlow = MutableStateFlow(loadingSavedState)

    private val authRepository = AuthRepository()
    private val userPreferencesRepository = Repository.userPreferencesRepository
    private val authService: AuthorizationService = AuthorizationService(getApplication())
    private val openAuthPageChannel = Channel<Intent>(Channel.BUFFERED)
    private val toastChannel = Channel<Int>(Channel.BUFFERED)

    private val authSuccessChannel = Channel<Unit>(Channel.BUFFERED)

    val openAuthPageStateFlow: Flow<Intent>
        get() = openAuthPageChannel.receiveAsFlow()

    val loadingStateFlow: StateFlow<Boolean>
        get() = loadingMutableStateFlow

    val toastStateFlow: Flow<Int>
        get() = toastChannel.receiveAsFlow()

    val authSuccessStateFlow: Flow<Unit>
        get() = authSuccessChannel.receiveAsFlow()

    suspend fun onAuthCodeFailed() {
        toastChannel.send(R.string.auth_canceled)
    }

    fun onAuthCodeReceived(tokenRequest: TokenRequest) {
        loadingMutableStateFlow.value = true
        authRepository.performTokenRequest(
            authService = authService,
            tokenRequest = tokenRequest,
            onComplete = { token, refreshToken ->
                viewModelScope.launch {
                    saveAuthToken(token)
                    saveAuthRefreshToken(refreshToken)
                    loadingMutableStateFlow.value = false
                    authSuccessChannel.send(Unit)
                }
            },
            onError = {
                viewModelScope.launch {
                    loadingMutableStateFlow.value = false
                    toastChannel.send(R.string.auth_canceled)
                }
            }
        )
    }

    private suspend fun saveAuthToken(token: String) {
        userPreferencesRepository.updateAuthToken(token)
    }

    private suspend fun saveAuthRefreshToken(refreshToken: String) {
        userPreferencesRepository.updateAuthRefreshToken(refreshToken)
    }

    suspend fun openLoginPage() {
        val openAuthPageIntent = authService.getAuthorizationRequestIntent(
            authRepository.getAuthRequest()
        )

        openAuthPageChannel.send(openAuthPageIntent)
    }

    override fun onCleared() {
        super.onCleared()
        authService.dispose()
    }

    companion object {
        private const val LOADING_KEY = "LOADING_KEY"
    }
}