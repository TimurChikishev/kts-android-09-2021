package com.swallow.cracker.data.model.subreddit

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RemoteSubredditAbout(
    @Json(name = "community_icon")
    val communityIcon: String?,
    @Json(name = "header_img")
    val headerImg: String?
)
