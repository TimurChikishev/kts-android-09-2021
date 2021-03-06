package com.swallow.cracker.ui.viewmodels

import android.content.Intent
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swallow.cracker.R
import com.swallow.cracker.domain.usecase.AuthUseCase
import com.swallow.cracker.domain.usecase.UserPreferencesUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import net.openid.appauth.AuthorizationService
import net.openid.appauth.TokenRequest

class AuthViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val userPreferencesUseCase: UserPreferencesUseCase,
    private val authUseCase: AuthUseCase,
    private val authService: AuthorizationService
) : ViewModel() {

    private var loadingSavedState = savedStateHandle.get<Boolean>(LOADING_KEY) ?: false
        set(value) {
            field = value
            savedStateHandle.set(LOADING_KEY, value)
        }

    private val loadingMutableStateFlow = MutableStateFlow(loadingSavedState)
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
        authUseCase.performTokenRequest(
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
        userPreferencesUseCase.updateAuthToken(token)
    }

    private suspend fun saveAuthRefreshToken(refreshToken: String) {
        userPreferencesUseCase.updateAuthRefreshToken(refreshToken)
    }

    suspend fun openLoginPage() {
        val openAuthPageIntent = authService.getAuthorizationRequestIntent(
            authUseCase.getAuthRequest()
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