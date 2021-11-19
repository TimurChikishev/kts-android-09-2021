package com.swallow.cracker.data.database.model

object RedditMineSubscriptionsContract {

    const val TABLE_NAME = "reddit_mine_subscriptions"

    object Columns {
        const val ID = "id"
        const val DISPLAY_NAME = "display_name"
        const val DISPLAY_NAME_PREFIXED = "display_name_prefixed"
        const val NAME = "name"
        const val URL = "url_subreddit"
        const val COMMUNITY_ICON = "community_icon"
        const val ICON_IMG = "icon_img"
        const val BANNER_IMG = "banner_img"
        const val SUBSCRIBERS = "subscribers"
        const val TITLE = "title"
        const val PUBLIC_DESCRIPTION = "public_description"
        const val DESCRIPTION = "description"
        const val CREATED = "created"
        const val USER_IS_SUBSCRIBER = "user_is_subscriber"
        const val ACTIVE_USER_COUNT = "active_user_count"
    }

}