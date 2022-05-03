package com.challenge.meli.ui.fragments.viewModels

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.challenge.meli.data.database.models.RecentSearches
import com.challenge.meli.ui.fragments.repositories.RecentSearchRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RecentSearchViewModel constructor(
    private val recentSearchRepository: RecentSearchRepository
) : ViewModel(), LifecycleObserver {


    private val _recentSearchActions = MutableLiveData<RecentSearchActions>()
    val recentSearchActions: LiveData<RecentSearchActions> = _recentSearchActions

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        getRecentSearches()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        compositeDisposable.clear()
    }

    fun addRecentSearch(search: String) {
        Observable
            .fromCallable {
                Runnable { recentSearchRepository.addRecentSearches(RecentSearches(search = search)) }.run()
            }
            .subscribeOn(Schedulers.io())
            .subscribe()
            .let(compositeDisposable::add)
    }

    fun getRecentSearches() {
        recentSearchRepository.getLocalRecentSearches()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { setLoader(true) }
            .subscribe({ recentSearches ->
                setLoader(false)
                _recentSearchActions.value =
                    RecentSearchActions.RecentSearchFound(recentSearches)
            }, {
                _recentSearchActions.value =
                    RecentSearchActions.OnError(it)
            })
            .let(compositeDisposable::add)
    }

    fun filterRecentSearches(filter: String) {
        recentSearchRepository.filterRecentSearches(filter)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ recentSearches ->
                _recentSearchActions.value =
                    RecentSearchActions.RecentSearchFound(recentSearches)
            }, {
                _recentSearchActions.value =
                    RecentSearchActions.OnError(it)
            })
            .let(compositeDisposable::add)
    }

    private fun setLoader(loading: Boolean) {
        _recentSearchActions.value =
            RecentSearchActions.RecentSearchLoading(loading)
    }

    sealed class RecentSearchActions {
        class RecentSearchLoading(val loading: Boolean) : RecentSearchActions()
        class RecentSearchFound(val recentSearches: List<RecentSearches>) : RecentSearchActions()
        class OnError(val error: Throwable) : RecentSearchActions()
    }
}