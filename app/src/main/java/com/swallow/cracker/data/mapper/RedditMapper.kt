package com.swallow.cracker.data.mapper

import androidx.paging.PagingData
import androidx.paging.map
import com.swallow.cracker.data.model.RedditJsonWrapper
import com.swallow.cracker.data.model.RemoteSearchQuery
import com.swallow.cracker.data.model.listing.RemoteRedditPost
import com.swallow.cracker.data.model.profile.RemoteRedditProfile
import com.swallow.cracker.data.model.subreddit.RemoteSubreddit
import com.swallow.cracker.data.model.subreddit.RemoteSubredditAbout
import com.swallow.cracker.data.model.subreddit.SubredditDataResponse
import com.swallow.cracker.ui.model.*
import com.swallow.cracker.utils.convertLongToTime
import com.swallow.cracker.utils.fixImgUrl

object RedditMapper {
    private fun mapRemoteRedditPostToUi(item: RemoteRedditPost): RedditItem {
        return when {
            item.preview?.enabled == true -> RedditListItemImage(
                id = item.id,
                prefixId = item.prefixId,
                author = item.author,
                subreddit = item.subreddit,
                subredditId = item.subredditId,
                title = item.title,
                selfText = item.selfText,
                score = item.score,
                likes = item.likes,
                saved = item.saved,
                numComments = item.numComments,
                created = item.created,
                thumbnail = item.thumbnail,
                url = item.url,
                preview = item.preview,
                communityIcon = item.communityIcon
            )
            else -> RedditListSimpleItem(
                id = item.id,
                prefixId = item.prefixId,
                author = item.author,
                subreddit = item.subreddit,
                subredditId = item.subredditId,
                title = item.title,
                selfText = item.selfText,
                score = item.score,
                likes = item.likes,
                saved = item.saved,
                numComments = item.numComments,
                created = item.created,
                url = item.url,
                communityIcon = item.communityIcon
            )
        }
    }

    fun mapPagingDataRemoteRedditPostToUi(pagingData: PagingData<RemoteRedditPost>): PagingData<RedditItem> {
        return pagingData.map {
            mapRemoteRedditPostToUi(it)
        }
    }

    fun mapRemoteProfileToUi(profile: RemoteRedditProfile): RedditProfile {
        val subreddit = profile.subreddit
        return RedditProfile(
            id = profile.id,
            name = profile.name,
            iconImage = profile.iconImage?.fixImgUrl(),
            avatarImg = profile.avatarImg?.fixImgUrl(),
            created = profile.created.convertLongToTime(),
            totalKarma = profile.totalKarma,
            awarderKarma = profile.awarderKarma,
            awardeeKarma = profile.awardeeKarma,
            commentKarma = profile.commentKarma,
            linkKarma = profile.linkKarma,
            bannerImg = subreddit.bannerImg?.fixImgUrl(),
            displayName = subreddit.displayName,
            url = subreddit.url
        )
    }

    fun mapRemoteSubredditToUi(subreddit: RemoteSubreddit): Subreddit {
        return Subreddit(
            id = subreddit.id,
            displayName = subreddit.displayName,
            displayNamePrefixed = subreddit.displayNamePrefixed,
            name = subreddit.name,
            url = subreddit.url,
            communityIcon = subreddit.communityIcon?.fixImgUrl() ?: "",
            iconImg = subreddit.iconImg?.fixImgUrl(),
            bannerImg = subreddit.bannerImg?.fixImgUrl(),
            subscribers = subreddit.subscribers,
            title = subreddit.title ?: "",
            publicDescription = subreddit.publicDescription ?: "",
            description = subreddit.description,
            created = subreddit.created,
            userIsSubscriber = subreddit.userIsSubscriber,
            activeUserCount = subreddit.activeUserCount
        )
    }

    fun RedditJsonWrapper<SubredditDataResponse>.mapRemoteSubredditToUi(): List<Subreddit> {
        val subreddits = this.data.children.map {
            val subreddit = it.data
            Subreddit(
                id = subreddit.id,
                displayName = subreddit.displayName,
                displayNamePrefixed = subreddit.displayNamePrefixed,
                name = subreddit.name,
                url = subreddit.url,
                communityIcon = subreddit.communityIcon?.fixImgUrl() ?: "",
                iconImg = subreddit.iconImg?.fixImgUrl(),
                bannerImg = subreddit.bannerImg?.fixImgUrl(),
                subscribers = subreddit.subscribers,
                title = subreddit.title ?: "",
                publicDescription = subreddit.publicDescription ?: "",
                description = subreddit.description,
                created = subreddit.created,
                userIsSubscriber = subreddit.userIsSubscriber,
                activeUserCount = subreddit.activeUserCount
            )
        }

        return subreddits
    }

    fun RedditJsonWrapper<RemoteSubredditAbout>.mapRemoteSubredditAboutToUi(): Subreddit {
        val subreddit = this.data
        return Subreddit(
            id = subreddit.id,
            displayName = subreddit.displayName,
            displayNamePrefixed = subreddit.displayNamePrefixed,
            name = subreddit.name ?: "",
            url = subreddit.url,
            communityIcon = subreddit.communityIcon?.fixImgUrl() ?: "",
            iconImg = subreddit.iconImg?.fixImgUrl(),
            bannerImg = subreddit.headerImg?.fixImgUrl(),
            subscribers = subreddit.subscribers,
            title = subreddit.title ?: "",
            publicDescription = subreddit.publicDescription ?: "",
            description = subreddit.description,
            created = subreddit.created,
            userIsSubscriber = subreddit.userIsSubscriber,
            activeUserCount = subreddit.activeUserCount
        )
    }

    fun mapRemoteSearchQueryToUi(querySearch: RemoteSearchQuery): SearchQuery {
        return SearchQuery(query = querySearch.query)
    }

    fun mapUiSearchQueryToRemote(searchQuery: SearchQuery): RemoteSearchQuery {
        return RemoteSearchQuery(query = searchQuery.query)
    }
}