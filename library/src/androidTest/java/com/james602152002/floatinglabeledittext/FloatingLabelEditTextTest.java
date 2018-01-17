package com.james602152002.floatinglabeledittext;

import android.test.AndroidTestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by shiki60215 on 18-1-17.
 */
public class FloatingLabelEditTextTest extends AndroidTestCase {

    private FloatingLabelEditText customView;

    @Before
    public void setUp() throws Exception {
        customView = new FloatingLabelEditText(getContext());
    }

    @Test
    public void testInit() {
        assertNotNull(customView);
        customView = new FloatingLabelEditText(getContext(), null);
        assertNotNull(customView);
        customView = new FloatingLabelEditText(getContext(), null, 0);
        assertNotNull(customView);
    }

    @After
    public void tearDown() throws Exception {
        customView = null;
    }

}