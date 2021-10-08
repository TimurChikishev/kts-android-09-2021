package com.swallow.cracker.data.model

import com.swallow.cracker.ui.model.RedditList
import com.swallow.cracker.ui.model.RedditListItemWithImage
import com.swallow.cracker.ui.model.RedditListSimpleItem
import com.swallow.cracker.utils.isUrl

object RedditMapper {
    fun mapApiToUi(item: RedditNewsDataResponse): RedditList {
        return when {
            item.thumbnail.isUrl() -> RedditListItemWithImage(
                id = item.id,
                t3_id = item.t3_id,
                author = item.author,
                subreddit = item.subreddit,
                title = item.title,
                selftext = item.selftext,
                score = item.score,
                likes = item.likes,
                saved = item.saved,
                numComments = item.num_comments,
                created = item.created,
                thumbnail = item.thumbnail,
                url = item.url
            )
            else -> RedditListSimpleItem(
                id = item.id,
                t3_id = item.t3_id,
                author = item.author,
                subreddit = item.subreddit,
                title = item.title,
                selftext = item.selftext,
                score = item.score,
                likes = item.likes,
                saved = item.saved,
                numComments = item.num_comments,
                created = item.created,
                url = item.url
            )
        }
    }
}