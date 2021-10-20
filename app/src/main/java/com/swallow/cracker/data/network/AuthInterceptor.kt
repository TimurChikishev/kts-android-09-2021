package com.swallow.cracker.data.network

import com.swallow.cracker.data.config.NetworkConfig
import com.swallow.cracker.data.repository.Repository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    private val repository = Repository.userPreferencesRepository

    override fun intercept(chain: Interceptor.Chain): Response {
        var originalReq = chain.request()

        runBlocking {
            repository.userPreferencesFlow.take(1).collect {
                val authToken = it.authToken
                if (authToken.isNotEmpty()) {
                    originalReq = originalReq
                        .newBuilder()
                        .addHeader(NetworkConfig.HEADER_AUTHORIZATION, "Bearer $authToken")
                        .build()
                }
            }
        }

        return chain.proceed(originalReq)
    }
}