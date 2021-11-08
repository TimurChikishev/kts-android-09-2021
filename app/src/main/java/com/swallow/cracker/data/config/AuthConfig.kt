package com.swallow.cracker.data.config

import android.util.Base64
import net.openid.appauth.ResponseTypeValues

object AuthConfig {
    var AUTH_TOKEN: String? = null
    var REFRESH_TOKEN: String? = null
    const val AUTH_URI = "https://ssl.reddit.com/api/v1/authorize.compact"
    const val TOKEN_URI = "https://ssl.reddit.com/api/v1/access_token"
    const val RESPONSE_TYPE = ResponseTypeValues.CODE
    const val SCOPE = "read identity vote save subscribe mysubreddits"

    const val CLIENT_ID = "_CTYgC0LmYg3G5z517KYHw"
    const val CLIENT_SECRET = ""
    const val CALLBACK_URL = "com.swallow://oauth2redirect/reddit"
    const val GRANT_TYPE_REFRESH_TOKEN = "refresh_token"
    val DURATION_PERMANENT = mapOf("duration" to "permanent")

    val BASIC_AUTH = "Basic " + Base64.encodeToString(
        "$CLIENT_ID:".toByteArray(),
        Base64.NO_WRAP
    )
}