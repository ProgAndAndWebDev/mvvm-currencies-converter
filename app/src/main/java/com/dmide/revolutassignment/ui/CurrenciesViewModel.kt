package com.dmide.revolutassignment.ui

import android.util.Log
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dmide.revolutassignment.R
import com.dmide.revolutassignment.common.BaseViewModel
import com.dmide.revolutassignment.model.Currency
import com.dmide.revolutassignment.model.CurrencyRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class CurrenciesViewModel : BaseViewModel() {
    @Inject
    lateinit var repository: CurrencyRepository

    val loadingVisibilityLiveData: LiveData<Int> = MutableLiveData()
    val errorMessageLiveData: LiveData<ErrorMessage> = MutableLiveData()
    val currenciesLiveData: LiveData<List<Currency>> = MutableLiveData()
    val scrollStateLiveData: LiveData<Int> = MutableLiveData()

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreated() {
        (loadingVisibilityLiveData as MutableLiveData).value = View.VISIBLE

        val currenciesDisposable = repository.currencyList
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { currencies ->
                    (currenciesLiveData as MutableLiveData).value = currencies
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
        
        compositeDisposable.add(statusDisposable)
        compositeDisposable.add(currenciesDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun onNewScrollState(scrollState: Int) {
        (scrollStateLiveData as MutableLiveData).value = scrollState
    }
    
    data class ErrorMessage(@StringRes val text: Int)
}