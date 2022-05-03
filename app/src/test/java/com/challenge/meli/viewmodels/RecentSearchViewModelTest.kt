package com.challenge.meli.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.challenge.meli.data.database.models.RecentSearches
import com.challenge.meli.ui.fragments.repositories.RecentSearchRepository
import com.challenge.meli.ui.fragments.viewModels.RecentSearchViewModel
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RecentSearchViewModelTest {

    @Mock
    lateinit var recentSearchActions: Observer<RecentSearchViewModel.RecentSearchActions>

    @Mock
    lateinit var recentSearchRepository: RecentSearchRepository

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: RecentSearchViewModel

    @Mock
    lateinit var recentsearchs: List<RecentSearches>

    @Before
    fun setUp() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        viewModel = RecentSearchViewModel(recentSearchRepository)

        viewModel.recentSearchActions.observeForever(recentSearchActions)
    }

    @Test
    fun `Verify that the recent search method retrieves data`() {
        Mockito.`when`(recentSearchRepository.getLocalRecentSearches())
            .thenReturn(Single.just(recentsearchs))

        viewModel.getRecentSearches()
        Assert.assertTrue(viewModel.recentSearchActions.value is RecentSearchViewModel.RecentSearchActions.RecentSearchFound)
    }

    @Test
    fun `Verify that the recent search method retrieves an exception`() {
        Mockito.`when`(recentSearchRepository.getLocalRecentSearches())
            .thenReturn(Single.error(Throwable()))

        viewModel.getRecentSearches()
        Assert.assertTrue(viewModel.recentSearchActions.value is RecentSearchViewModel.RecentSearchActions.OnError)
    }

    @Test
    fun `insert a new value and return the number of records`() {
        val data = RecentSearches(
            search = "Mac PRO"
        )
        Mockito.`when`(recentSearchRepository.addRecentSearches(data)).thenReturn(1)
        Assert.assertTrue(recentSearchRepository.addRecentSearches(data) > 0)
    }
}