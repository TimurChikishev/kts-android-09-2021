package com.swallow.cracker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.swallow.cracker.data.database.model.RedditKeysContract
import com.swallow.cracker.data.model.RemoteRedditKeys

@Dao
interface RedditKeysDao {

    @Insert(onConflict = REPLACE)
    suspend fun saveRedditKeys(redditKey: RemoteRedditKeys)

    @Query("DELETE FROM ${RedditKeysContract.TABLE_NAME} WHERE ${RedditKeysContract.Columns.ID} = :id")
    suspend fun clearRedditKeysByQuery(id: String)

    @Query("DELETE FROM ${RedditKeysContract.TABLE_NAME}")
    suspend fun clearRedditKeys()

    @Query("SELECT * FROM ${RedditKeysContract.TABLE_NAME} WHERE ${RedditKeysContract.Columns.ID} = :id")
    suspend fun getRedditKeysByQuery(id: String): RemoteRedditKeys?
}