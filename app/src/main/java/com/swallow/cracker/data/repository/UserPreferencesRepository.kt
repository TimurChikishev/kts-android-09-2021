package com.swallow.cracker.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.swallow.cracker.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import timber.log.Timber
import java.io.IOException

class UserPreferencesRepository (context: Context) {

    companion object {
        private const val DATA_STORE_FILE_NAME = "user_prefs.pb"
    }

    private val Context.dataStore: DataStore<UserPreferences> by dataStore(
        fileName = DATA_STORE_FILE_NAME,
        serializer = UserPreferencesSerializer
    )

    private val userPreferencesStore: DataStore<UserPreferences> = context.dataStore

    val userPreferencesFlow: Flow<UserPreferences> = userPreferencesStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Timber.e("Error reading user preferences.", exception)
                emit(UserPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }

    suspend fun updateOnBoardingShown(onBoardingShown: Boolean) {
        userPreferencesStore.updateData { preferences ->
            preferences.toBuilder().setOnBoardingShown(onBoardingShown).build()
        }
    }

    suspend fun updateAuthToken(authToken: String) {
        userPreferencesStore.updateData { preferences ->
            preferences.toBuilder().setAuthToken(authToken).build()
        }
    }
}