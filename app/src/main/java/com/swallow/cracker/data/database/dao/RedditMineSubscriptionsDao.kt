package com.swallow.cracker.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.swallow.cracker.data.database.model.RedditMineSubscriptionsContract
import com.swallow.cracker.data.model.subreddit.RemoteSubreddit

@Dao
interface RedditMineSubscriptionsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMineSubscriptions(subreddits: List<RemoteSubreddit>)

    @Query("DELETE FROM ${RedditMineSubscriptionsContract.TABLE_NAME}")
    suspend fun clearMineSubscriptions()

    @Query("SELECT * FROM ${RedditMineSubscriptionsContract.TABLE_NAME}")
    fun getMineSubscriptions(): PagingSource<Int, RemoteSubreddit>
}