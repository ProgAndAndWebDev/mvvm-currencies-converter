package com.dmide.currencies

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dmide.currencies.model.Currency
import com.dmide.currencies.model.CurrencyRepository
import com.dmide.currencies.ui.CurrenciesViewModel
import com.dmide.currencies.ui.CurrenciesViewModelFactory
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito
import org.mockito.Mockito.verify


class CurrenciesTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun baseCurrencyUpdatePropagation() {
        val repository = Mockito.mock(CurrencyRepository::class.java)
        Mockito.`when`(repository.currency).thenReturn(PublishSubject.create())
        Mockito.`when`(repository.status).thenReturn(BehaviorSubject.create())

        val currenciesViewModelFactory = CurrenciesViewModelFactory(repository)
        val viewModel = currenciesViewModelFactory.create(CurrenciesViewModel::class.java)

        val testCurrencyName = "TEST"
        val currency = Currency(testCurrencyName, 1f, testCurrencyName)
        viewModel.onCurrencySelected(currency)

        verify (repository).changeBaseCurrency(testCurrencyName)
    }

}
