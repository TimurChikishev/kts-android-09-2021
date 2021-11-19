package com.swallow.cracker.domain.repository

import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.TokenRequest

interface AuthRepository {
    fun getAuthRequest(): AuthorizationRequest

    fun performTokenRequest(
        authService: AuthorizationService,
        tokenRequest: TokenRequest,
        onComplete: (String, String) -> Unit,
        onError: () -> Unit
    )
}