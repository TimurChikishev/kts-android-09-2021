package com.swallow.cracker.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.swallow.cracker.data.repository.OnBoardingRepository
import com.swallow.cracker.data.repository.Repository
import com.swallow.cracker.ui.model.OnBoardingUI
import kotlinx.coroutines.launch

class OnBoardingViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val onBoardingRepository = OnBoardingRepository()
    private val userPreferencesRepository = Repository.userPreferencesRepository

    fun getOnBoardingData(): List<OnBoardingUI> =
        onBoardingRepository.getOnBoardingData()

    val userPreferencesFlow = userPreferencesRepository.userPreferencesFlow

    fun updateOnboardShown(onBoardingShown: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateOnBoardingShown(onBoardingShown)
        }
    }
}

