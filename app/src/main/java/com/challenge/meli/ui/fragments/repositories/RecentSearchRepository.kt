package com.challenge.meli.ui.fragments.repositories

import com.challenge.meli.data.database.RecentSearchDao
import com.challenge.meli.data.database.models.RecentSearches
import io.reactivex.Single
import javax.inject.Inject

class RecentSearchRepository @Inject constructor(
    private val recentSearchDao: RecentSearchDao
) {

    fun getLocalRecentSearches(): Single<List<RecentSearches>> {
        return recentSearchDao.getRecentSearches()
    }

    fun filterRecentSearches(filter: String): Single<List<RecentSearches>> {
        return recentSearchDao.filterRecentSearch(filter)
    }

    fun addRecentSearches(recentSearches: RecentSearches): Long {
        return recentSearchDao.addRecentSearches(recentSearches)
    }
}