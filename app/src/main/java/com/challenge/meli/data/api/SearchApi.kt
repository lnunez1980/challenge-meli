package com.challenge.meli.data.api

import com.challenge.meli.data.models.SearchResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

const val SEARCH_API = "/sites/MLA/search?"

interface SearchApi {

    @GET(SEARCH_API)
    fun searchBy(
        @Query("q") query: String? = null,
    ): Single<SearchResponse>

}