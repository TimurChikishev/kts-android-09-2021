package com.swallow.cracker.data.database.model

object RedditProfileContract {

    const val TABLE_NAME = "reddit_profile"

    object Columns {
        const val ID = "id"
        const val NAME = "name"
        const val ICON_IMG = "icon_img"
        const val AVATAR_IMG = "avatar_img"
        const val CREATED = "created"
        const val TOTAL_KARMA = "total_karma"
        const val AWARDER_KARMA = "awarder_karma"
        const val AWARDEE_KARMA = "awardee_karma"
        const val COMMENT_KARMA = "comment_karma"
        const val LINK_KARMA = "link_karma"
        const val SUBREDDIT = "subreddit"
    }
}