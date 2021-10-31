package com.swallow.cracker.ui.model

import com.swallow.cracker.data.model.listing.RedditChildrenPreview

sealed interface RedditItem {
    var id: String
    var t3_id: String
    var likes: Boolean?
    var subredditId: String
    var saved: Boolean
    var url: String
    var score: Int
    var selfText: String
    var communityIcon: String?
    var subreddit: String
    var title: String
    var author: String
    var numComments: Int
    var created: Long

    val time: String

    fun thumbnail(): String? = null

    fun preview(): RedditChildrenPreview? = null

    fun plusScore(value: Int)

    fun equalsContent(other: Any?) : Boolean

    fun payload(other: Any): Payload = Payload.None

    interface Payload {
        object None: Payload
    }
}