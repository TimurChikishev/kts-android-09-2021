package com.swallow.cracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swallow.cracker.domain.usecase.OnBoardingUseCase
import com.swallow.cracker.domain.usecase.UserPreferencesUseCase
import com.swallow.cracker.ui.model.OnBoardingUI
import kotlinx.coroutines.launch

class OnBoardingViewModel constructor(
    private val userPreferencesUseCase: UserPreferencesUseCase,
    private val onBoardingUseCase: OnBoardingUseCase
) : ViewModel() {

    fun getOnBoardingData(): List<OnBoardingUI> =
        onBoardingUseCase.getOnBoardingData()

    val userPreferencesFlow = userPreferencesUseCase.userPreferencesFlow

    fun updateOnboardShown(onBoardingShown: Boolean) {
        viewModelScope.launch {
            userPreferencesUseCase.updateOnBoardingShown(onBoardingShown)
        }
    }
}

