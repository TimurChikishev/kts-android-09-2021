package com.swallow.cracker.ui.model

data class RedditListQuery(
    var query: String = "",
    var sorted: String = "best"
){
    val fullQuery: String
        get() = if(query.isEmpty()) sorted else "$query/$sorted"
}


