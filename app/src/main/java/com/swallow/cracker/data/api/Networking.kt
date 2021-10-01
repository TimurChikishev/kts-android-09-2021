package com.swallow.cracker.data.api

import com.swallow.cracker.data.config.AuthConfig
import com.swallow.cracker.data.config.NetworkConfig
import com.swallow.cracker.data.config.NetworkConfig.BASE_URL
import okhttp3.*
import okhttp3.OkHttpClient.Builder
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import timber.log.Timber

object Networking {

    private var okhttpClient: OkHttpClient =
        Builder().addInterceptor(getHeaderInterceptor()).addNetworkInterceptor(
            HttpLoggingInterceptor {
                Timber.tag(NetworkConfig.LOG_TAG).d(it)
            }.setLevel(HttpLoggingInterceptor.Level.BODY)
        ).build()

    private fun getHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()

            if (AuthConfig.token != null) {
                request.newBuilder()
                    .header(NetworkConfig.HEADER_NAME, "Bearer ${AuthConfig.token}")
                    .build()
            }

            chain.proceed(request)
        }
    }

    private val retrofit = Retrofit.Builder()
        .client(okhttpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val redditApi: RedditApi
        get() = retrofit.create()
}
