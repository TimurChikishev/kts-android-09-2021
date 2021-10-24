package com.swallow.cracker.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.swallow.cracker.data.api.Networking
import com.swallow.cracker.data.config.NetworkConfig
import com.swallow.cracker.ui.model.QuerySubreddit
import com.swallow.cracker.ui.model.RedditItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class RedditRepository {

    fun getPager(query: QuerySubreddit): Pager<String, RedditItems> {
        return Pager(
            PagingConfig(
                pageSize = NetworkConfig.PAGE_SIZE,
                initialLoadSize = NetworkConfig.INITIAL_LOAD_SIZE,
                enablePlaceholders = false
            )
        ) {
            RedditPagingSource(query)
        }
    }

    fun savePost(category: String?, id: String): Flow<Response<Unit>> = flow {
        emit(Networking.redditApiOAuth.savedPost(category = category, id = id))
    }

    suspend fun unSavePost(id: String): Flow<Response<Unit>> = flow {
        emit(Networking.redditApiOAuth.unSavedPost(id = id))
    }

    suspend fun votePost(dir: Int, id: String): Flow<Response<Unit>> = flow {
        emit(Networking.redditApiOAuth.votePost(dir = dir, id = id))
    }
}