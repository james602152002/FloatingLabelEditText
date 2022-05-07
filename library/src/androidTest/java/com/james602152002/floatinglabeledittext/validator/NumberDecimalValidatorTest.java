package com.james602152002.floatinglabeledittext.validator;

import androidx.test.filters.SmallTest;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class NumberDecimalValidatorTest {

    private NumberDecimalValidator regexValidator;

    @Before
    public void setUp() throws Exception {
        regexValidator = new NumberDecimalValidator("msg");
    }

    @Test
    public void testErrorMsg() {
        String error_msg = regexValidator.getError_message("abc");
        Assert.assertEquals(error_msg, "msg");
        error_msg = regexValidator.getError_message("111");
        Assert.assertNull(error_msg);
    }

    @After
    public void tearDown() throws Exception {
        regexValidator = null;
    }
}
