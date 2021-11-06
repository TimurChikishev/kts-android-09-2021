package com.swallow.cracker.data.model.subreddit

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RemoteSubredditAbout(
    @Json(name = "id")
    val id: String,
    @Json(name = "name")
    val name: String?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "display_name")
    val displayName: String,
    @Json(name = "display_name_prefixed")
    val displayNamePrefixed: String,
    @Json(name = "community_icon")
    val communityIcon: String?,
    @Json(name = "icon_img")
    val iconImg: String?,
    @Json(name = "header_img")
    val headerImg: String?,
    @Json(name = "public_description")
    val publicDescription: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "subscribers")
    val subscribers: Int?,
    @Json(name = "active_user_count")
    val activeUserCount: Int?,
    @Json(name = "user_is_subscriber")
    val userIsSubscriber: Boolean,
    @Json(name = "created")
    val created: Long,
    @Json(name = "url")
    val url: String
)
