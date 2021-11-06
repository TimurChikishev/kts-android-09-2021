package com.swallow.cracker.data.repository

import com.swallow.cracker.R
import com.swallow.cracker.domain.repository.OnBoardingRepository
import com.swallow.cracker.ui.model.OnBoardingUI

class OnBoardingRepositoryImpl : OnBoardingRepository {

    override fun getOnBoardingData() = listOf(
        OnBoardingUI(
            image = R.drawable.ic_launcher_background,
            title = "First Title",
            description = "First description special for on boarding fragment"
        ), OnBoardingUI(
            image = R.drawable.ic_launcher_background,
            title = "Second Title",
            description = "Second description special for on boarding fragment"
        ), OnBoardingUI(
            image = R.drawable.ic_launcher_background,
            title = "Third Title",
            description = "Third description special for on boarding fragment"
        )
    )

}
