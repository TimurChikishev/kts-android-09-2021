package com.swallow.cracker.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.swallow.cracker.data.model.RedditPost

@Dao
interface RedditPostsDao {

    @Insert(onConflict = REPLACE)
    suspend fun savePosts(redditPosts: List<RedditPost>)

    @Query("DELETE FROM redditPosts")
    suspend fun clearPosts()

    @Query("SELECT * FROM redditPosts")
    fun getPosts(): PagingSource<Int, RedditPost>

    @Query("UPDATE redditPosts SET likes =:likes, score =:score WHERE t3_id =:id")
    fun updatePostLikes(likes: Boolean?, score: Int, id: String)

    @Query("UPDATE redditPosts SET saved =:saved WHERE t3_id =:id")
    fun updatePostSaved(saved: Boolean, id: String)
}