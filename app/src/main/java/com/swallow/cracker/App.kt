package com.swallow.cracker

import android.app.Application
import androidx.viewbinding.BuildConfig
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}