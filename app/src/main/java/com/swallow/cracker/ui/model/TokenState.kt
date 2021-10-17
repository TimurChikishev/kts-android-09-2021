package com.swallow.cracker.ui.model

sealed class TokenState {
    object Default : TokenState()
    object IsNoToken : TokenState()
    data class IsToken(val token: String) : TokenState()
}