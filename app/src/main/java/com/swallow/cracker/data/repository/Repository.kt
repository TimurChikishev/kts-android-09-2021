package com.swallow.cracker.data.repository

import android.content.Context

object Repository {
    lateinit var userPreferencesRepository: UserPreferencesRepository
        private set

    fun initUserPreferencesRepository(context: Context) {
        userPreferencesRepository = UserPreferencesRepository(context)
    }
}