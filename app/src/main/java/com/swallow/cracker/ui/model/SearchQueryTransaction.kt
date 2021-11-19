package com.swallow.cracker.ui.model

data class SearchQueryTransaction(
    val query: String,
    val subreddit: Subreddit? = null,
    val isSuccess: Boolean = true
)
