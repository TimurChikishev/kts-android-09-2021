package com.swallow.cracker.domain.repository

import com.swallow.cracker.ui.model.OnBoardingUI

interface OnBoardingRepository {
    fun getOnBoardingData(): List<OnBoardingUI>
}