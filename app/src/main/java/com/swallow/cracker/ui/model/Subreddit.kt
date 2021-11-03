package com.swallow.cracker.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Subreddit(
    var id: String,
    var displayName: String,
    var displayNamePrefixed: String,
    var name: String,
    var url: String,
    var communityIcon: String,
    var iconImg: String?,
    var bannerImg: String?,
    var subscribers: Int?,
    var title: String,
    var publicDescription: String,
    var description: String?,
    var created: Long,
    var userIsSubscriber: Boolean?,
    var activeUserCount: Int?
) : Parcelable