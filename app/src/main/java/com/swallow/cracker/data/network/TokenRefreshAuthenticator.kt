package com.swallow.cracker.data.network

import com.swallow.cracker.data.config.AuthConfig
import com.swallow.cracker.data.config.NetworkConfig
import com.swallow.cracker.data.repository.Repository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenRefreshAuthenticator : Authenticator {

    private val repository = Repository.userPreferencesRepository

    override fun authenticate(route: Route?, response: Response): Request {
        runBlocking {
            repository.userPreferencesFlow.take(1).collect {
                val refreshAccessToken = Networking.redditApiV1.refreshAuthToken(
                    authorization = AuthConfig.BASIC_AUTH,
                    grantType = AuthConfig.GRANT_TYPE_REFRESH_TOKEN,
                    refreshToken = it.authRefreshToken
                )

                if (refreshAccessToken.isSuccessful) {
                    AuthConfig.AUTH_TOKEN = refreshAccessToken.body()?.accessToken
                    AuthConfig.REFRESH_TOKEN = refreshAccessToken.body()?.refreshToken
                    saveAuthToken(AuthConfig.AUTH_TOKEN ?: "")
                    saveAuthRefreshToken(AuthConfig.REFRESH_TOKEN ?: "")
                }
            }
        }

        return response.request.newBuilder()
            .header(NetworkConfig.HEADER_AUTHORIZATION, AuthConfig.AUTH_TOKEN ?: "")
            .build()
    }

    private suspend fun saveAuthToken(token: String) {
        repository.updateAuthToken(token)
    }

    private suspend fun saveAuthRefreshToken(refreshToken: String) {
        repository.updateAuthRefreshToken(refreshToken)
    }
}