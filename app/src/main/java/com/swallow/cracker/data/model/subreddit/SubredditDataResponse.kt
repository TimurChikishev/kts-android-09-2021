package com.swallow.cracker.data.model.subreddit

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SubredditDataResponse (
    @Json(name="children")
    val children: List<SubredditChildren>,

    @Json(name="after")
    val after: String?,

    @Json(name="before")
    val before: String?,

    @Json(name="dist")
    val dist: Int
)