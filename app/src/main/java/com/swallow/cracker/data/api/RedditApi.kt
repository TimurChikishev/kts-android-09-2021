package com.swallow.cracker.data.api

import com.swallow.cracker.data.model.AccessTokenResponse
import com.swallow.cracker.data.model.RedditDataResponse
import com.swallow.cracker.data.model.RedditJsonWrapper
import retrofit2.Response
import retrofit2.http.*

interface RedditApi {

    @GET("r/{subreddit}/{category}.json")
    suspend fun getSubreddit(
        @Path("subreddit") subreddit: String,
        @Path("category") category: String,
        @Query("limit") limit: String,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null
    ): Response<RedditJsonWrapper<RedditDataResponse>>

    @FormUrlEncoded
    @POST("api/save")
    suspend fun savedPost(
        @Field("category") category: String? = null,
        @Field("id") id: String
    ): Response<Unit>

    @FormUrlEncoded
    @POST("api/unsave")
    suspend fun unSavedPost(
        @Field("id") id: String
    ): Response<Unit>

    @FormUrlEncoded
    @POST("api/vote")
    suspend fun votePost(
        @Field("dir") dir: Int,
        @Field("id") id: String
    ): Response<Unit>

    @FormUrlEncoded
    @POST("access_token")
    suspend fun refreshAuthToken(
        @Header("Authorization") authorization: String,
        @Field("grant_type") grantType: String,
        @Field("refresh_token") refreshToken: String
    ): Response<AccessTokenResponse>
}