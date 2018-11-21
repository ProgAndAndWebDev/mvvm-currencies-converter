package com.dmide.revolutassignment.util

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.BiFunction

fun Float.toShortString() : String {
    return if (this == toInt().toFloat()) { // has no decimal part
        toInt().toString()
    } else {
        toString()
    }
}

fun <T1, T2, R> Observable<T1>.withLatestFrom(other: ObservableSource<T2>, combiner: (T1, T2) -> R): Observable<R>
        = withLatestFrom(other, BiFunction { a, b -> combiner.invoke(a, b) })