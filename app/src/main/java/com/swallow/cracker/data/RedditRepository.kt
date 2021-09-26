package com.swallow.cracker.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData

class RedditRepository {

    fun getTop(limit: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { RedditPagingSource(limit = limit) }
        ).liveData

}