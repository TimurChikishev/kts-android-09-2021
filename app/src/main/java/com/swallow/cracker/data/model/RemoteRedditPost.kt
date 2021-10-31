package com.swallow.cracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(tableName = "redditPosts")
@JsonClass(generateAdapter = true)
data class RemoteRedditPost(
    @Json(name = "id")
    val id: String,
    @PrimaryKey
    @Json(name = "name")
    val t3_id: String,
    @Json(name = "author")
    val author: String,
    @Json(name = "subreddit")
    val subreddit: String,
    @Json(name = "subreddit_id")
    val subredditId: String,
    @Json(name = "title")
    val title: String,
    @Json(name = "selftext")
    var selftext: String,
    @Json(name = "score")
    val score: Int,
    @Json(name = "likes")
    val likes: Boolean?,
    @Json(name = "saved")
    val saved: Boolean,
    @Json(name = "num_comments")
    val num_comments: Int,
    @Json(name = "created")
    val created: Long,
    @Json(name = "thumbnail")
    val thumbnail: String,
    @Json(name = "url")
    val url: String,
    @Json(name = "preview")
    val preview: RedditChildrenPreview?,
    var communityIcon: String?
)