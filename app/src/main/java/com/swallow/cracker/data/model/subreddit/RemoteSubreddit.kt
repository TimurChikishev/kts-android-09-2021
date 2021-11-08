package com.swallow.cracker.data.model.subreddit

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.swallow.cracker.data.database.model.RedditMineSubscriptionsContract

@Entity(tableName = RedditMineSubscriptionsContract.TABLE_NAME)
@JsonClass(generateAdapter = true)
data class RemoteSubreddit(
    @Json(name = "id")
    @ColumnInfo(name = RedditMineSubscriptionsContract.Columns.ID)
    val id: String,

    @Json(name = "display_name")
    @ColumnInfo(name = RedditMineSubscriptionsContract.Columns.DISPLAY_NAME)
    val displayName: String,

    @Json(name = "display_name_prefixed")
    @ColumnInfo(name = RedditMineSubscriptionsContract.Columns.DISPLAY_NAME_PREFIXED)
    val displayNamePrefixed: String,

    @PrimaryKey
    @Json(name = "name")
    @ColumnInfo(name = RedditMineSubscriptionsContract.Columns.NAME)
    val name: String,

    @Json(name = "url")
    @ColumnInfo(name = RedditMineSubscriptionsContract.Columns.URL)
    val url: String?,

    @Json(name = "community_icon")
    @ColumnInfo(name = RedditMineSubscriptionsContract.Columns.COMMUNITY_ICON)
    val communityIcon: String?,

    @Json(name = "icon_img")
    @ColumnInfo(name = RedditMineSubscriptionsContract.Columns.ICON_IMG)
    val iconImg: String?,

    @Json(name = "banner_img")
    @ColumnInfo(name = RedditMineSubscriptionsContract.Columns.BANNER_IMG)
    val bannerImg: String?,

    @Json(name = "subscribers")
    @ColumnInfo(name = RedditMineSubscriptionsContract.Columns.SUBSCRIBERS)
    val subscribers: Int?,

    @Json(name = "title")
    @ColumnInfo(name = RedditMineSubscriptionsContract.Columns.TITLE)
    val title: String?,

    @Json(name = "public_description")
    @ColumnInfo(name = RedditMineSubscriptionsContract.Columns.PUBLIC_DESCRIPTION)
    val publicDescription: String?,

    @Json(name = "description")
    @ColumnInfo(name = RedditMineSubscriptionsContract.Columns.DESCRIPTION)
    val description: String?,

    @Json(name = "created")
    @ColumnInfo(name = RedditMineSubscriptionsContract.Columns.CREATED)
    val created: Long?,

    @Json(name = "user_is_subscriber")
    @ColumnInfo(name = RedditMineSubscriptionsContract.Columns.USER_IS_SUBSCRIBER)
    val userIsSubscriber: Boolean,

    @Json(name = "active_user_count")
    @ColumnInfo(name = RedditMineSubscriptionsContract.Columns.ACTIVE_USER_COUNT)
    val activeUserCount: Int?

)

