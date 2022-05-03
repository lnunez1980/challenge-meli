package com.challenge.meli.ui.fragments.controllers

import com.airbnb.epoxy.AsyncEpoxyController
import com.challenge.meli.data.models.Product
import com.challenge.meli.ui.fragments.views.products.ProductCartListener
import com.challenge.meli.ui.fragments.views.products.ProductCartViewModel_


class SearchAdapterController constructor(
    private val listener: ProductCartListener
) : AsyncEpoxyController() {

    private var products: List<Product> = listOf()
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        if (products.isNotEmpty()){
            products.forEach { product ->
                ProductCartViewModel_()
                    .id(product.id)
                    .data(product)
                    .listener(listener)
                    .addTo(this)
            }
        }
    }

    fun dispatch(products: List<Product>) {
        this.products = products
    }

    fun clear() {
        products = emptyList()
    }
}