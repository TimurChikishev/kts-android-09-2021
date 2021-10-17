package com.swallow.cracker.data

import android.content.Context
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.swallow.cracker.data.config.AuthConfig
import com.swallow.cracker.data.datastore.dataStore
import kotlinx.coroutines.flow.map
import net.openid.appauth.*

class AuthRepository(
    context: Context
) {
    private var dataStore: DataStore<Preferences> = context.dataStore

    fun getAuthRequest(): AuthorizationRequest {
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
            .setScope(AuthConfig.SCOPE)
            .build()
    }

    fun performTokenRequest(
        authService: AuthorizationService,
        tokenRequest: TokenRequest,
        onComplete: (String) -> Unit,
        onError: () -> Unit
    ) {
        authService.performTokenRequest(
            tokenRequest,
            ClientSecretBasic(AuthConfig.CLIENT_SECRET)
        ) { response, ex ->
            when {
                response != null -> {
                    AuthConfig.token = response.accessToken.orEmpty()
                    onComplete(AuthConfig.token.orEmpty())
                }
                else -> onError()
            }
        }
    }

    suspend fun saveAuthToken(token: String) {
        dataStore.edit { it[KEY_TOKEN] = token }
    }

    fun getToken() = dataStore.data.map { it[KEY_TOKEN] }

    companion object {
        private val KEY_TOKEN = stringPreferencesKey("AUTH_TOKEN")
    }
}