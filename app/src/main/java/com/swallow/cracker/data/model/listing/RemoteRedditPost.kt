package com.swallow.cracker.data.model.listing

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.swallow.cracker.data.database.model.RedditPostContract

@Entity(tableName = RedditPostContract.TABLE_NAME)
@JsonClass(generateAdapter = true)
data class RemoteRedditPost(

    @Json(name = "id")
    @ColumnInfo(name = RedditPostContract.Columns.ID)
    val id: String,

    @PrimaryKey
    @Json(name = "name")
    @ColumnInfo(name = RedditPostContract.Columns.T3_ID)
    val t3_id: String,

    @Json(name = "author")
    @ColumnInfo(name = RedditPostContract.Columns.AUTHOR)
    val author: String,

    @Json(name = "subreddit")
    @ColumnInfo(name = RedditPostContract.Columns.SUBREDDIT)
    val subreddit: String,

    @Json(name = "subreddit_id")
    @ColumnInfo(name = RedditPostContract.Columns.SUBREDDIT_ID)
    val subredditId: String,

    @Json(name = "title")
    @ColumnInfo(name = RedditPostContract.Columns.TITLE)
    val title: String,

    @Json(name = "selftext")
    @ColumnInfo(name = RedditPostContract.Columns.SELF_TEXT)
    var selfText: String,

    @Json(name = "score")
    @ColumnInfo(name = RedditPostContract.Columns.SCORE)
    val score: Int,

    @Json(name = "likes")
    @ColumnInfo(name = RedditPostContract.Columns.LIKES)
    val likes: Boolean?,

    @Json(name = "saved")
    @ColumnInfo(name = RedditPostContract.Columns.SAVED)
    val saved: Boolean,

    @Json(name = "num_comments")
    @ColumnInfo(name = RedditPostContract.Columns.NUM_COMMENTS)
    val numComments: Int,

    @Json(name = "created")
    @ColumnInfo(name = RedditPostContract.Columns.CREATED)
    val created: Long,

    @Json(name = "thumbnail")
    @ColumnInfo(name = RedditPostContract.Columns.THUMBNAIL)
    val thumbnail: String,

    @Json(name = "url")
    @ColumnInfo(name = RedditPostContract.Columns.URL)
    val url: String,

    @Json(name = "preview")
    @ColumnInfo(name = RedditPostContract.Columns.PREVIEW)
    val preview: RedditChildrenPreview?,

    @ColumnInfo(name = RedditPostContract.Columns.COMMUNITY_ICON)
    var communityIcon: String?,

    @ColumnInfo(name = RedditPostContract.Columns.QUERY)
    var query: String?,
)