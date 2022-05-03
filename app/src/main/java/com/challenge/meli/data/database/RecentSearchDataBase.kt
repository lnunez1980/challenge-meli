package com.challenge.meli.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.challenge.meli.data.database.models.RecentSearches

@Database(
    entities = [
        RecentSearches::class
    ], version = 1, exportSchema = false
)
abstract class RecentSearchDataBase : RoomDatabase() {

    abstract fun recentSearchDao(): RecentSearchDao
}