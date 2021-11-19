package com.swallow.cracker.domain.usecase

import com.swallow.cracker.domain.repository.UserPreferencesRepository


class UserPreferencesUseCase constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {

    val userPreferencesFlow = userPreferencesRepository.userPreferencesFlow

    suspend fun clearAuthToken() = userPreferencesRepository.clearAuthToken()

    suspend fun clearAuthRefreshToken() = userPreferencesRepository.clearAuthRefreshToken()

    suspend fun clearCurrentAccountId() = userPreferencesRepository.clearCurrentAccountId()

    suspend fun updateAccountId(id: String) =
        userPreferencesRepository.updateAccountId(id)

    suspend fun updateAuthToken(token: String) =
        userPreferencesRepository.updateAuthToken(token)

    suspend fun updateAuthRefreshToken(refreshToken: String) =
        userPreferencesRepository.updateAuthRefreshToken(refreshToken)

    suspend fun updateOnBoardingShown(onBoardingShown: Boolean) =
        userPreferencesRepository.updateOnBoardingShown(onBoardingShown)
}