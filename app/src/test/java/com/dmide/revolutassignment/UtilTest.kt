package com.dmide.revolutassignment

import com.dmide.revolutassignment.util.toShortString
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
    }
}