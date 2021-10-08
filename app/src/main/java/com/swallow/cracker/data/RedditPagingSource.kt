package com.swallow.cracker.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.swallow.cracker.data.api.Networking
import com.swallow.cracker.data.model.RedditMapper
import com.swallow.cracker.ui.model.RedditList
import retrofit2.HttpException

class RedditPagingSource(
    private val subreddit: String,
    private val category: String,
    private val limit: String
) : PagingSource<String, RedditList>() {

    override fun getRefreshKey(state: PagingState<String, RedditList>): String? = null

    override suspend fun load(params: LoadParams<String>): LoadResult<String, RedditList> {
        val pageSize: Int = params.loadSize

        return try {
            val response = Networking.redditApiOAuth.getSubreddit(
                subreddit = subreddit,
                category = category,
                limit = limit,
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
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override val keyReuseSupported: Boolean = true
}