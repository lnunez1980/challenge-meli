package com.challenge.meli.di.modules

import androidx.lifecycle.ViewModelProvider
import com.challenge.meli.factories.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class FactoryModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}