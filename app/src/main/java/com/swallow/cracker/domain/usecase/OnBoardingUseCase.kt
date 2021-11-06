package com.swallow.cracker.domain.usecase

import com.swallow.cracker.domain.repository.OnBoardingRepository

class OnBoardingUseCase constructor(
    private val onBoardingRepository: OnBoardingRepository
) {
    fun getOnBoardingData() = onBoardingRepository.getOnBoardingData()
}