package com.swallow.cracker.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.swallow.cracker.R
import com.swallow.cracker.data.datastore.dataStore
import com.swallow.cracker.ui.model.OnBoardingUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OnBoardingRepository(
    context: Context
) {
    private var dataStore: DataStore<Preferences> = context.dataStore

    suspend fun initFirstLaunch() {
        dataStore.edit { it[KEY] = FIRST_LAUNCH }
    }

    fun getFirstLaunch(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[KEY] ?: true
    }

    fun getOnBoardingData() = listOf(
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

    companion object {
        private val KEY = booleanPreferencesKey("FIRST_LAUNCH_KEY")
        private const val FIRST_LAUNCH = false
    }

}
