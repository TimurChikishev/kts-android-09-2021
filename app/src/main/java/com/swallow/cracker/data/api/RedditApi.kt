package com.swallow.cracker.data.api

import com.swallow.cracker.data.model.RedditJsonWrapper
import com.swallow.cracker.data.model.listing.RedditDataResponse
import com.swallow.cracker.data.model.profile.RemoteRedditProfile
import com.swallow.cracker.data.model.subreddit.RemoteSubredditAbout
import com.swallow.cracker.data.model.subreddit.SubredditDataResponse
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

    @GET("search.json")
    suspend fun search(
        @Query("q") query: String,
        @Query("limit") limit: Int,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null
    ): Response<RedditJsonWrapper<RedditDataResponse>>

    @GET("api/subreddit_autocomplete_v2.json")
    suspend fun getSubreddits(
        @Query("query") query: String,
        @Query("limit") limit: Int = 5,
        @Query("raw_json") rawJson: Int = 1,
        @Query("gilding_detail") gildingDetail: Int = 1
    ): Response<RedditJsonWrapper<SubredditDataResponse>>

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

    @GET("api/v1/me")
    suspend fun getProfileInfo(): Response<RemoteRedditProfile>

    @GET("r/{subreddit}/about.json")
    suspend fun getSubredditInfo(
        @Path("subreddit") subreddit: String
    ): Response<RedditJsonWrapper<RemoteSubredditAbout>>

    @FormUrlEncoded
    @POST("/api/subscribe")
    suspend fun subscribeSubreddit(
        @Field("action") action: String,
        @Field("skip_initial_defaults") skipInitialDefaults: Boolean = false,
        @Field("sr") subredditId: String, // prefixId = @Json(name = "name")
    ): Response<Unit>

    @GET("subreddits/mine/{where}")
    suspend fun mineSubscriptions(
        @Path("where") where: String = "subscriber",
        @Query("limit") limit: Int,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null,
        @Query("count") count: String? = null,
        @Query("show") show: String? = "all"
    ):Response<RedditJsonWrapper<SubredditDataResponse>>
}