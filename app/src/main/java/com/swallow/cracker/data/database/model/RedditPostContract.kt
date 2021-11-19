package com.swallow.cracker.data.database.model

object RedditPostContract {
    const val TABLE_NAME = "reddit_post"

    object Columns {
        const val ID = "id"
        const val PREFIX_ID = "prefix_id"
        const val AUTHOR = "author"
        const val SUBREDDIT = "subreddit"
        const val SUBREDDIT_ID = "subreddit_id"
        const val TITLE = "title"
        const val SELF_TEXT = "self_text"
        const val SCORE = "score"
        const val LIKES = "likes"
        const val SAVED = "saved"
        const val NUM_COMMENTS = "num_comments"
        const val CREATED = "created"
        const val THUMBNAIL = "thumbnail"
        const val URL = "url"
        const val PREVIEW = "preview"
        const val COMMUNITY_ICON = "community_icon"
        const val QUERY = "query_str"
    }
}