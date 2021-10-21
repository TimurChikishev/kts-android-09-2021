package com.swallow.cracker.data.database

import android.content.Context


object Database {
    lateinit var redditDatabase: RedditDatabase
        private set

    fun initRedditDatabase(context: Context){
        redditDatabase = RedditDatabase.create(context)
    }
}