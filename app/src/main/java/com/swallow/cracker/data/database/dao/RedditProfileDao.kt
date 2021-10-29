package com.swallow.cracker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.swallow.cracker.data.database.model.RedditProfileContract
import com.swallow.cracker.data.model.RemoteRedditProfile

@Dao
interface RedditProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProfile(redditProfile: RemoteRedditProfile)

    @Query("SELECT * FROM ${RedditProfileContract.TABLE_NAME} WHERE ${RedditProfileContract.Columns.ID} = :id")
    suspend fun getProfileById(id: String): RemoteRedditProfile?

    @Query("DELETE FROM ${RedditProfileContract.TABLE_NAME}")
    suspend fun clearRedditProfile()
}