package com.swallow.cracker.ui.modal

sealed interface RedditList {
    fun getItemId() : String {
        return when(this) {
            is RedditListItem -> this.id
            is RedditListItemWithImage -> this.id
        }
    }
}