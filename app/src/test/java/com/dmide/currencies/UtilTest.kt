package com.dmide.currencies

import com.dmide.currencies.util.numberOfDecimals
import com.dmide.currencies.util.toShortString
import org.junit.Test

class UtilTest {

    @Test
    fun floatToShortString() {
        assert(1.0f.toShortString() == "1")
        assert(1.00f.toShortString() == "1")
        assert(1.40f.toShortString() == "1.4")
        assert(1.42f.toShortString() == "1.42")
        assert(0f.toShortString() == "0")
        assert((-1f).toShortString() == "-1")

        assert(1.42f.toShortString(1) == "1.4")
        assert(1.42f.toShortString(2) == "1.42")
        assert(1.42f.toShortString(3) == "1.420")
        assert(1.42f.toShortString(0) == "1")
        assert(1.42f.toShortString(null) == "1.42")
        assert(1.42f.toShortString(-1) == "1.42")
    }

    @Test
    fun numberOfDecimals() {
        assert("1.00".numberOfDecimals() == 2)
        assert("1.0".numberOfDecimals() == 1)
        assert("1.".numberOfDecimals() == 0)
        assert("1".numberOfDecimals() == 0)
        assert("-1".numberOfDecimals() == 0)
        assert("-1.1".numberOfDecimals() == 1)
        assert("".numberOfDecimals() == 0)
        assert(null.numberOfDecimals() == null)
    }
}