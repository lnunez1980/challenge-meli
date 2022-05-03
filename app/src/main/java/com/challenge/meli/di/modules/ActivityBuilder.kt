package com.challenge.meli.di.modules

import com.challenge.meli.di.scope.ActivityScope
import com.challenge.meli.ui.activities.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(
        modules = [
            RecentSearchFragmentBuilder::class,
            SearchFragmentBuilder::class,
            ProductDetailFragmentBuilder::class
        ]
    )

    @ActivityScope
    abstract fun bindMainActivity(): MainActivity
}