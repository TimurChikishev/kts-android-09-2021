package com.swallow.cracker.ui.model

sealed interface RedditItem {
    fun id(): String

    fun getLikeStatus(): Boolean?

    fun setLikeStatus(likes: Boolean?)

    fun setSavedStatus(saved: Boolean)

    fun plusScore(value: Int)

    fun equalsContent(other: Any?) : Boolean
}