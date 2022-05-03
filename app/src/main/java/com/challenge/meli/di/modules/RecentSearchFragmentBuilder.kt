package com.challenge.meli.di.modules

import androidx.lifecycle.ViewModel
import com.challenge.meli.di.scope.FragmentScope
import com.challenge.meli.di.scope.ViewModelKey
import com.challenge.meli.ui.fragments.RecentSearchFragment
import com.challenge.meli.ui.fragments.repositories.RecentSearchRepository
import com.challenge.meli.ui.fragments.viewModels.RecentSearchViewModel
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class RecentSearchFragmentBuilder {

    @FragmentScope
    @ContributesAndroidInjector(
        modules = [
            RecentSearchProviderModule::class
        ]
    )
    abstract fun bindRecentSearchFragment(): RecentSearchFragment

}

@Module
object RecentSearchProviderModule {

    @IntoMap
    @Provides
    @ViewModelKey(RecentSearchViewModel::class)
    fun provideViewModel(
        recentSearchRepository: RecentSearchRepository
    ): ViewModel {
        return RecentSearchViewModel(
            recentSearchRepository
        )
    }
}