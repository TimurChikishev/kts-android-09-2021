package com.swallow.cracker.data.api

import com.swallow.cracker.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface RedditApi {

    @GET("{query}.json")
    suspend fun getSubreddit(
        @Path("query") query: String,
        @Query("limit") limit: Int,
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

    @GET("api/v1/me")
    suspend fun getProfileInfo(): Response<RemoteRedditProfile>

    @GET("r/{subreddit}/about.json")
    suspend fun getSubredditInfo(
        @Path("subreddit") subreddit: String
    ): Response<RedditJsonWrapper<RemoteSubredditAbout>>
}