package com.swallow.cracker.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.swallow.cracker.data.api.EverythingNewsPagingSource
import com.swallow.cracker.data.api.Networking
import com.swallow.cracker.data.modal.RedditDataResponse
import com.swallow.cracker.data.modal.RedditJsonWrapper
import retrofit2.Response

class RedditRepository {

    fun getTop(limit: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { EverythingNewsPagingSource(limit = limit) }
        ).liveData

}