package com.swallow.cracker.di

import com.swallow.cracker.data.network.AuthInterceptor
import com.swallow.cracker.data.network.TokenRefreshAuthenticator
import com.swallow.cracker.domain.repository.UserPreferencesRepository
import com.swallow.cracker.domain.usecase.UserPreferencesUseCase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import timber.log.Timber

val NetworkModule = module {
    single { provideOkHttpClient(get(), get()) }

    single { provideTokenRefreshAuthenticator(get()) }

    single { provideAuthInterceptor(get()) }
}

fun provideOkHttpClient(
    authenticator: TokenRefreshAuthenticator,
    authInterceptor: AuthInterceptor
): OkHttpClient {
    return OkHttpClient.Builder()
        .authenticator(authenticator)
        .addInterceptor(authInterceptor)
        .addNetworkInterceptor(
            HttpLoggingInterceptor {
                Timber.tag("Network").d(it)
            }.setLevel(HttpLoggingInterceptor.Level.BODY)
        ).build()
}

fun provideTokenRefreshAuthenticator(userPreferencesUseCase: UserPreferencesUseCase): TokenRefreshAuthenticator {
    return TokenRefreshAuthenticator(userPreferencesUseCase)
}

fun provideAuthInterceptor(userPreferencesUseCase: UserPreferencesUseCase): AuthInterceptor {
    return AuthInterceptor(userPreferencesUseCase)
}

fun providesUserPreferencesUseCase(repository: UserPreferencesRepository): UserPreferencesUseCase {
    return UserPreferencesUseCase(repository)
}