package com.swallow.cracker.data.model.subreddit

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SubredditChildren (
    @Json(name = "data")
    val data: RemoteSubreddit,
)