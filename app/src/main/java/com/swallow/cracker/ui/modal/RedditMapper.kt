package com.swallow.cracker.ui.modal

import com.swallow.cracker.data.modal.RedditNewsDataResponse
import com.swallow.cracker.utils.isUrl

class RedditMapper {
    fun mapApiToUi(item: RedditNewsDataResponse): RedditList {
        return when {
            item.thumbnail.isUrl() -> RedditListItemWithImage(
                id = item.id,
                author = item.author,
                title = item.title,
                selftext = item.selftext,
                ups = item.ups,
                numComments = item.num_comments,
                created = item.created,
                thumbnail = item.thumbnail,
                url = item.url
            )
            else -> RedditListItem (
                id = item.id,
                author = item.author,
                title = item.title,
                selftext = item.selftext,
                ups = item.ups,
                numComments = item.num_comments,
                created = item.created,
                url = item.url
            )
        }
    }
}