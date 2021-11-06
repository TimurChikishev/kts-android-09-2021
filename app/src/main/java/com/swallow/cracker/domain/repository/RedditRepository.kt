package com.swallow.cracker.domain.repository

import androidx.paging.Pager
import com.swallow.cracker.data.model.listing.RemoteRedditPost
import com.swallow.cracker.data.model.profile.RemoteRedditProfile
import com.swallow.cracker.ui.model.RedditItem
import com.swallow.cracker.ui.model.SearchQuery
import com.swallow.cracker.ui.model.Subreddit
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RedditRepository {
    fun getNewListingPager(query: String): Pager<Int, RemoteRedditPost>

    fun getNewSearchPager(query: String): Pager<Int, RemoteRedditPost>

    fun getSubredditInfo(subreddit: String): Flow<Subreddit>

    suspend fun votePost(item: RedditItem, likes: Boolean): Flow<Response<Unit>>

    suspend fun updatePostLikes(item: RedditItem, likes: Boolean)

    suspend fun savePost(item: RedditItem): Flow<Response<Unit>>

    suspend fun unSavePost(item: RedditItem): Flow<Response<Unit>>

    suspend fun getProfileInfo(): Flow<RemoteRedditProfile>

    suspend fun clearDataBase()

    suspend fun getProfileFromDB(id: String): Flow<RemoteRedditProfile?>

    suspend fun searchSubreddits(query: String): Flow<List<Subreddit>>

    suspend fun getSearchQuery(): Flow<List<SearchQuery>?>

    suspend fun savedSearchQuery(searchQuery: SearchQuery)

    suspend fun removeSearchQuery(query: String)
}