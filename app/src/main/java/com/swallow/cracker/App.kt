package com.swallow.cracker

import android.app.Application
import com.swallow.cracker.di.AppModule
import com.swallow.cracker.di.NetworkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidContext(this@App)
            modules(AppModule, NetworkModule)
        }
    }
}