package com.swallow.cracker.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.swallow.cracker.data.database.model.RedditPostContract
import com.swallow.cracker.data.model.RemoteRedditPost

@Dao
interface RedditPostsDao {

    @Insert(onConflict = REPLACE)
    suspend fun savePosts(redditPosts: List<RemoteRedditPost>)

    @Query("DELETE FROM ${RedditPostContract.TABLE_NAME}")
    suspend fun clearPosts()

    @Query("DELETE FROM ${RedditPostContract.TABLE_NAME} WHERE ${RedditPostContract.Columns.QUERY} = :query")
    suspend fun clearPostsByQuery(query: String)

    @Query("SELECT * FROM ${RedditPostContract.TABLE_NAME}")
    fun getPosts(): PagingSource<Int, RemoteRedditPost>

    @Query("UPDATE ${RedditPostContract.TABLE_NAME} SET ${RedditPostContract.Columns.LIKES} =:likes, ${RedditPostContract.Columns.SCORE} =:score WHERE ${RedditPostContract.Columns.T3_ID} =:id")
    suspend fun updatePostLikes(likes: Boolean?, score: Int, id: String)

    @Query("UPDATE ${RedditPostContract.TABLE_NAME} SET ${RedditPostContract.Columns.SAVED} =:saved WHERE ${RedditPostContract.Columns.T3_ID} =:id")
    suspend fun updatePostSaved(saved: Boolean, id: String)
}