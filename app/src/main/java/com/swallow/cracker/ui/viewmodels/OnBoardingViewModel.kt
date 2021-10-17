package com.swallow.cracker.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.swallow.cracker.data.AuthRepository
import com.swallow.cracker.data.OnBoardingRepository
import com.swallow.cracker.ui.model.OnBoardingUI
import com.swallow.cracker.ui.model.TokenState
import com.swallow.cracker.utils.set
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class OnBoardingViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val onBoardingRepository = OnBoardingRepository(application)
    private val authRepository = AuthRepository(application)

    private val tokenMutableStateFlow = MutableStateFlow<TokenState>(TokenState.Default)
    private val firstLaunchMutableStateFlow = MutableStateFlow<Boolean?>(null)

    val firstLaunchFlow: Flow<Boolean?>
        get() = firstLaunchMutableStateFlow

    val authToken: Flow<TokenState>
        get() = tokenMutableStateFlow

    private var tokenJob: Job? = null
    private var firstLaunchJob: Job? = null

    fun getToken() {
        tokenJob?.cancel()
        tokenJob = viewModelScope.launch {
            authRepository.getToken()
                .map { it }
                .catch { error(it) }
                .collect {
                    val tokenState = it?.let { TokenState.IsToken(it) } ?: TokenState.IsNoToken
                    tokenMutableStateFlow.set(tokenState)
                }
        }
    }

    fun checkFirstLaunch() {
        firstLaunchJob?.cancel()
        firstLaunchJob = viewModelScope.launch {
            onBoardingRepository.getFirstLaunch()
                .map { it }
                .catch { error(it) }
                .collect {
                    firstLaunchMutableStateFlow.set(it)
                }
        }
    }

    fun initFirstLaunch() {
        viewModelScope.launch {
            onBoardingRepository.initFirstLaunch()
        }
    }

    fun getOnBoardingData(): List<OnBoardingUI> =
        onBoardingRepository.getOnBoardingData()
}

