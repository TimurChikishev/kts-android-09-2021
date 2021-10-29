package com.swallow.cracker.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.swallow.cracker.data.api.RedditApi
import com.swallow.cracker.data.database.RedditDatabase
import com.swallow.cracker.data.model.RemoteRedditKeys
import com.swallow.cracker.data.model.RemoteRedditPost
import com.swallow.cracker.utils.fixImgUrl
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class RedditRemoteMediator(
    private val query: String,
    private val redditApi: RedditApi,
    private val database: RedditDatabase
) : RemoteMediator<Int, RemoteRedditPost>() {

    private var subredditHashMap: HashMap<String, String?> = hashMapOf()

    override suspend fun initialize(): InitializeAction {
        // Require that remote REFRESH is launched on initial load and succeeds before launching
        // remote PREPEND / APPEND.
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RemoteRedditPost>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKey(query)
                    remoteKeys?.after
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    remoteKeys
                }
            }

            val response = redditApi.getSubreddit(
                query = query,
                after = loadKey?.after,
                before = loadKey?.before,
                limit = when (loadType) {
                    LoadType.REFRESH -> state.config.initialLoadSize
                    else -> state.config.pageSize
                }
            )

            Timber.tag("TAG").d("_______ query: $query _______")
            Timber.tag("TAG").d("_______ state: $loadType _______")
            Timber.tag("TAG").d("_______ after: ${loadKey?.after} _______")
            Timber.tag("TAG").d("_______ before: ${loadKey?.before} _______")

            val listing = response.body()?.data
            val redditPosts = listing?.children?.map {
                it.data.communityIcon = getSubredditIcon(it.data)
                it.data.query = query
                it.data
            }

            if (redditPosts != null) {
                database.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        database.redditKeysDao().clearRedditKeysByQuery(query)
                        database.redditPostsDao().clearPostsByQuery(query)
                    }

                    database.redditKeysDao()
                        .saveRedditKeys(RemoteRedditKeys(query, listing.after, listing.before))

                    database.redditPostsDao().savePosts(redditPosts)
                }
            }

            MediatorResult.Success(endOfPaginationReached = listing?.after == null)
        } catch (exception: Throwable) {
            MediatorResult.Error(exception)
        }
    }

    private suspend fun getSubredditIcon(post: RemoteRedditPost): String? {
        val subredditId = post.subredditId

        return if (subredditHashMap.containsKey(subredditId)) {
            subredditHashMap[subredditId]
        } else {
            val info = redditApi.getSubredditInfo(post.subreddit).body()
            subredditHashMap[subredditId] = info?.data?.communityIcon?.fixImgUrl()
            subredditHashMap[subredditId]
        }
    }

    /**
     * get the last remote key inserted which had the data
     */
    private suspend fun getRemoteKey(query: String): RemoteRedditKeys? {
        return database.redditKeysDao().getRedditKeysByQuery(query)
    }
}