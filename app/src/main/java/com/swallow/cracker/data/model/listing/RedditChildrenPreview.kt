package com.swallow.cracker.data.model.listing

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class RedditChildrenPreview (
    @Json(name = "images")
    val images: List<RedditChildrenPreviewImage>,
    @Json(name = "enabled")
    val enabled: Boolean
) : Parcelable