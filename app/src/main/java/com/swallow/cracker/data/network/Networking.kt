package com.swallow.cracker.data.network

import com.swallow.cracker.data.api.RedditApi
import com.swallow.cracker.data.config.NetworkConfig
import okhttp3.OkHttpClient
import okhttp3.OkHttpClient.Builder
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

object Networking {
    private var okhttpClientOauth: OkHttpClient = Builder()
        .authenticator(TokenRefreshAuthenticator())
        .addInterceptor(AuthInterceptor())
        .build()

    private val retrofitOAuth = Retrofit.Builder()
        .client(okhttpClientOauth)
        .baseUrl(NetworkConfig.OAUTH_BASE_URI)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val redditApiOAuth: RedditApi = retrofitOAuth.create()

    private var okhttp = Builder().build()

    private val retrofitApiV1 = Retrofit.Builder()
        .client(okhttp)
        .baseUrl(NetworkConfig.URL_API_V1)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val redditApiV1: RedditApi = retrofitApiV1.create()
}

