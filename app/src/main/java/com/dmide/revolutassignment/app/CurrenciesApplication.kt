package com.dmide.revolutassignment.app

import android.app.Application
import android.util.Log
import com.dmide.revolutassignment.inject.component.AppComponent
import com.dmide.revolutassignment.inject.component.DaggerAppComponent
import com.dmide.revolutassignment.inject.module.AppModule
import com.dmide.revolutassignment.inject.module.NetworkModule
import io.reactivex.plugins.RxJavaPlugins

class CurrenciesApplication : Application() {

    val component: AppComponent by lazy {
        DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .networkModule(NetworkModule)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        // see https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0#error-handling
        RxJavaPlugins.setErrorHandler { t -> Log.e(javaClass.name, "", t) }
    }
}