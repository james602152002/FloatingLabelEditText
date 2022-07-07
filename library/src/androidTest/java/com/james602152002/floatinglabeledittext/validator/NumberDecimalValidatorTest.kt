package com.james602152002.floatinglabeledittext.validator

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class NumberDecimalValidatorTest {

    private var regexValidator: NumberDecimalValidator? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        regexValidator = NumberDecimalValidator("msg")
    }

    @Test
    fun testErrorMsg() {
        var errorMsg = regexValidator?.getError_message("abc")
        Assert.assertEquals(errorMsg, "msg")
        errorMsg = regexValidator?.getError_message("111")
        Assert.assertNull(errorMsg)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        regexValidator = null
    }
}