package com.swallow.cracker

import android.app.Application
import com.swallow.cracker.data.database.Database
import com.swallow.cracker.data.repository.Repository
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        Repository.initUserPreferencesRepository(this)
        Database.initRedditDatabase(this)
    }
}