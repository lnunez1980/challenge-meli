package com.challenge.meli.ui.fragments.viewModels

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.challenge.meli.data.models.Product

class ProductDetailViewModel constructor(
    private val productArg: Product?
) : ViewModel(), LifecycleObserver {

    private val _product = MutableLiveData<Product>()
    val product: LiveData<Product> = _product

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onResume() {
        productArg?.let { _product.value = it }
    }
}