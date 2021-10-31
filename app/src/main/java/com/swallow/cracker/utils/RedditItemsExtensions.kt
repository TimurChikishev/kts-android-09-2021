package com.swallow.cracker.utils

import com.swallow.cracker.ui.model.RedditItem

fun RedditItem.updateScore(likes: Boolean) {
    if (likes) {
        when (this.likes) {
            true -> {
                this.likes = null
                this.plusScore(-1)
            }
            false -> {
                this.likes = true
                this.plusScore(2)
            }
            null -> {
                this.likes = true
                this.plusScore(1)
            }
        }
    } else {
        when (this.likes) {
            true -> {
                this.likes = false
                this.plusScore(-2)
            }
            false -> {
                this.likes = null
                this.plusScore(1)
            }
            null -> {
                this.likes = false
                this.plusScore(-1)
            }
        }
    }
}

fun RedditItem.getVoteDir(likes: Boolean): Int {
    return if (likes) {
        when (this.likes) {
            true -> 0
            false -> 1
            null -> 1
        }
    } else {
        when (this.likes) {
            true -> -1
            false -> 0
            null -> -1
        }
    }
}