package com.challenge.meli.repositories

import com.challenge.meli.data.database.RecentSearchDao
import com.challenge.meli.data.database.models.RecentSearches
import com.challenge.meli.ui.fragments.repositories.RecentSearchRepository
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RecentSearchRepositoryTest {

    @Mock
    lateinit var recentSearchDao: RecentSearchDao

    @Mock
    lateinit var recentsearchs: List<RecentSearches>

    private lateinit var searchRepository: RecentSearchRepository

    @Before
    fun setUp() {
        searchRepository = RecentSearchRepository(recentSearchDao)
    }

    @Test
    fun `get recent searches returns a data when exist`() {
        Mockito.`when`(recentSearchDao.getRecentSearches())
            .thenReturn(Single.just(recentsearchs))
        searchRepository.getLocalRecentSearches()
            .test()
            .assertNoErrors()
            .dispose()
    }

    @Test
    fun `call method filterRecentSearch and returns a data filtered`() {
        val recentSearches = listOf(
            RecentSearches(search = "Motorola"),
            RecentSearches(search = "Nokia")
        )
        Mockito.`when`(recentSearchDao.filterRecentSearch(String()))
            .thenReturn(Single.just(recentSearches.take(1)))
        searchRepository.filterRecentSearches(String())
            .test()
            .assertValue {
                it.size < recentSearches.size
            }
    }

    @Test
    fun `call method filterRecentSearch and returns error`() {
        val exception = Exception("Mock data error")
        Mockito.`when`(recentSearchDao.filterRecentSearch(String()))
            .thenReturn(Single.error(exception))
        searchRepository.filterRecentSearches(String())
            .test()
            .assertError(exception)
            .dispose()
    }

    @Test
    fun `when recent lookups are get they throw an exception when something happens in the local database`() {
        val exception = Exception("Mock data error")
        Mockito.`when`(recentSearchDao.getRecentSearches())
            .thenReturn(Single.error(exception))
        searchRepository.getLocalRecentSearches()
            .test()
            .assertError(exception)
            .dispose()
    }
}