package com.swallow.cracker.ui.model

sealed interface RedditItem {
    fun id(): String

    fun likes(): Boolean?

    fun setItemLikes(likes: Boolean?)

    fun setItemSaved(saved: Boolean)

    fun plusScore(value: Int)

    fun score(): Int

    fun equalsContent(other: Any?) : Boolean
}