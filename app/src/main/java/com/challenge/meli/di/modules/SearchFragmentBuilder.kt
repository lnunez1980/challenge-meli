package com.challenge.meli.di.modules

import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.navArgs
import com.challenge.meli.data.api.SearchApi
import com.challenge.meli.di.scope.FragmentScope
import com.challenge.meli.di.scope.ViewModelKey
import com.challenge.meli.ui.fragments.SearchFragment
import com.challenge.meli.ui.fragments.SearchFragmentArgs
import com.challenge.meli.ui.fragments.repositories.SearchRepository
import com.challenge.meli.ui.fragments.viewModels.SearchViewModel
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import retrofit2.Retrofit

@Module
abstract class SearchFragmentBuilder {

    @FragmentScope
    @ContributesAndroidInjector(
        modules = [
            SearchProviderModule::class
        ]
    )
    abstract fun bindSearchFragment(): SearchFragment

}

@Module
object SearchProviderModule {

    @Provides
    fun provideSearchApi(retrofit: Retrofit): SearchApi {
        return retrofit.create(SearchApi::class.java)
    }

    @IntoMap
    @Provides
    @ViewModelKey(SearchViewModel::class)
    fun provideViewModel(
        fragment: SearchFragment,
        searchRepository: SearchRepository
    ): ViewModel {
        val args: SearchFragmentArgs by fragment.navArgs()
        return SearchViewModel(args.query, searchRepository)
    }
}