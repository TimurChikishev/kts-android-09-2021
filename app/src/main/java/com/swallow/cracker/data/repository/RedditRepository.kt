package com.swallow.cracker.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.withTransaction
import com.swallow.cracker.data.config.NetworkConfig
import com.swallow.cracker.data.database.Database.redditDatabase
import com.swallow.cracker.data.mapper.RedditMapper
import com.swallow.cracker.data.mapper.RedditMapper.mapRemoteSubredditAboutToUi
import com.swallow.cracker.data.mapper.RedditMapper.mapRemoteSubredditToUi
import com.swallow.cracker.data.model.Resources
import com.swallow.cracker.data.model.listing.RemoteRedditPost
import com.swallow.cracker.data.model.profile.RemoteRedditProfile
import com.swallow.cracker.data.network.NetworkHandler
import com.swallow.cracker.data.network.Networking
import com.swallow.cracker.ui.model.RedditItem
import com.swallow.cracker.ui.model.SearchQuery
import com.swallow.cracker.ui.model.Subreddit
import com.swallow.cracker.utils.getVoteDir
import com.swallow.cracker.utils.updateScore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class RedditRepository {

    @OptIn(ExperimentalPagingApi::class)
    fun getNewPager(query: String): Pager<Int, RemoteRedditPost> {
        return Pager(
            config = PagingConfig(
                pageSize = NetworkConfig.PAGE_SIZE,
                enablePlaceholders = true,
                maxSize = NetworkConfig.MAX_SIZE,
                initialLoadSize = NetworkConfig.INITIAL_LOAD_SIZE
            ),
            remoteMediator = RedditRemoteMediator(query, Networking.redditApiOAuth, redditDatabase)
        ) {
            redditDatabase.redditPostsDao().getPosts()
        }
    }

    suspend fun votePost(item: RedditItem, likes: Boolean): Flow<Response<Unit>> = flow {
        val dir = item.getVoteDir(likes)

        val response = Networking.redditApiOAuth.votePost(
            dir = dir,
            id = item.t3_id
        )

        if (response.isSuccessful)
            updatePostLikes(item, likes)

        emit(response)
    }

    private suspend fun updatePostLikes(item: RedditItem, likes: Boolean) {
        item.updateScore(likes)
        redditDatabase.withTransaction {
            redditDatabase.redditPostsDao().updatePostLikes(
                likes = item.likes,
                score = item.score,
                id = item.t3_id
            )
        }
    }

    suspend fun savePost(item: RedditItem): Flow<Response<Unit>> = flow {
        val response = Networking.redditApiOAuth.savedPost(id = item.t3_id)

        if (response.isSuccessful)
            updateSavedPost(true, item.t3_id)

        emit(response)
    }

    suspend fun unSavePost(item: RedditItem): Flow<Response<Unit>> = flow {
        val response = Networking.redditApiOAuth.unSavedPost(id = item.t3_id)

        if (response.isSuccessful)
            updateSavedPost(false, item.t3_id)

        emit(response)
    }

    suspend fun getProfileInfo(): Flow<RemoteRedditProfile> = flow {
        when (val profile = getProfile()) {
            is Resources.Success<RemoteRedditProfile> -> {
                saveRedditProfile(profile.value)
                emit(profile.value)
            }
            is Resources.Error -> {
                throw IllegalArgumentException(profile.throwable)
            }
        }
    }

    private suspend fun getProfile(): Resources<RemoteRedditProfile> {
        return NetworkHandler.call(
            api = { getProfileInfo() },
            mapper = { this } // this -> RemoteRedditProfile
        )
    }

    private suspend fun saveRedditProfile(redditProfile: RemoteRedditProfile) {
        redditDatabase.withTransaction {
            redditDatabase.redditProfileDao().saveProfile(redditProfile)
        }
    }

    private suspend fun updateSavedPost(saved: Boolean, id: String) {
        redditDatabase.withTransaction {
            redditDatabase.redditPostsDao().updatePostSaved(saved, id)
        }
    }

    suspend fun clearDataBase() {
        redditDatabase.withTransaction { redditDatabase.clearAllTables() }
    }

    suspend fun getProfileFromDB(id: String): Flow<RemoteRedditProfile?> = flow {
        val value = redditDatabase.redditProfileDao().getProfileById(id)
        emit(value)
    }

    suspend fun searchSubreddits(query: String): Flow<List<Subreddit>> = flow {
        when (val subreddits = getSubreddits(query)) {
            is Resources.Success<List<Subreddit>> -> {
                emit(subreddits.value)
            }
            is Resources.Error -> {
                throw IllegalArgumentException(subreddits.throwable)
            }
        }
    }

    private suspend fun getSubreddits(query: String): Resources<List<Subreddit>> {
        return NetworkHandler.call(
            api = { getSubreddits(query = query) },
            mapper = { this.mapRemoteSubredditToUi() }
        )
    }

    suspend fun getSearchQuery(): Flow<List<SearchQuery>?> = flow {
        emit(
            redditDatabase.redditSearchQueryDao().getSearchQuery()?.map {
                RedditMapper.mapRemoteSearchQueryToUi(it)
            }
        )
    }

    suspend fun savedSearchQuery(searchQuery: SearchQuery) {
        redditDatabase.withTransaction {
            val result = RedditMapper.mapUiSearchQueryToRemote(searchQuery)
            redditDatabase.redditSearchQueryDao().saveSearchQuery(result)
        }
    }

    suspend fun removeSearchQuery(query: String) {
        redditDatabase.withTransaction {
            redditDatabase.redditSearchQueryDao().removeSearchQueryByQuery(query)
        }
    }

    fun getSubredditInfo(subreddit: String): Flow<Subreddit> = flow {
        when (val result = getSubredditAbout(subreddit)) {
            is Resources.Success<Subreddit> -> {
                emit(result.value)
            }
            is Resources.Error -> {
                throw IllegalArgumentException(result.throwable)
            }
        }
    }

    private suspend fun getSubredditAbout(subreddit: String): Resources<Subreddit> {
        return NetworkHandler.call(
            api = { getSubredditInfo(subreddit = subreddit) },
            mapper = { this.mapRemoteSubredditAboutToUi()  }
        )
    }
}