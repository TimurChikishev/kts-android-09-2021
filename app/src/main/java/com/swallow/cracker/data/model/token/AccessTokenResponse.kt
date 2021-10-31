package com.swallow.cracker.data.model.token

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AccessTokenResponse(

    @Json(name = "access_token")
    val accessToken: String,

    @Json(name = "token_type")
    val tokenType: String,

    @Json(name = "expires_in")
    val expiresInUnix: Long,

    @Json(name = "scope")
    val scope: String,

    @Json(name = "refresh_token")
    val refreshToken: String
)