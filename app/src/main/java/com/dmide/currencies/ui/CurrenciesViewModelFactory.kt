package com.dmide.currencies.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dmide.currencies.model.CurrencyRepository
import javax.inject.Inject

class CurrenciesViewModelFactory @Inject constructor(val repository: CurrencyRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CurrenciesViewModel::class.java)) {
            CurrenciesViewModel(repository) as T
        } else {
            throw java.lang.IllegalArgumentException("ViewModel Not Found")
        }
    }
}