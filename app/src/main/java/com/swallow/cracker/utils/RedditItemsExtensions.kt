package com.swallow.cracker.utils

import com.swallow.cracker.ui.model.RedditItem

fun RedditItem.updateScore(likes: Boolean) {
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

fun RedditItem.getVoteDir(likes: Boolean): Int {
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