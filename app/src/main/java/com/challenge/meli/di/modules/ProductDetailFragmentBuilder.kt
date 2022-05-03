package com.challenge.meli.di.modules

import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.navArgs
import com.challenge.meli.di.scope.FragmentScope
import com.challenge.meli.di.scope.ViewModelKey
import com.challenge.meli.ui.fragments.ProductDetailFragment
import com.challenge.meli.ui.fragments.ProductDetailFragmentArgs
import com.challenge.meli.ui.fragments.viewModels.ProductDetailViewModel
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class ProductDetailFragmentBuilder {

    @FragmentScope
    @ContributesAndroidInjector(
        modules = [
            ProductDetailFragmentProviderModule::class
        ]
    )
    abstract fun bindProductDetailFragment(): ProductDetailFragment

}

@Module
object ProductDetailFragmentProviderModule {

    @IntoMap
    @Provides
    @ViewModelKey(ProductDetailViewModel::class)
    fun provideViewModel(
        fragment: ProductDetailFragment
    ): ViewModel {
        val args: ProductDetailFragmentArgs by fragment.navArgs()
        return ProductDetailViewModel(args.product)
    }
}