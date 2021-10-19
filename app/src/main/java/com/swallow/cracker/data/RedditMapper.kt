package com.swallow.cracker.data

import androidx.paging.PagingData
import androidx.paging.map
import com.swallow.cracker.data.model.RedditPost
import com.swallow.cracker.ui.model.RedditItem
import com.swallow.cracker.ui.model.RedditListItemImage
import com.swallow.cracker.ui.model.RedditListSimpleItem

object RedditMapper {
    fun mapApiToUi(item: RedditPost): RedditItem {
        return when {
            item.preview?.enabled == true -> RedditListItemImage(
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
                url = item.url,
                preview = item.preview
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

    fun replaceRedditPostToRedditItem(pagingData: PagingData<RedditPost>): PagingData<RedditItem> {
        return pagingData.map {
            mapApiToUi(it)
        }
    }
}