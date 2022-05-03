package com.challenge.meli.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.challenge.meli.data.database.models.RecentSearches
import io.reactivex.Single

@Dao
interface RecentSearchDao {

    @Query("SELECT * FROM recent_searches GROUP BY search")
    fun getRecentSearches(): Single<List<RecentSearches>>

    @Query("SELECT * FROM recent_searches WHERE search LIKE '%' || :filter || '%' GROUP BY search")
    fun filterRecentSearch(filter: String): Single<List<RecentSearches>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addRecentSearches(recentSearches: RecentSearches): Long

}