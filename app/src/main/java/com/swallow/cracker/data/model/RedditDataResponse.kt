package com.swallow.cracker.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class RedditDataResponse(
    @Json(name = "children")
    val children: List<RedditChildrenResponse>,
    @Json(name = "after")
    val after: String?,
    @Json(name = "before")
    val before: String?
)