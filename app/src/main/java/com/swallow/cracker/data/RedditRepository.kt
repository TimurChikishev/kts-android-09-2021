package com.swallow.cracker.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.swallow.cracker.data.api.Networking

class RedditRepository {

    fun getSubreddit(
        subreddit: String,
        category: String,
        limit: String
    ) = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            RedditPagingSource(
                subreddit = subreddit,
                category = category,
                limit = limit
            )
        }
    ).liveData

    suspend fun savePost(category: String?, id: String) {
        Networking.redditApiOAuth.savedPost(category = category, id = id)
    }

    suspend fun unSavePost(id: String) {
        Networking.redditApiOAuth.unSavedPost(id = id)
    }

    suspend fun votePost(dir: Int, id: String) {
        Networking.redditApiOAuth.votePost(dir = dir, id = id)
    }

    suspend fun userIdentity() {
        Networking.redditApiOAuth.userIdentity()
    }
}