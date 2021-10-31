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
import com.swallow.cracker.ui.model.QuerySubreddit
import com.swallow.cracker.utils.fixImgUrl
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class RedditRemoteMediator(
    private val query: QuerySubreddit,
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
            val loadKey = when (val pageKeyData = getKeyPageData(loadType, state)) {
                is MediatorResult.Success -> {
                    return pageKeyData
                }
                else -> {
                    pageKeyData as RemoteRedditKeys?
                }
            }

            val response = redditApi.getSubreddit(
                subreddit = query.subreddit,
                category = query.category,
                limit =  state.config.pageSize.toString(),
                after = loadKey?.after,
                before = loadKey?.before
            )

            val listing = response.body()?.data
            val redditPosts = listing?.children?.map { it.data }

            if (redditPosts != null) {
                database.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        database.redditKeysDao().clearRedditKeys()
                        database.redditPostsDao().clearPosts()
                    }

                    redditPosts.map {
                        it.communityIcon = getSubredditIcon(it)

                        database.redditKeysDao()
                            .saveRedditKeys(RemoteRedditKeys(it.t3_id, listing.after, listing.before))
                    }

                    database.redditPostsDao().savePosts(redditPosts)
                }
            }

            MediatorResult.Success(endOfPaginationReached = listing?.after == null)
        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: HttpException) {
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
     * this returns the page key or the final end of list success result
     */
    private suspend fun getKeyPageData(
        loadType: LoadType,
        state: PagingState<Int, RemoteRedditPost>
    ): Any? {
        return when (loadType) {
            LoadType.REFRESH -> null
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                remoteKeys?.before
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                remoteKeys
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                remoteKeys?.after
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                remoteKeys
            }
        }
    }

    /**
     * get the last remote key inserted which had the data
     */
    private suspend fun getFirstRemoteKey(state: PagingState<Int, RemoteRedditPost>): RemoteRedditKeys? {
        return state.pages
            .firstOrNull() { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { database.redditKeysDao().getRedditKeys(it.t3_id).firstOrNull() }
    }

    /**
     * get the last remote key inserted which had the data
     */
    private suspend fun getLastRemoteKey(state: PagingState<Int, RemoteRedditPost>): RemoteRedditKeys? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { database.redditKeysDao().getRedditKeys(it.t3_id).lastOrNull() }
    }
}