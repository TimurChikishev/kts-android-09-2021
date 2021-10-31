package com.swallow.cracker.data.api

import com.swallow.cracker.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface RedditApi {

    @GET("{query}.json")
    suspend fun getListing(
        @Path("query") query: String,
        @Query("limit") limit: Int,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null
    ): Response<RedditJsonWrapper<RedditDataResponse>>

    @GET("search")
    suspend fun search(
        @Field("q") query: String,
        @Field("type") type: String,
    )

    @GET("api/search_subreddits")
    suspend fun searchSubreddit(
        @Field("query") query: String,
        @Field("exact") exact: Boolean = false,
        @Field("include_over_18") include_over_18: Boolean = false,
        @Field("include_unadvertisable") include_unadvertisable: Boolean = false,
        @Field("typeahead_active") typeahead_active: Boolean = false,
        @Field("search_query_id") search_query_id: Boolean?
    )

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