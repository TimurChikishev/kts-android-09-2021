package com.swallow.cracker.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.swallow.cracker.data.database.dao.*
import com.swallow.cracker.data.model.RemoteRedditKeys
import com.swallow.cracker.data.model.RemoteSearchQuery
import com.swallow.cracker.data.model.listing.RemoteRedditPost
import com.swallow.cracker.data.model.profile.RemoteRedditProfile
import com.swallow.cracker.data.model.subreddit.RemoteSubreddit

@Database(
    entities = [
        RemoteRedditPost::class,
        RemoteRedditKeys::class,
        RemoteRedditProfile::class,
        RemoteSearchQuery::class,
        RemoteSubreddit::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(TypeConverter::class)
abstract class RedditDatabase : RoomDatabase() {
    companion object {
        fun create(context: Context): RedditDatabase {
            val databaseBuilder =
                Room.databaseBuilder(context, RedditDatabase::class.java, "reddit.db")
            return databaseBuilder.build()
        }
    }

    abstract fun redditPostsDao(): RedditPostsDao
    abstract fun redditKeysDao(): RedditKeysDao
    abstract fun redditProfileDao(): RedditProfileDao
    abstract fun redditSearchQueryDao(): RedditSearchQueryDao
    abstract fun redditMineSubscriptionsDao(): RedditMineSubscriptionsDao
}