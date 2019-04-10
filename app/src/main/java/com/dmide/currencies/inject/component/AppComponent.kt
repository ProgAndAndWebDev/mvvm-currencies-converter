package com.dmide.currencies.inject.component

import com.dmide.currencies.inject.module.AppModule
import com.dmide.currencies.inject.module.NetworkModule
import com.dmide.currencies.ui.CurrenciesActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class])
interface AppComponent {

    fun inject(currenciesActivity: CurrenciesActivity)

    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        fun appModule(appModule: AppModule): Builder

        fun networkModule(networkModule: NetworkModule): Builder
    }
}