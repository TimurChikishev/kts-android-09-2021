package com.swallow.cracker.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.swallow.cracker.data.database.model.RedditSearchQueryContract

@Entity(tableName = RedditSearchQueryContract.TABLE_NAME)
data class RemoteSearchQuery(
    @PrimaryKey
    @ColumnInfo(name = RedditSearchQueryContract.Columns.QUERY)
    var query: String
)
