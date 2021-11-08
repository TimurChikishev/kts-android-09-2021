package com.swallow.cracker.data.api

import com.swallow.cracker.data.model.token.AccessTokenResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface RedditApiAuth {

    @FormUrlEncoded
    @POST("access_token")
    suspend fun refreshAuthToken(
        @Header("Authorization") authorization: String,
        @Field("grant_type") grantType: String,
        @Field("refresh_token") refreshToken: String
    ): Response<AccessTokenResponse>

}