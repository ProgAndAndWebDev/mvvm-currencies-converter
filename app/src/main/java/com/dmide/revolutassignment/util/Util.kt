package com.dmide.revolutassignment.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
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

/**
 * Sets the value to the result of a function that is called when both `LiveData`s have data
 * or when they receive updates after that.
 */
fun <T, A, B> LiveData<A>.withLatestFrom(other: LiveData<B>, onChange: (A, B) -> T): MediatorLiveData<T> {

    var source1emitted = false
    var source2emitted = false

    val result = MediatorLiveData<T>()

    val mergeF = {
        val source1Value = this.value
        val source2Value = other.value

        if (source1emitted && source2emitted) {
            result.value = onChange.invoke(source1Value!!, source2Value!! )
        }
    }

    result.addSource(this) { source1emitted = true; mergeF.invoke() }
    result.addSource(other) { source2emitted = true; mergeF.invoke() }

    return result
}