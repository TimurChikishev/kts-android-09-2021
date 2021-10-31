package com.swallow.cracker.data.mapper

import androidx.paging.PagingData
import androidx.paging.map
import com.swallow.cracker.data.model.RemoteRedditPost
import com.swallow.cracker.data.model.RemoteRedditProfile
import com.swallow.cracker.ui.model.RedditItem
import com.swallow.cracker.ui.model.RedditListItemImage
import com.swallow.cracker.ui.model.RedditListSimpleItem
import com.swallow.cracker.ui.model.RedditProfile
import com.swallow.cracker.utils.convertLongToTime
import com.swallow.cracker.utils.fixImgUrl

object RedditMapper {
    fun mapApiToUi(item: RemoteRedditPost): RedditItem {
        return when {
            item.preview?.enabled == true -> RedditListItemImage(
                id = item.id,
                t3_id = item.t3_id,
                author = item.author,
                subreddit = item.subreddit,
                subredditId = item.subredditId,
                title = item.title,
                selftext = item.selftext,
                score = item.score,
                likes = item.likes,
                saved = item.saved,
                numComments = item.num_comments,
                created = item.created,
                thumbnail = item.thumbnail,
                url = item.url,
                preview = item.preview,
                communityIcon = item.communityIcon
            )
            else -> RedditListSimpleItem(
                id = item.id,
                t3_id = item.t3_id,
                author = item.author,
                subreddit = item.subreddit,
                subredditId = item.subredditId,
                title = item.title,
                selftext = item.selftext,
                score = item.score,
                likes = item.likes,
                saved = item.saved,
                numComments = item.num_comments,
                created = item.created,
                url = item.url,
                communityIcon = item.communityIcon
            )
        }
    }

    fun replaceRedditPostToRedditItem(pagingData: PagingData<RemoteRedditPost>): PagingData<RedditItem> {
        return pagingData.map {
            mapApiToUi(it)
        }
    }

    fun remoteProfileToUi(profile: RemoteRedditProfile): RedditProfile {
        val subreddit = profile.subreddit
        return RedditProfile(
            id = profile.id,
            name = profile.name,
            iconImage = profile.iconImage?.fixImgUrl(),
            avatarImg = profile.avatarImg?.fixImgUrl(),
            created = profile.created.convertLongToTime(),
            totalKarma = profile.totalKarma,
            bannerImg = subreddit.bannerImg?.fixImgUrl(),
            displayName = subreddit.displayName,
            url = subreddit.url,
        )
    }
}