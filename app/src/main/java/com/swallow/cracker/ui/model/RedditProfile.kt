package com.swallow.cracker.ui.model

data class RedditProfile(
    val id: String,
    val name: String,
    val iconImage: String?,
    val avatarImg: String?,
    val created: String,
    val totalKarma: Int,
    val bannerImg: String?,
    val displayName: String,
    val url: String
)