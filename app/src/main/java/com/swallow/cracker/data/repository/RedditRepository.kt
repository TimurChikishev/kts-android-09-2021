package com.swallow.cracker.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.swallow.cracker.data.RedditRemoteMediator
import com.swallow.cracker.data.config.NetworkConfig
import com.swallow.cracker.data.database.Database
import com.swallow.cracker.data.model.RedditPost
import com.swallow.cracker.data.network.Networking
import com.swallow.cracker.ui.model.QuerySubreddit
import com.swallow.cracker.ui.model.RedditItem
import com.swallow.cracker.utils.getVoteDir
import com.swallow.cracker.utils.updateScore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class RedditRepository {

    private val redditDatabase = Database.redditDatabase

    @OptIn(ExperimentalPagingApi::class)
    fun getPostPager(query: QuerySubreddit): Flow<PagingData<RedditPost>> {
        return Pager(
            config = PagingConfig(
                pageSize = NetworkConfig.PAGE_SIZE,
                enablePlaceholders = false,
                maxSize = NetworkConfig.MAX_SIZE,
                initialLoadSize = NetworkConfig.INITIAL_LOAD_SIZE,
                prefetchDistance = NetworkConfig.PAGE_SIZE / 2
            ),
            remoteMediator = RedditRemoteMediator(query, Networking.redditApiOAuth, redditDatabase),
            pagingSourceFactory = { redditDatabase.redditPostsDao().getPosts() }
        ).flow
    }

    suspend fun votePost(item: RedditItem, likes: Boolean): Flow<Response<Unit>> = flow {
        val dir = item.getVoteDir(likes)

        val response = Networking.redditApiOAuth.votePost(
            dir = dir,
            id = item.id()
        )

        if (response.isSuccessful)
            updatePostLikes(item, likes)

        emit(response)
    }

    private fun updatePostLikes(item: RedditItem, likes: Boolean) {
        item.updateScore(likes)
        redditDatabase.redditPostsDao().updatePostLikes(
            likes = item.likes(),
            score = item.score(),
            id = item.id()
        )
    }


    suspend fun savePost(item: RedditItem): Flow<Response<Unit>> = flow {
        val response = Networking.redditApiOAuth.savedPost(id = item.id())

        if (response.isSuccessful)
            updateSavedPost(true, item.id())

        emit(response)
    }

    suspend fun unSavePost(item: RedditItem): Flow<Response<Unit>> = flow {
        val response = Networking.redditApiOAuth.unSavedPost(id = item.id())

        if (response.isSuccessful)
            updateSavedPost(false, item.id())

        emit(response)
    }

    private fun updateSavedPost(saved: Boolean, id: String) {
        redditDatabase.redditPostsDao().updatePostSaved(saved, id)
    }
}