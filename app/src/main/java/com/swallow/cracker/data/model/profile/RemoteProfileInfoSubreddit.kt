package com.swallow.cracker.data.model.profile

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RemoteProfileInfoSubreddit(
    @Json(name = "banner_img")
    val bannerImg: String?,
    @Json(name = "display_name")
    val displayName: String,
    @Json(name = "url")
    val url: String
)