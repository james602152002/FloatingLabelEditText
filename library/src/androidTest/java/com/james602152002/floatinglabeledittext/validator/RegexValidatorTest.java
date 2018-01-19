package com.james602152002.floatinglabeledittext.validator;

import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by shiki60215 on 18-1-19.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class RegexValidatorTest {

    private RegexValidator regexValidator;

    @Before
    public void setUp() throws Exception {
        regexValidator = new RegexValidator("msg", "\\d+");
    }

    @Test
    public void testErrorMsg() {
        String error_msg = regexValidator.getError_message("111");
        Assert.assertEquals(error_msg, "msg");
        error_msg = regexValidator.getError_message("abc");
        Assert.assertNull(error_msg);
    }

    @After
    public void tearDown() throws Exception {
        regexValidator = null;
    }

}