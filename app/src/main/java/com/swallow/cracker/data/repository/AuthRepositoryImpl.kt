package com.swallow.cracker.data.repository

import android.net.Uri
import com.swallow.cracker.data.config.AuthConfig
import com.swallow.cracker.domain.repository.AuthRepository
import net.openid.appauth.*

class AuthRepositoryImpl : AuthRepository {

    override fun getAuthRequest(): AuthorizationRequest {
        val serviceConfiguration = AuthorizationServiceConfiguration(
            Uri.parse(AuthConfig.AUTH_URI),
            Uri.parse(AuthConfig.TOKEN_URI)
        )

        val redirectUri = Uri.parse(AuthConfig.CALLBACK_URL)

        return AuthorizationRequest.Builder(
            serviceConfiguration,
            AuthConfig.CLIENT_ID,
            AuthConfig.RESPONSE_TYPE,
            redirectUri
        )
            .setAdditionalParameters(AuthConfig.DURATION_PERMANENT)
            .setScope(AuthConfig.SCOPE)
            .build()
    }

    override fun performTokenRequest(
        authService: AuthorizationService,
        tokenRequest: TokenRequest,
        onComplete: (String, String) -> Unit,
        onError: () -> Unit
    ) {
        authService.performTokenRequest(
            tokenRequest,
            ClientSecretBasic(AuthConfig.CLIENT_SECRET)
        ) { response, ex ->
            when {
                response != null -> {
                    AuthConfig.AUTH_TOKEN  = response.accessToken.orEmpty()
                    AuthConfig.REFRESH_TOKEN = response.refreshToken.orEmpty()
                    onComplete(AuthConfig.AUTH_TOKEN ?: "", AuthConfig.REFRESH_TOKEN ?: "")
                }
                else -> onError()
            }
        }
    }
}