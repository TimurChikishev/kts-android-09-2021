package com.swallow.cracker.data.config

import net.openid.appauth.ResponseTypeValues

object AuthConfig {

    var token: String? = null
    const val AUTH_URI = "https://ssl.reddit.com/api/v1/authorize.compact"
    const val TOKEN_URI = "https://ssl.reddit.com/api/v1/access_token"
    const val RESPONSE_TYPE = ResponseTypeValues.CODE
    const val SCOPE = "read identity vote save"

    const val CLIENT_ID = "_CTYgC0LmYg3G5z517KYHw"
    const val CLIENT_SECRET = ""
    const val CALLBACK_URL = "com.swallow://oauth2redirect/reddit"
}