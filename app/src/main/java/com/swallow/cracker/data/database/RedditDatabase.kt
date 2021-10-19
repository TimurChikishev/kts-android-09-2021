package com.swallow.cracker.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.swallow.cracker.data.database.dao.RedditKeysDao
import com.swallow.cracker.data.database.dao.RedditPostsDao
import com.swallow.cracker.data.model.RedditKeys
import com.swallow.cracker.data.model.RedditPost



@Database(
    entities = [RedditPost::class, RedditKeys::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(TypeConverter::class)
abstract class RedditDatabase : RoomDatabase() {
    companion object {
        fun create(context: Context): RedditDatabase {
            val databaseBuilder =
                Room.databaseBuilder(context, RedditDatabase::class.java, "redditclone.db")
            return databaseBuilder.build()
        }
    }

    abstract fun redditPostsDao(): RedditPostsDao
    abstract fun redditKeysDao(): RedditKeysDao
}