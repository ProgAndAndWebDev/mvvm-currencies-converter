package com.dmide.revolutassignment.ui

import android.util.Log
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dmide.revolutassignment.R
import com.dmide.revolutassignment.common.BaseViewModel
import com.dmide.revolutassignment.model.BASE_CURRENCY
import com.dmide.revolutassignment.model.Currency
import com.dmide.revolutassignment.model.CurrencyRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class CurrenciesViewModel : BaseViewModel() {
    @Inject
    lateinit var repository: CurrencyRepository

    val loadingVisibilityLiveData: LiveData<Int> = MutableLiveData()
    val errorMessageLiveData: LiveData<ErrorMessage> = MutableLiveData()
    val currenciesLiveData: CurrenciesLiveData = CurrenciesLiveData()
    val scrollStateLiveData: LiveData<Int> = MutableLiveData()

    var selectedCurrencyName: String = BASE_CURRENCY

    private var portfolio: Portfolio = Portfolio(BASE_CURRENCY, 100f)
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreated() {
        (loadingVisibilityLiveData as MutableLiveData).value = View.VISIBLE

        val currenciesDisposable = repository.currencyList
            .subscribe { pair ->
                val newBaseCurrencyName = pair.first
                val currencies = pair.second

                if (newBaseCurrencyName != portfolio.currencyName) {
                    // '!!' is intended. oldBaseCurrency should be found, otherwise fail fast
                    val oldBaseCurrency: Currency = currencies.find { it.name == portfolio.currencyName }!!
                    portfolio = Portfolio(newBaseCurrencyName, portfolio.amount / oldBaseCurrency.rate)
                }

                currenciesLiveData.update(currencies)
            }

        val statusDisposable = repository.status
            .subscribe { status ->
                when (status) {
                    is CurrencyRepository.Status.LoadingFinished -> {
                        (errorMessageLiveData as MutableLiveData).value = null
                        loadingVisibilityLiveData.value = View.GONE
                    }
                    is CurrencyRepository.Status.LoadingFailed -> {
                        Log.e(javaClass.name, "Error retrieving currencies", status.t)
                        (errorMessageLiveData as MutableLiveData).value = ErrorMessage(R.string.error_loading)
                    }
                }
            }

        compositeDisposable.addAll(statusDisposable, currenciesDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun onNewScrollState(scrollState: Int) {
        (scrollStateLiveData as MutableLiveData).value = scrollState
    }

    fun onCurrencySelected(currency: Currency) {
        selectedCurrencyName = currency.name
        currenciesLiveData.update()
        repository.changeBaseCurrency(currency.name)
    }

    fun onAmountUpdated(amount: Float) {
        portfolio = Portfolio(portfolio.currencyName, amount)
        val copy = currenciesLiveData.value?.map { it.copy() }
        currenciesLiveData.update(copy)
    }

    data class ErrorMessage(@StringRes val text: Int)

    data class Portfolio(val currencyName: String, val amount: Float)

    inner class CurrenciesLiveData : MutableLiveData<List<Currency>>() {

        fun update(currencies: List<Currency>? = value) {
            // sort here is a bit hacky but simple: we take empty string as the lowest possible value
            // when sorting by name to promote the selected currency to the top
            value = currencies
                ?.sortedBy { if (it.name == selectedCurrencyName) "" else it.name }
                ?.apply { forEach { it.value = it.rate * portfolio.amount } } // update values
        }
    }

}