package com.swallow.cracker.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.swallow.cracker.data.OnBoardingRepository
import com.swallow.cracker.ui.model.OnBoardingUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class OnBoardingViewModel (
    application: Application
) : AndroidViewModel(application) {

    private val repository = OnBoardingRepository(application)

    val firstLaunchFlow: Flow<Boolean>
        get() = repository.observe()

    fun initFirstLaunch() {
        viewModelScope.launch {
            repository.initFirstLaunch()
        }
    }

    fun getOnBoardingData(): List<OnBoardingUI> =
        repository.getOnBoardingData()
}