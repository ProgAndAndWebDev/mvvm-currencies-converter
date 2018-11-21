package com.dmide.revolutassignment.app

import android.app.Application
import com.dmide.revolutassignment.inject.component.AppComponent
import com.dmide.revolutassignment.inject.component.DaggerAppComponent
import com.dmide.revolutassignment.inject.module.AppModule
import com.dmide.revolutassignment.inject.module.NetworkModule

class CurrenciesApplication : Application() {

    val component: AppComponent by lazy {
        DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .networkModule(NetworkModule)
                .build()
    }

}