package com.swallow.cracker.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RedditChildrenResponse(
    @Json(name = "data")
    val data: RedditNewsDataResponse
)