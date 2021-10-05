package com.swallow.cracker.utils

import com.swallow.cracker.ui.modal.RedditList
import com.swallow.cracker.ui.modal.RedditListSimpleItem
import com.swallow.cracker.ui.modal.RedditListItemWithImage

fun RedditList.getItemId(): String {
    return when (this) {
        is RedditListSimpleItem -> this.id
        is RedditListItemWithImage -> this.id
    }
}

fun RedditList.getLikeStatus(): Boolean? {
    return when (this) {
        is RedditListSimpleItem -> this.likes
        is RedditListItemWithImage -> this.likes
    }
}

fun RedditList.setLikeStatus(likes: Boolean?) {
    return when (this) {
        is RedditListSimpleItem -> this.likes = likes
        is RedditListItemWithImage -> this.likes = likes
    }
}

fun RedditList.getSavedStatus(): Boolean {
    return when (this) {
        is RedditListSimpleItem -> this.saved
        is RedditListItemWithImage -> this.saved
    }
}

fun RedditList.setSavedStatus(saved: Boolean) {
    return when (this) {
        is RedditListSimpleItem -> this.saved = saved
        is RedditListItemWithImage -> this.saved = saved
    }
}

fun RedditList.plusScore(value: Int) {
    return when (this) {
        is RedditListSimpleItem -> this.score += value
        is RedditListItemWithImage -> this.score += value
    }
}

fun RedditList.updateScore(likes: Boolean) {
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