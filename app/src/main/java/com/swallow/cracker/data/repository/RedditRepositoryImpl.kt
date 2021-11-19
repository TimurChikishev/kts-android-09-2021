package com.swallow.cracker.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.withTransaction
import com.swallow.cracker.data.api.RedditApi
import com.swallow.cracker.data.config.NetworkConfig
import com.swallow.cracker.data.database.RedditDatabase
import com.swallow.cracker.data.mapper.RedditMapper
import com.swallow.cracker.data.mapper.RedditMapper.mapRemoteSubredditAboutToUi
import com.swallow.cracker.data.mapper.RedditMapper.mapRemoteSubredditToUi
import com.swallow.cracker.data.model.Resources
import com.swallow.cracker.data.model.listing.RemoteRedditPost
import com.swallow.cracker.data.model.profile.RemoteRedditProfile
import com.swallow.cracker.data.model.subreddit.RemoteSubreddit
import com.swallow.cracker.data.network.NetworkHandler
import com.swallow.cracker.domain.repository.RedditRepository
import com.swallow.cracker.ui.model.RedditItem
import com.swallow.cracker.ui.model.SearchQuery
import com.swallow.cracker.ui.model.Subreddit
import com.swallow.cracker.utils.getVoteDir
import com.swallow.cracker.utils.updateScore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class RedditRepositoryImpl constructor(
    private val redditDatabase: RedditDatabase,
    private val redditApi: RedditApi
) : RedditRepository {

    private val defaultPagerConfig = PagingConfig(
        pageSize = NetworkConfig.PAGE_SIZE,
        enablePlaceholders = true,
        maxSize = NetworkConfig.MAX_SIZE,
        initialLoadSize = NetworkConfig.INITIAL_LOAD_SIZE
    )

    @OptIn(ExperimentalPagingApi::class)
    override fun getNewListingPager(query: String): Pager<Int, RemoteRedditPost> {
        return Pager(
            config = defaultPagerConfig,
            remoteMediator = RedditListingRemoteMediator(query, redditApi, redditDatabase)
        ) {
            redditDatabase.redditPostsDao().getPosts()
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getNewSearchPager(query: String): Pager<Int, RemoteRedditPost> {
        return Pager(
            config = defaultPagerConfig,
            remoteMediator = RedditSearchRemoteMediator(query, redditApi, redditDatabase)
        ) {
            redditDatabase.redditPostsDao().getPosts()
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getNewMineSubscriptionsPager(): Flow<PagingData<RemoteSubreddit>> {
        return Pager(
            config = PagingConfig(
                pageSize = 100,
                enablePlaceholders = false
            ),
            remoteMediator = RedditMineSubscriptionsRemoteMediator(redditDatabase, redditApi)
        ) {
            redditDatabase.redditMineSubscriptionsDao().getMineSubscriptions()
        }.flow
    }

    override suspend fun votePost(item: RedditItem, likes: Boolean): Flow<Response<Unit>> = flow {
        val dir = item.getVoteDir(likes)

        val response = redditApi.votePost(
            dir = dir,
            id = item.prefixId
        )

        if (response.isSuccessful)
            updatePostLikes(item, likes)

        emit(response)
    }

    override suspend fun updatePostLikes(item: RedditItem, likes: Boolean) {
        item.updateScore(likes)
        redditDatabase.withTransaction {
            redditDatabase.redditPostsDao().updatePostLikes(
                likes = item.likes,
                score = item.score,
                id = item.prefixId
            )
        }
    }

    override fun subscribeSubreddit(action: String, subreddit: Subreddit): Flow<Response<Unit>> =
        flow {
            val response = redditApi.subscribeSubreddit(
                action = action,
                subredditId = subreddit.name
            )

            if (!response.isSuccessful) throw Exception("Subscription response failed")

            emit(response)
        }

    override suspend fun savePost(item: RedditItem): Flow<Response<Unit>> = flow {
        val response = redditApi.savedPost(id = item.prefixId)

        if (response.isSuccessful)
            updateSavedPost(true, item.prefixId)

        emit(response)
    }

    override suspend fun unSavePost(item: RedditItem): Flow<Response<Unit>> = flow {
        val response = redditApi.unSavedPost(id = item.prefixId)

        if (response.isSuccessful)
            updateSavedPost(false, item.prefixId)

        emit(response)
    }

    override suspend fun getProfileInfo(): Flow<RemoteRedditProfile> = flow {
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
            api =  redditApi,
            apiMethod = { getProfileInfo() },
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

    override suspend fun clearDataBase() {
        redditDatabase.withTransaction { redditDatabase.clearAllTables() }
    }

    override suspend fun getProfileFromDB(id: String): Flow<RemoteRedditProfile?> = flow {
        val value = redditDatabase.redditProfileDao().getProfileById(id)
        emit(value)
    }

    override suspend fun searchSubreddits(query: String): Flow<List<Subreddit>> = flow {
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
            api = redditApi,
            apiMethod = { getSubreddits(query = query) },
            mapper = { this.mapRemoteSubredditToUi() }
        )
    }

    override suspend fun getSearchQuery(): Flow<List<SearchQuery>?> = flow {
        emit(
            redditDatabase.redditSearchQueryDao().getSearchQuery()?.map {
                RedditMapper.mapRemoteSearchQueryToUi(it)
            }
        )
    }

    override suspend fun savedSearchQuery(searchQuery: SearchQuery) {
        redditDatabase.withTransaction {
            val result = RedditMapper.mapUiSearchQueryToRemote(searchQuery)
            redditDatabase.redditSearchQueryDao().saveSearchQuery(result)
        }
    }

    override suspend fun removeSearchQuery(query: String) {
        redditDatabase.withTransaction {
            redditDatabase.redditSearchQueryDao().removeSearchQueryByQuery(query)
        }
    }

    override fun getSubredditInfo(subreddit: String): Flow<Subreddit> = flow {
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
            api = redditApi,
            apiMethod = { getSubredditInfo(subreddit = subreddit) },
            mapper = { this.mapRemoteSubredditAboutToUi() }
        )
    }
}