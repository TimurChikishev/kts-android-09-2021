package com.swallow.cracker.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.swallow.cracker.utils.fixImgUrl
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class RedditChildrenPreviewImageSource(
    @Json(name = "url")
    val url: String,
    @Json(name = "width")
    val width: Int,
    @Json(name = "height")
    val height: Int,
    val urlNew: String = url.fixImgUrl()
) : Parcelable
