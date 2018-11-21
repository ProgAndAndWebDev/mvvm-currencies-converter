package com.dmide.revolutassignment.common

fun Float.toShortString() : String {
    return if (this == toInt().toFloat()) { // has no decimal part
        toInt().toString()
    } else {
        toString()
    }
}