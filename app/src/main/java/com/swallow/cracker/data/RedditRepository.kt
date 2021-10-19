package com.swallow.cracker.data

import com.swallow.cracker.data.api.Networking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class RedditRepository() {
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