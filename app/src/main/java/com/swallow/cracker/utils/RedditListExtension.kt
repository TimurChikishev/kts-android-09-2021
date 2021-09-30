package com.swallow.cracker.utils

import com.swallow.cracker.ui.modal.RedditList
import com.swallow.cracker.ui.modal.RedditListItem
import com.swallow.cracker.ui.modal.RedditListItemWithImage

fun RedditList.getItemId(): String {
    return when (this) {
        is RedditListItem -> this.id
        is RedditListItemWithImage -> this.id
        else -> error("Нельзя получить id для $this")
    }
}

fun RedditList.getLikeStatus(): Boolean? {
    return when (this) {
        is RedditListItem -> this.likes
        is RedditListItemWithImage -> this.likes
        else -> error("Нельзя получить likes для $this")
    }
}

fun RedditList.setLikeStatus(likes: Boolean?) {
    return when (this) {
        is RedditListItem -> this.likes = likes
        is RedditListItemWithImage -> this.likes = likes
        else -> error("Нельзя установить likes для $this")
    }
}

fun RedditList.plusScore(value: Int) {
    return when (this) {
        is RedditListItem -> this.score += value
        is RedditListItemWithImage -> this.score += value
        else -> error("Нельзя увеличить score для $this")
    }
}

fun RedditList.updateScore(likes: Boolean){
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