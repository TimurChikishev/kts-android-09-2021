package com.swallow.cracker.data.modal

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RedditChildrenResponse(
    val data: RedditNewsDataResponse
)