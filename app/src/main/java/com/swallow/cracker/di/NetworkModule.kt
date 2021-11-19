package com.swallow.cracker.di

import com.swallow.cracker.data.api.RedditApi
import com.swallow.cracker.data.api.RedditApiAuth
import com.swallow.cracker.data.config.NetworkConfig
import com.swallow.cracker.data.network.AuthInterceptor
import com.swallow.cracker.data.network.TokenRefreshAuthenticator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import timber.log.Timber

val NetworkModule = module {
    single { provideOkHttpClient(get(), get()) }

    single { TokenRefreshAuthenticator(get(), get()) }

    single { AuthInterceptor(get()) }

    single { provideRedditApiAuth() }

    single { provideRedditApi(get()) }
}

private fun provideOkHttpClient(
    authenticator: TokenRefreshAuthenticator,
    authInterceptor: AuthInterceptor
): OkHttpClient {
    return OkHttpClient.Builder()
        .authenticator(authenticator)
        .addInterceptor(authInterceptor)
        .addNetworkInterceptor(
            HttpLoggingInterceptor {
                Timber.tag(NetworkConfig.LOG_TAG).d(it)
            }.setLevel(HttpLoggingInterceptor.Level.BODY)
        ).build()
}

private fun provideRedditApiAuth(): RedditApiAuth {
    val retrofitApiV1 = Retrofit.Builder()
        .baseUrl(NetworkConfig.URL_REDDIT_API_V1)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    return retrofitApiV1.create()
}

private fun provideRedditApi(okHttpClient: OkHttpClient): RedditApi {
    val retrofitOAuth = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(NetworkConfig.OAUTH_REDDIT_BASE_URI)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    return retrofitOAuth.create()
}