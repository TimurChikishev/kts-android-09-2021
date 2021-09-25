package com.swallow.cracker.data.api

import com.swallow.cracker.data.modal.RedditDataResponse
import com.swallow.cracker.data.modal.RedditJsonWrapper
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RedditApi {
    @GET("top.json")
    suspend fun getTop(
        @Query("after") after: String,
        @Query("limit") limit: String
    ): Response<RedditJsonWrapper<RedditDataResponse>>
}