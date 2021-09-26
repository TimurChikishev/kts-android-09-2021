package com.swallow.cracker.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.swallow.cracker.data.api.Networking
import com.swallow.cracker.ui.modal.RedditList
import com.swallow.cracker.ui.modal.RedditMapper
import retrofit2.HttpException

class RedditPagingSource(
    private val limit: String
) : PagingSource<String, RedditList>() {

    override fun getRefreshKey(state: PagingState<String, RedditList>): String? = null

    override suspend fun load(params: LoadParams<String>): LoadResult<String, RedditList> {
        val pageSize: Int = params.loadSize

        return try {
            val response = Networking.redditApi.getTop(
                limit = limit,
                count = pageSize.toString(),
                after = params.key,
                before = null
            )

            val responseBody = checkNotNull(response.body())
            val after = responseBody.data.after.toString()
            val before = if (params.key == null) null else responseBody.data.before.toString()

            val data =
                checkNotNull(response.body()).data.children.map { RedditMapper().mapApiToUi(it.data) }

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