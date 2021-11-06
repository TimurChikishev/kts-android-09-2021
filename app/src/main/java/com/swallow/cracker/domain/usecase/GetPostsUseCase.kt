package com.swallow.cracker.domain.usecase

import com.swallow.cracker.domain.repository.RedditRepository
import com.swallow.cracker.ui.model.RedditItem
import com.swallow.cracker.ui.model.SearchQuery
import com.swallow.cracker.ui.model.Subreddit

class GetPostsUseCase constructor(private val redditRepository: RedditRepository) {

    fun getNewListingPager(query: String) = redditRepository.getNewListingPager(query)

    fun getNewSearchPager(query: String) = redditRepository.getNewSearchPager(query)

    suspend fun savePost(item: RedditItem) = redditRepository.savePost(item)

    suspend fun unSavePost(item: RedditItem) = redditRepository.unSavePost(item)

    suspend fun votePost(item: RedditItem, likes: Boolean) = redditRepository.votePost(item, likes)

    suspend fun getProfileFromDB(id: String) = redditRepository.getProfileFromDB(id)

    suspend fun clearDataBase() = redditRepository.clearDataBase()

    suspend fun getProfileInfo() = redditRepository.getProfileInfo()

    suspend fun searchSubreddits(query: String) = redditRepository.searchSubreddits(query)

    suspend fun getSearchQuery() = redditRepository.getSearchQuery()

    suspend fun savedSearchQuery(searchQuery: SearchQuery) =
        redditRepository.savedSearchQuery(searchQuery)

    suspend fun removeSearchQuery(query: String) = redditRepository.removeSearchQuery(query)

    fun getSubredditInfo(subredditName: String) = redditRepository.getSubredditInfo(subredditName)

    fun subscribeSubreddit(action: String, subreddit: Subreddit) = redditRepository.subscribeSubreddit(action, subreddit)
}