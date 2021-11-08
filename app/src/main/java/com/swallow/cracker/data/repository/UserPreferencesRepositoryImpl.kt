package com.swallow.cracker.data.repository

import androidx.datastore.core.DataStore
import com.swallow.cracker.UserPreferences
import com.swallow.cracker.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException

class UserPreferencesRepositoryImpl constructor(
    private val userPreferencesStore: DataStore<UserPreferences>
) : UserPreferencesRepository {

    override val userPreferencesFlow: Flow<UserPreferences> = userPreferencesStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                emit(UserPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }

    override suspend fun updateOnBoardingShown(onBoardingShown: Boolean) {
        userPreferencesStore.updateData { preferences ->
            preferences.toBuilder().setOnBoardingShown(onBoardingShown).build()
        }
    }

    override suspend fun updateAuthToken(authToken: String) {
        userPreferencesStore.updateData { preferences ->
            preferences.toBuilder().setAuthToken(authToken).build()
        }
    }

    override suspend fun updateAuthRefreshToken(refreshToken: String) {
        userPreferencesStore.updateData { preferences ->
            preferences.toBuilder().setAuthRefreshToken(refreshToken).build()
        }
    }

    override suspend fun updateAccountId(id: String) {
        userPreferencesStore.updateData { preferences ->
            preferences.toBuilder().setCurrentAccountId(id).build()
        }
    }

    override suspend fun clearAuthToken() {
        userPreferencesStore.updateData { preferences ->
            preferences.toBuilder().clearAuthToken().build()
        }
    }

    override suspend fun clearAuthRefreshToken() {
        userPreferencesStore.updateData { preferences ->
            preferences.toBuilder().clearAuthRefreshToken().build()
        }
    }

    override suspend fun clearCurrentAccountId() {
        userPreferencesStore.updateData { preferences ->
            preferences.toBuilder().clearCurrentAccountId().build()
        }
    }
}