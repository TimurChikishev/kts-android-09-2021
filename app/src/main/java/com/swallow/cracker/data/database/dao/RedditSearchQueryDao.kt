package com.swallow.cracker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.swallow.cracker.data.database.model.RedditSearchQueryContract
import com.swallow.cracker.data.model.RemoteSearchQuery

@Dao
interface RedditSearchQueryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSearchQuery(redditProfile: RemoteSearchQuery)

    @Query("SELECT * FROM ${RedditSearchQueryContract.TABLE_NAME}")
    suspend fun getSearchQuery(): List<RemoteSearchQuery>?

    @Query("DELETE FROM ${RedditSearchQueryContract.TABLE_NAME} WHERE ${RedditSearchQueryContract.Columns.QUERY} = :query")
    suspend fun removeSearchQueryByQuery(query: String)

    @Query("DELETE FROM ${RedditSearchQueryContract.TABLE_NAME}")
    suspend fun clearSearchQuery()
}