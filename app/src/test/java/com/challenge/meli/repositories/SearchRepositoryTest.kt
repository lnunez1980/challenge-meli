package com.challenge.meli.repositories

import com.challenge.meli.data.api.SearchApi
import com.challenge.meli.data.database.RecentSearchDao
import com.challenge.meli.data.models.SearchResponse
import com.challenge.meli.ui.fragments.repositories.SearchRepository
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SearchRepositoryTest  {

    @Mock
    lateinit var api: SearchApi

    @Mock
    lateinit var recentSearchDao: RecentSearchDao

    @Mock
    lateinit var searchResponse: SearchResponse

    private lateinit var searchRepository: SearchRepository

    private val query = "PS5"

    @Before
    fun setUp() {
        searchRepository = SearchRepository(recentSearchDao, api)
    }

    @Test
    fun `Searching by query should returns a properly data when there are results available`() {
        Mockito.`when`(api.searchBy(query))
            .thenReturn(Single.just(searchResponse))
        searchRepository.searchBy(query)
            .test()
            .assertNoErrors()
            .dispose()
    }

    @Test
    fun `Searching by query should return an exception when something happens in back`() {
        val exception = Exception("Mock data error")
        Mockito.`when`(api.searchBy(query))
            .thenReturn(Single.error(exception))
        searchRepository.searchBy(query)
            .test()
            .assertError(exception)
            .dispose()
    }
}