package com.swallow.cracker.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class RedditDataResponse(
    val children: List<RedditChildrenResponse>,
    val after: String?,
    val before: String?
)