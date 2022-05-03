package com.challenge.meli.ui.fragments.repositories

import com.challenge.meli.data.api.SearchApi
import com.challenge.meli.data.database.RecentSearchDao
import com.challenge.meli.data.database.models.RecentSearches
import com.challenge.meli.data.models.Product
import io.reactivex.Single
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val recentSearchDao: RecentSearchDao,
    private val searchApi: SearchApi
) {

    fun addRecentSearches(recentSearches: RecentSearches): Long {
        return recentSearchDao.addRecentSearches(recentSearches)
    }

    fun searchBy(
        query: String? = null
    ): Single<List<Product>> {
        return searchApi.searchBy(query)
            .map { response -> response.results }
    }
}