package com.james602152002.floatinglabeledittext;

import android.test.AndroidTestCase;

import org.junit.After;
import org.junit.Before;

/**
 * Created by shiki60215 on 18-1-17.
 */
public class FloatingLabelEditTextTest extends AndroidTestCase {

    private FloatingLabelEditText customView;

    @Before
    public void setUp() throws Exception {
        customView = new FloatingLabelEditText(getContext());
    }

    @After
    public void tearDown() throws Exception {
        customView = null;
    }

}