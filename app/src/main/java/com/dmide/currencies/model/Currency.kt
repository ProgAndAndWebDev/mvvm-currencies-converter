package com.dmide.currencies.model

data class Currency(val name: String, val rate: Float, val baseCurrencyName: String, var value: Float = rate)