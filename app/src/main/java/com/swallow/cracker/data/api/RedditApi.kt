package com.swallow.cracker.data.api

import com.swallow.cracker.data.modal.RedditDataResponse
import com.swallow.cracker.data.modal.RedditJsonWrapper
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RedditApi {
    @GET("top.json")
    suspend fun getTop(
        @Query("limit") limit: String,
        @Query("count") count: String,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null
    ): Response<RedditJsonWrapper<RedditDataResponse>>
}