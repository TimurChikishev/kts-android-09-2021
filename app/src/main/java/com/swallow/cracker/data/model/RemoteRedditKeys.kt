package com.swallow.cracker.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.swallow.cracker.data.database.model.RedditKeysContract

@Entity(tableName = RedditKeysContract.TABLE_NAME)
data class RemoteRedditKeys(
    @PrimaryKey
    @ColumnInfo(name = RedditKeysContract.Columns.ID)
    val id: String,

    @ColumnInfo(name = RedditKeysContract.Columns.AFTER)
    val after: String?,

    @ColumnInfo(name = RedditKeysContract.Columns.BEFORE)
    val before: String?
)
