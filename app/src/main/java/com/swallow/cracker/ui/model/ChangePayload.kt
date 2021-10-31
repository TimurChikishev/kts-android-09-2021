package com.swallow.cracker.ui.model

sealed class ChangePayload: RedditItem.Payload {
    data class LikeChanged(val likes: Boolean?, val score: Int): ChangePayload()
    data class SavedChanged(val saved: Boolean): ChangePayload()
}