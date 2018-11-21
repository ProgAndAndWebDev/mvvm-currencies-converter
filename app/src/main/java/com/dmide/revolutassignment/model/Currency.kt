package com.dmide.revolutassignment.model

data class Currency(val name: String, val rate: Float, val baseCurrencyName: String, var value: Float = rate)