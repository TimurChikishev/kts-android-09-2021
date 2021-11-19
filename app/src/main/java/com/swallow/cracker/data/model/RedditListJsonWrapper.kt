package com.swallow.cracker.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RedditListJsonWrapper<T>(
    val subreddits: List<T>
)