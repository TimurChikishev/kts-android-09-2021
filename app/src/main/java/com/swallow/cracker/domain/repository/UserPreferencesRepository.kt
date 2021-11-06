package com.swallow.cracker.domain.repository

import com.swallow.cracker.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val userPreferencesFlow: Flow<UserPreferences>

    suspend fun updateOnBoardingShown(onBoardingShown: Boolean)

    suspend fun updateAuthToken(authToken: String)

    suspend fun updateAuthRefreshToken(refreshToken: String)

    suspend fun updateAccountId(id: String)

    suspend fun clearAuthToken()

    suspend fun clearAuthRefreshToken()

    suspend fun clearCurrentAccountId()
}