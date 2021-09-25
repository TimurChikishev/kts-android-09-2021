package com.swallow.cracker.data.api

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.swallow.cracker.data.modal.RedditDataResponse
import com.swallow.cracker.data.modal.RedditJsonWrapper
import com.swallow.cracker.ui.modal.RedditList
import com.swallow.cracker.ui.modal.RedditMapper
import retrofit2.HttpException
import timber.log.Timber

class EverythingNewsPagingSource(
    private val limit: String
) : PagingSource<Int, RedditList>() {

    override fun getRefreshKey(state: PagingState<Int, RedditList>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RedditList> {
        val page: Int = params.key ?: 1

        return try   {
            val response = Networking.redditApi.getTop(Networking.after, limit)
            val responseBody = checkNotNull(response.body())
            Networking.after = responseBody.data.after.toString()

            val data =
                checkNotNull(response.body()).data.children.map { RedditMapper().mapApiToUi(it.data) }

            val nextKey = if (data.isEmpty()) null else page + 1
            val prevKey = if (page == 1) null else page - 1
            LoadResult.Page(data, prevKey, nextKey)
        } catch (exception: Throwable) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
}