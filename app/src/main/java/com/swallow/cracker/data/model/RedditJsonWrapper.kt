package com.swallow.cracker.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RedditJsonWrapper<T>(
    val data: T
)