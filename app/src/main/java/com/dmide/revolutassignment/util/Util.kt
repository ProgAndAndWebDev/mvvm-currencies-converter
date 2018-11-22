package com.dmide.revolutassignment.util

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.BiFunction


fun Float.toShortString(maxDecimals: Int? = null): String {
    return if (this == toInt().toFloat()) { // has no decimal part
        toInt().toString()
    } else {
        if (maxDecimals != null && maxDecimals >= 0) {
            "%.${maxDecimals}f".format(this)
        } else {
            toString()
        }
    }
}

fun String?.numberOfDecimals(): Int? {
    if (this == null) return null
    return if (contains(".")) {
        split(".")[1].length
    } else {
        0
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

fun Context.showKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

fun <T> MutableLiveData<T>.setIfDiffers(newValue: T) {
    if (value != newValue) value = newValue
}