package com.dmide.revolutassignment.common

import androidx.lifecycle.ViewModel
import com.dmide.revolutassignment.inject.component.AppComponent
import com.dmide.revolutassignment.ui.CurrenciesViewModel

abstract class BaseViewModel() : ViewModel() {

    fun inject(component: AppComponent) {
        when (this) {
            is CurrenciesViewModel -> component.inject(this)
        }
        onCreated()
    }

    abstract fun onCreated()
}