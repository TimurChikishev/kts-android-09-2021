package com.swallow.cracker.data.database.model

object RedditProfileContract {

    const val TABLE_NAME = "redditProfile"

    object Columns {
        const val ID = "id"
        const val NAME = "name"
        const val ICON_IMG = "icon_img"
        const val AVATAR_IMG = "avatar_img"
        const val CREATED = "created"
        const val TOTAL_KARMA = "total_karma"
        const val SUBREDDIT = "subreddit"
    }
}