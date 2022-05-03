package com.challenge.meli.ui.fragments.controllers

import com.airbnb.epoxy.AsyncEpoxyController
import com.challenge.meli.data.database.models.RecentSearches
import com.challenge.meli.ui.fragments.views.search.RecentSearchItemViewModel_
import com.challenge.meli.ui.fragments.views.search.RecentSearchListener


class RecentSearchAdapterController constructor(
    private val listener: RecentSearchListener
) : AsyncEpoxyController() {

    private var data: List<RecentSearches> = listOf()
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        if (data.isNotEmpty()){
            data.forEach {
                RecentSearchItemViewModel_()
                    .id(it.id)
                    .textItem(it.search)
                    .listener(listener)
                    .addTo(this)
            }
        }
    }

    fun dispatch(data: List<RecentSearches>) {
        this.data = data
    }
}