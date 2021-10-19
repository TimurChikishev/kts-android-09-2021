package com.swallow.cracker.data.api

import com.swallow.cracker.data.config.AuthConfig
import com.swallow.cracker.data.config.NetworkConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.OkHttpClient.Builder
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

object Networking {
    private var okhttpClientOauth: OkHttpClient =
        Builder().addInterceptor(Interceptor { chain ->
                val original = chain.request()
                val builder = original.newBuilder()

                if (AuthConfig.token != null) {
                    builder
                        .addHeader(NetworkConfig.HEADER_AUTHORIZATION, "bearer " + AuthConfig.token)
                        .addHeader(NetworkConfig.HEADER_USER_AGENT, NetworkConfig.HEADER_USER_AGENT_VALUE)
                }

                val request = builder.method(original.method, original.body)
                    .build()
                chain.proceed(request)
            }).build()

    private val retrofitOAuth = Retrofit.Builder()
        .client(okhttpClientOauth)
        .baseUrl(NetworkConfig.OAUTH_BASE_URI)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val redditApiOAuth: RedditApi = retrofitOAuth.create()
}

