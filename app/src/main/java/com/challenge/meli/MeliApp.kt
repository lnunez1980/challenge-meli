package com.challenge.meli

import android.app.Application
import com.challenge.meli.di.component.AppComponent
import com.challenge.meli.di.component.DaggerAppComponent
import com.challenge.meli.di.scope.ComponentHolder
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

open class MeLiApp : Application(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    private var _component: AppComponent? = null
    private val component: AppComponent
        get() = _component!!

    override fun onCreate() {
        super.onCreate()
        initializeInjector()
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }

    override fun onTerminate() {
        super.onTerminate()
        _component = null
    }

    open fun initializeInjector() {
        ComponentHolder.components.clear()
        _component = DaggerAppComponent.builder()
            .application(this)
            .url(getString(R.string.meli_url))
            .build()
            .apply { inject(this@MeLiApp) }
        ComponentHolder.components.add(component)
    }
}