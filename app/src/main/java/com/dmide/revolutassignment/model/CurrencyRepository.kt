package com.dmide.revolutassignment.model

import android.app.Activity
import com.dmide.revolutassignment.app.ActivityLifecycleCallbacksAdapter
import com.dmide.revolutassignment.app.CurrenciesApplication
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepository @Inject constructor(application: CurrenciesApplication, private val currencyApi: CurrencyApi) {

    val currencyList: PublishSubject<Pair<String, List<Currency>>> = PublishSubject.create()
    val status: BehaviorSubject<Status> = BehaviorSubject.create()

    private var baseCurrencyName: String = BASE_CURRENCY
    private var disposable: Disposable? = null

    init {
        application.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacksAdapter() {
            override fun onActivityResumed(p0: Activity?) {
                disposable = createCurrenciesObservable()
                    .subscribe {
                        currencyList.onNext(Pair(baseCurrencyName, it))
                    }
            }

            override fun onActivityPaused(p0: Activity?) {
                disposable?.dispose()
            }
        })
    }

    fun changeBaseCurrency(baseCurrencyName: String) {
        this.baseCurrencyName = baseCurrencyName
    }

    private fun createCurrenciesObservable(): Observable<List<Currency>> {
        return Observable.interval(1, TimeUnit.SECONDS, Schedulers.io())
            .startWith(0)
            .flatMap { currencyApi.getLatest(baseCurrencyName) }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { e -> status.onNext(Status.LoadingFailed(e)) }
            .retry()
            .doOnSubscribe { status.onNext(Status.LoadingStarted) }
            .doOnNext { status.onNext(Status.LoadingFinished) }
            .map { response ->
                response.rates
                    .map { Currency(it.key, it.value, baseCurrencyName) }
                    .toMutableList()
                    .apply { add(0, Currency(baseCurrencyName, 1f, baseCurrencyName)) }
            }
    }

    sealed class Status {
        object LoadingStarted : Status()
        object LoadingFinished : Status()
        data class LoadingFailed(val t: Throwable) : Status()
    }
}