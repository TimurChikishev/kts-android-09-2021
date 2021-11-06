package com.swallow.cracker.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import com.swallow.cracker.data.database.RedditDatabase
import com.swallow.cracker.data.model.RedditJsonWrapper
import com.swallow.cracker.data.model.RemoteRedditKeys
import com.swallow.cracker.data.model.listing.RedditDataResponse
import com.swallow.cracker.data.model.listing.RemoteRedditPost
import com.swallow.cracker.data.network.Networking
import retrofit2.Response


@OptIn(ExperimentalPagingApi::class)
class RedditSearchRemoteMediator(
    private val query: String,
    database: RedditDatabase
) : RedditListingRemoteMediator(query, database) {

    override suspend fun getPosts(
        loadKey: RemoteRedditKeys?,
        loadType: LoadType,
        state: PagingState<Int, RemoteRedditPost>
    ): Response<RedditJsonWrapper<RedditDataResponse>> {
        return Networking.redditApiOAuth.search(
            query = query,
            after = loadKey?.after,
            before = loadKey?.before,
            limit = when (loadType) {
                LoadType.REFRESH -> state.config.initialLoadSize
                else -> state.config.pageSize
            }
        )
    }
}