package com.swallow.cracker.data.api

import com.swallow.cracker.utils.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import timber.log.Timber

object Networking {

    private val okhttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(
            HttpLoggingInterceptor {
                Timber.tag("Network").d(it)
            }.setLevel(HttpLoggingInterceptor.Level.BODY)
        ).build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .client(okhttpClient)
        .build()

    val redditApi: RedditApi
        get() = retrofit.create()

    var after = ""
}
