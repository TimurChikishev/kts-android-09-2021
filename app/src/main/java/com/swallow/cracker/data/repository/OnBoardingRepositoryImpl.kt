package com.swallow.cracker.data.repository

import com.swallow.cracker.R
import com.swallow.cracker.domain.repository.OnBoardingRepository
import com.swallow.cracker.ui.model.OnBoardingUI

class OnBoardingRepositoryImpl : OnBoardingRepository {

    override fun getOnBoardingData() = listOf(
        OnBoardingUI(
            image = R.drawable.onboarding_first,
            title = R.string.onboarding_title_first,
            description = R.string.onboarding_first
        ), OnBoardingUI(
            image = R.drawable.onboarding_second,
            title =  R.string.onboarding_title_second,
            description = R.string.onboarding_second
        ), OnBoardingUI(
            image =  R.drawable.onboarding_third,
            title = R.string.onboarding_title_third,
            description = R.string.onboarding_third
        )
    )

}
