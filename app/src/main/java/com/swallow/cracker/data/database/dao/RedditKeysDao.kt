package com.swallow.cracker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.swallow.cracker.data.model.RedditKeys

@Dao
interface RedditKeysDao {

    @Insert(onConflict = REPLACE)
    suspend fun saveRedditKeys(redditKey: RedditKeys)

    @Query("SELECT * FROM redditKeys WHERE id=:t3_id")
    suspend fun getRedditKeys(t3_id: String): List<RedditKeys>

    @Query("DELETE FROM redditKeys")
    suspend fun clearRedditKeys()
}