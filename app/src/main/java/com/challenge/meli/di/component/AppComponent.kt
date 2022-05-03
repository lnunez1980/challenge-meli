package com.challenge.meli.di.component

import android.app.Application
import com.challenge.meli.MeLiApp
import com.challenge.meli.di.modules.ActivityBuilder
import com.challenge.meli.di.modules.DatabaseModule
import com.challenge.meli.di.modules.FactoryModule
import com.challenge.meli.di.modules.NetworkModule
import com.challenge.meli.ui.activities.MainActivity
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Qualifier
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        FactoryModule::class,
        NetworkModule::class,
        DatabaseModule::class,
        ActivityBuilder::class,
        AndroidSupportInjectionModule::class
    ]
)

interface AppComponent : AndroidInjector<MeLiApp> {

    fun inject(app: MainActivity)

    @WebServiceUrl
    fun getWebServiceUrl(): String

    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun url(@WebServiceUrl storeType: String): Builder

        @BindsInstance
        fun application(application: Application): Builder
    }
}

@Qualifier
annotation class WebServiceUrl
