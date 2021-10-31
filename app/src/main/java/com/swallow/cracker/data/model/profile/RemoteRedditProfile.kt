package com.swallow.cracker.data.model.profile

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.swallow.cracker.data.database.model.RedditProfileContract

@Entity(tableName = RedditProfileContract.TABLE_NAME)
@JsonClass(generateAdapter = true)
data class RemoteRedditProfile(

    @PrimaryKey
    @Json(name = "id")
    @ColumnInfo(name = RedditProfileContract.Columns.ID)
    val id: String,

    @Json(name = "name")
    @ColumnInfo(name = RedditProfileContract.Columns.NAME)
    val name: String,

    @Json(name = "icon_img")
    @ColumnInfo(name = RedditProfileContract.Columns.ICON_IMG)
    val iconImage: String?,

    @Json(name = "snoovatar_img")
    @ColumnInfo(name = RedditProfileContract.Columns.AVATAR_IMG)
    val avatarImg: String?,

    @Json(name = "created")
    @ColumnInfo(name = RedditProfileContract.Columns.CREATED)
    val created: Long,

    @Json(name = "total_karma")
    @ColumnInfo(name = RedditProfileContract.Columns.TOTAL_KARMA)
    val totalKarma: Int,

    @Json(name = "subreddit")
    @ColumnInfo(name = RedditProfileContract.Columns.SUBREDDIT)
    val subreddit: RemoteProfileInfoSubreddit
)