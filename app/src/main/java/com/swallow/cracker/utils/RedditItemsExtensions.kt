package com.swallow.cracker.utils

import com.swallow.cracker.ui.model.RedditItems
import com.swallow.cracker.ui.model.RedditListItemImage
import com.swallow.cracker.ui.model.RedditListSimpleItem

fun RedditItems.id(): String {
    return when (this) {
        is RedditListSimpleItem -> this.t3_id
        is RedditListItemImage -> this.t3_id
    }
}

fun RedditItems.getLikeStatus(): Boolean? {
    return when (this) {
        is RedditListSimpleItem -> this.likes
        is RedditListItemImage -> this.likes
    }
}

fun RedditItems.setLikeStatus(likes: Boolean?) {
    return when (this) {
        is RedditListSimpleItem -> this.likes = likes
        is RedditListItemImage -> this.likes = likes
    }
}

fun RedditItems.setSavedStatus(saved: Boolean) {
    return when (this) {
        is RedditListSimpleItem -> this.saved = saved
        is RedditListItemImage -> this.saved = saved
    }
}

fun RedditItems.plusScore(value: Int) {
    return when (this) {
        is RedditListSimpleItem -> this.score += value
        is RedditListItemImage -> this.score += value
    }
}

fun RedditItems.updateScore(likes: Boolean) {
    if (likes) {
        when (this.getLikeStatus()) {
            true -> {
                this.setLikeStatus(null)
                this.plusScore(-1)
            }
            false -> {
                this.setLikeStatus(true)
                this.plusScore(2)
            }
            null -> {
                this.setLikeStatus(true)
                this.plusScore(1)
            }
        }
    } else {
        when (this.getLikeStatus()) {
            true -> {
                this.setLikeStatus(false)
                this.plusScore(-2)
            }
            false -> {
                this.setLikeStatus(null)
                this.plusScore(1)
            }
            null -> {
                this.setLikeStatus(false)
                this.plusScore(-1)
            }
        }
    }
}

fun RedditItems.getVoteDir(likes: Boolean): Int {
    return if (likes) {
        when (this.getLikeStatus()) {
            true -> 0
            false -> 1
            null -> 1
        }
    } else {
        when (this.getLikeStatus()) {
            true -> -1
            false -> 0
            null -> -1
        }
    }
}