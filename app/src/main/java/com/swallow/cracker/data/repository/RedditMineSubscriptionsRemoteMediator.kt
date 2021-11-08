package com.swallow.cracker.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.swallow.cracker.data.database.RedditDatabase
import com.swallow.cracker.data.model.RedditJsonWrapper
import com.swallow.cracker.data.model.RemoteRedditKeys
import com.swallow.cracker.data.model.subreddit.RemoteSubreddit
import com.swallow.cracker.data.model.subreddit.SubredditDataResponse
import com.swallow.cracker.data.network.Networking
import retrofit2.Response

@OptIn(ExperimentalPagingApi::class)
class RedditMineSubscriptionsRemoteMediator(
    private val database: RedditDatabase
) : RemoteMediator<Int, RemoteSubreddit>() {

    override suspend fun initialize(): InitializeAction {
        // Require that remote REFRESH is launched on initial load and succeeds before launching
        // remote PREPEND / APPEND.
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RemoteSubreddit>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            }

            val response = getPosts(loadKey)
            val listing = response.body()?.data
            val subreddits = listing?.children?.map { it.data }

            if (subreddits != null) {
                database.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        database.redditMineSubscriptionsDao().clearMineSubscriptions()
                        // clear table with mine subscriptions
                    }

                    // save key
//                    database.redditKeysDao()
//                        .saveRedditKeys(RemoteRedditKeys(query, listing.after, listing.before))

                    // save mine subscriptions in table
                    database.redditMineSubscriptionsDao().saveMineSubscriptions(subreddits)
                }
            }

            MediatorResult.Success(endOfPaginationReached = listing?.after == null)
        } catch (exception: Throwable) {
            MediatorResult.Error(exception)
        }
    }

    private suspend fun getPosts(
        loadKey: RemoteRedditKeys?
    ): Response<RedditJsonWrapper<SubredditDataResponse>> {
        return Networking.redditApiOAuth.mineSubscriptions(
            after = loadKey?.after,
            before = loadKey?.before,
            limit = 100
        )
    }
}
