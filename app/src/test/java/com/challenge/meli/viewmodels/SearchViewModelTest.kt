package com.challenge.meli.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.challenge.meli.data.database.models.RecentSearches
import com.challenge.meli.data.models.Product
import com.challenge.meli.data.models.Shipping
import com.challenge.meli.ui.fragments.repositories.SearchRepository
import com.challenge.meli.ui.fragments.viewModels.SearchViewModel
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
class SearchViewModelTest {

    @Mock
    lateinit var searchActions: Observer<SearchViewModel.SearchActions>

    @Mock
    lateinit var searchRepository: SearchRepository

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: SearchViewModel

    private val query = "PS5"
    private val exception = Exception("Mock data error")
    private val product = Product(
        id = "MLA929389582",
        title = "Nokia 24 M 64 Gb Carbón 3 Gb Ram",
        price = 20999.0,
        address = null,
        thumbnail = "http://http2.mlstatic.com/D_715512-MLA46711394228_072021-I.jpg",
        condition = "new",
        domainId = "domainId",
        shipping = Shipping(freeShipping = false, storePickUp = true),
        attributes = emptyList(),
        soldQuantity = 1000,
        availableQuantity = 300,
        sellerReputation = null
    )

    @Before
    fun setUp() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        viewModel = SearchViewModel(query, searchRepository)

        viewModel.searchActions.observeForever(searchActions)
    }

    @Test
    fun `Verify that the search by query method retrieves data`() {
        Mockito.`when`(searchRepository.searchBy(query))
            .thenReturn(Single.just(listOf(product)))

        viewModel.getSearch(query)
        Assert.assertTrue(viewModel.searchActions.value is SearchViewModel.SearchActions.ProductSearchFound)
    }

    @Test
    fun `Verify that the search by query method retrieves an exception`() {
        Mockito.`when`(searchRepository.searchBy(query))
            .thenReturn(Single.error(exception))

        viewModel.getSearch(query)
        Assert.assertTrue(viewModel.searchActions.value is SearchViewModel.SearchActions.OnError)
    }

    @Test
    fun `insert a new value and return the number of records`() {
        val data = RecentSearches(
            search = "Mac PRO"
        )
        Mockito.`when`(searchRepository.addRecentSearches(data)).thenReturn(1)
        Assert.assertTrue(searchRepository.addRecentSearches(data) > 0)
    }
}