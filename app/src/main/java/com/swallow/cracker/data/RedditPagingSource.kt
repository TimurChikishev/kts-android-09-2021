package com.swallow.cracker.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.swallow.cracker.data.api.Networking
import com.swallow.cracker.data.model.RedditMapper
import com.swallow.cracker.ui.model.QuerySubreddit
import com.swallow.cracker.ui.model.RedditItems

class RedditPagingSource(
    private val query: QuerySubreddit
) : PagingSource<String, RedditItems>() {

    override fun getRefreshKey(state: PagingState<String, RedditItems>): String? = null

    override suspend fun load(params: LoadParams<String>): LoadResult<String, RedditItems> {
        val pageSize: Int = params.loadSize

        return try {
            val response = Networking.redditApiOAuth.getSubreddit(
                subreddit = query.subreddit,
                category = query.category,
                limit = query.limit,
                count = pageSize.toString(),
                after = params.key,
                before = null
            )

            val responseBody = checkNotNull(response.body())
            val after = responseBody.data.after
            val before = params.key?.let { responseBody.data.before }

            val data =
                checkNotNull(response.body()).data.children.map { RedditMapper.mapApiToUi(it.data) }

            LoadResult.Page(
                data,
                before,
                after
            )
        } catch (exception: Throwable) {
            LoadResult.Error(exception)
        }
    }

    override val keyReuseSupported: Boolean = true
}
