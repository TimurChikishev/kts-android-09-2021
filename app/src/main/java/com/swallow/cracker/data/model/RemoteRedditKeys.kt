package com.swallow.cracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "redditKeys")
data class RemoteRedditKeys(
    @PrimaryKey
    val id: String,
    val after: String?,
    val before: String?
)
