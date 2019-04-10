package com.dmide.currencies.model

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_CURRENCY = "EUR"

interface CurrencyApi {

    @GET("/latest")
    fun getLatest(@Query("base") base: String = BASE_CURRENCY): Observable<LatestCurrenciesResponse>

}

data class LatestCurrenciesResponse(
    val base: String,
    val rates: Map<String, Float>
)