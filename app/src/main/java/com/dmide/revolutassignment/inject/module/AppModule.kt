package com.dmide.revolutassignment.inject.module

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.preference.PreferenceManager
import com.dmide.revolutassignment.common.CurrenciesApplication
import dagger.Module
import dagger.Provides

@Module
@Suppress("unused")
class AppModule(val application: CurrenciesApplication) {

    @Provides
    fun provideApplication(): CurrenciesApplication {
        return application
    }

    @Provides
    fun provideAppContext(): Context {
        return application
    }

    @Provides
    fun provideSharedPreferences(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(application)
    }
}