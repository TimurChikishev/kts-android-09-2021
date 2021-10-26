package com.swallow.cracker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.swallow.cracker.data.model.RemoteRedditKeys

@Dao
interface RedditKeysDao {

    @Insert(onConflict = REPLACE)
    suspend fun saveRedditKeys(redditKey: RemoteRedditKeys)

    @Query("SELECT * FROM redditKeys WHERE id=:t3_id")
    suspend fun getRedditKeys(t3_id: String): List<RemoteRedditKeys>

    @Query("DELETE FROM redditKeys")
    suspend fun clearRedditKeys()
}