package com.swallow.cracker.data.database

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.swallow.cracker.data.model.listing.RedditChildrenPreview
import com.swallow.cracker.data.model.profile.RemoteProfileInfoSubreddit

class TypeConverter {
    @TypeConverter
    fun redditChildrenPreviewToJson(preview: RedditChildrenPreview?): String? {
        preview ?: return null

        val moshi: Moshi = Moshi.Builder().build()
        val adapter: JsonAdapter<RedditChildrenPreview> = moshi.adapter(RedditChildrenPreview::class.java)

        return adapter.toJson(preview)
    }

    @TypeConverter
    fun jsonToRedditChildrenPreview(preview: String?): RedditChildrenPreview? {
        preview ?: return null

        val moshi: Moshi = Moshi.Builder().build()
        val adapter: JsonAdapter<RedditChildrenPreview> = moshi.adapter(RedditChildrenPreview::class.java)

        return adapter.fromJson(preview)
    }

    @TypeConverter
    fun profileInfoSubredditToJson(infoSubreddit: RemoteProfileInfoSubreddit?): String? {
        infoSubreddit ?: return null

        val moshi: Moshi = Moshi.Builder().build()
        val adapter: JsonAdapter<RemoteProfileInfoSubreddit> = moshi.adapter(RemoteProfileInfoSubreddit::class.java)

        return adapter.toJson(infoSubreddit)
    }

    @TypeConverter
    fun jsonToProfileInfoSubreddit(infoSubreddit: String?): RemoteProfileInfoSubreddit? {
        infoSubreddit ?: return null

        val moshi: Moshi = Moshi.Builder().build()
        val adapter: JsonAdapter<RemoteProfileInfoSubreddit> = moshi.adapter(RemoteProfileInfoSubreddit::class.java)

        return adapter.fromJson(infoSubreddit)
    }
}