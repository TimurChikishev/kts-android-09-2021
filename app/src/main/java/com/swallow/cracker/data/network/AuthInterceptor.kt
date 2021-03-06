package com.swallow.cracker.data.network

import com.swallow.cracker.data.config.NetworkConfig
import com.swallow.cracker.domain.usecase.UserPreferencesUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response


class AuthInterceptor constructor(
    private val userPreferencesUseCase: UserPreferencesUseCase
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        runBlocking {
            userPreferencesUseCase.userPreferencesFlow.take(1).collect {
                val authToken = it.authToken
                if (authToken.isNotEmpty()) {
                    request = request
                        .newBuilder()
                        .addHeader(NetworkConfig.HEADER_AUTHORIZATION, "Bearer $authToken")
                        .build()
                }
            }
        }

        return chain.proceed(request)
    }
}

