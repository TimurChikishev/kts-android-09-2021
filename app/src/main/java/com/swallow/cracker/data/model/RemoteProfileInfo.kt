package com.swallow.cracker.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RemoteProfileInfo(
    @Json(name = "id")
    val id: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "icon_img")
    val iconImage: String?,
    @Json(name = "snoovatar_img")
    val avatarImg: String?,
    @Json(name = "created")
    val created: Long,
    @Json(name = "total_karma")
    val totalKarma: Int,
    @Json(name = "subreddit")
    val subreddit: RemoteProfileInfoSubreddit
)