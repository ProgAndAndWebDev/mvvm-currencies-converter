package com.dmide.revolutassignment.inject.component

import com.dmide.revolutassignment.ui.CurrenciesViewModel
import com.dmide.revolutassignment.ui.CurrencyViewHolder
import com.dmide.revolutassignment.inject.module.AppModule
import com.dmide.revolutassignment.inject.module.NetworkModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class])
interface AppComponent {

    fun inject(currenciesViewModel: CurrenciesViewModel)

    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        fun appModule(appModule: AppModule): Builder

        fun networkModule(networkModule: NetworkModule): Builder
    }
}