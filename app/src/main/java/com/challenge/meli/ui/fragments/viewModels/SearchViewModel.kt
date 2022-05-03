package com.challenge.meli.ui.fragments.viewModels

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.challenge.meli.data.database.models.RecentSearches
import com.challenge.meli.data.models.Product
import com.challenge.meli.ui.fragments.repositories.SearchRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SearchViewModel constructor(
    val querySearch: String,
    private val searchRepository: SearchRepository
) : ViewModel(), LifecycleObserver {

    private val _searchActions = MutableLiveData<SearchActions>()
    val searchActions: LiveData<SearchActions> = _searchActions

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        getSearch()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        compositeDisposable.clear()
    }

    /**
     * method get products of api
     * @param search search word
     */
    fun getSearch(search: String? = null) {
        addRecentSearch(search ?: querySearch)
        searchRepository.searchBy(search ?: querySearch)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { setLoader(true) }
            .subscribe({ products ->
                setLoader(false)
                _searchActions.value =
                    SearchActions.ProductSearchFound(products)
            }, { _searchActions.value = SearchActions.OnError(it) })
            .let(compositeDisposable::add)
    }

    /**
     * method add recent searches to local data base
     * @param search search word
     */
    private fun addRecentSearch(search: String) {
        Observable
            .fromCallable {
                Runnable { searchRepository.addRecentSearches(RecentSearches(search = search)) }.run()
            }
            .subscribeOn(Schedulers.io())
            .subscribe()
            .let(compositeDisposable::add)
    }

    private fun setLoader(loading: Boolean) {
        _searchActions.value = SearchActions.SearchLoading(loading)
    }

    /**
     * method get recent searches
     */
    sealed class SearchActions {
        class SearchLoading(val loading: Boolean) : SearchActions()
        class ProductSearchFound(val products: List<Product>) : SearchActions()
        class OnError(val error: Throwable) : SearchActions()
    }

}