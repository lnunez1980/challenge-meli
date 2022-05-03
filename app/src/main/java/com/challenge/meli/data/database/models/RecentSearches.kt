package com.challenge.meli.data.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_searches")
data class RecentSearches(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "search") val search: String
)