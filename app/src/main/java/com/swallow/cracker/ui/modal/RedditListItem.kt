package com.swallow.cracker.ui.modal

import com.swallow.cracker.utils.convertLongToTime

data class RedditListItem(
    var id: String,
    var author: String,
    var title: String,
    var selftext: String,
    val ups: Int,
    var numComments: Int,
    var created: Long,
    var url: String
) : RedditList {

    val time: String
        get() = created.convertLongToTime()

}