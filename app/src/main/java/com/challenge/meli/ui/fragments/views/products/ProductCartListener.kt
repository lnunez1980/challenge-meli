package com.challenge.meli.ui.fragments.views.products

import com.challenge.meli.data.models.Product

interface ProductCartListener {
    fun onProductClicked(product: Product)
}