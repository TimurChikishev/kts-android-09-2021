package com.swallow.cracker.utils

import com.swallow.cracker.ui.model.RedditItem

fun RedditItem.updateScore(likes: Boolean) {
    if (likes) {
        when (this.likes()) {
            true -> {
                this.setItemLikes(null)
                this.plusScore(-1)
            }
            false -> {
                this.setItemLikes(true)
                this.plusScore(2)
            }
            null -> {
                this.setItemLikes(true)
                this.plusScore(1)
            }
        }
    } else {
        when (this.likes()) {
            true -> {
                this.setItemLikes(false)
                this.plusScore(-2)
            }
            false -> {
                this.setItemLikes(null)
                this.plusScore(1)
            }
            null -> {
                this.setItemLikes(false)
                this.plusScore(-1)
            }
        }
    }
}

fun RedditItem.getVoteDir(likes: Boolean): Int {
    return if (likes) {
        when (this.likes()) {
            true -> 0
            false -> 1
            null -> 1
        }
    } else {
        when (this.likes()) {
            true -> -1
            false -> 0
            null -> -1
        }
    }
}