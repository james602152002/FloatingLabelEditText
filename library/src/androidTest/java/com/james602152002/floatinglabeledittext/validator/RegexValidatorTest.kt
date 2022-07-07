package com.james602152002.floatinglabeledittext.validator

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Created by shiki60215 on 18-1-19.
 */
@RunWith(AndroidJUnit4::class)
@SmallTest
class RegexValidatorTest {
    private var regexValidator: RegexValidator? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        regexValidator = RegexValidator("msg", "\\d+")
        regexValidator = RegexValidator("msg", "\\d+", true)
    }

    @Test
    fun testErrorMsg() {
        var errorMsg = regexValidator?.getError_message("111")
        Assert.assertEquals(errorMsg, "msg")
        errorMsg = regexValidator?.getError_message("abc")
        Assert.assertNotNull(errorMsg)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        regexValidator = null
    }
}