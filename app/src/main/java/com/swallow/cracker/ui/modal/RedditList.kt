package com.swallow.cracker.ui.modal

sealed interface RedditList {
    fun getItemId(): String {
        return when (this) {
            is RedditListItem -> this.id
            is RedditListItemWithImage -> this.id
            else -> error("Нельзя получить id для $this")
        }
    }

    fun getLikeStatus(): Boolean? {
        return when (this) {
            is RedditListItem -> this.likes
            is RedditListItemWithImage -> this.likes
            else -> error("Нельзя получить likes для $this")
        }
    }

    fun setLikeStatus(likes: Boolean?) {
        return when (this) {
            is RedditListItem -> this.likes = likes
            is RedditListItemWithImage -> this.likes = likes
            else -> error("Нельзя установить likes для $this")
        }
    }

    fun plusScore(value: Int) {
        return when (this) {
            is RedditListItem -> this.score += value
            is RedditListItemWithImage -> this.score += value
            else -> error("Нельзя увеличить score для $this")
        }
    }

}