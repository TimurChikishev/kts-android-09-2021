package com.swallow.cracker.domain.usecase

import com.swallow.cracker.domain.repository.AuthRepository
import net.openid.appauth.AuthorizationService
import net.openid.appauth.TokenRequest

class AuthUseCase constructor(private val authRepository: AuthRepository) {

    fun getAuthRequest() = authRepository.getAuthRequest()

    fun performTokenRequest(
        authService: AuthorizationService,
        tokenRequest: TokenRequest,
        onComplete: (String, String) -> Unit,
        onError: () -> Unit
    ) {
        authRepository.performTokenRequest(authService, tokenRequest, onComplete, onError)
    }
}