package com.james602152002.floatinglabeledittext;

import android.graphics.Canvas;
import android.graphics.Color;
import android.test.AndroidTestCase;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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

    @Test
    public void testStartAnimator() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = FloatingLabelEditText.class.getDeclaredMethod("startAnimator", float.class, float.class);
        method.setAccessible(true);
        method.invoke(customView, 0, 1);
    }

    @Test
    public void testOnFoucusChangeListener() {
        View.OnFocusChangeListener listener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        };
        customView.setOnFocusChangeListener(listener);
        assertEquals(listener, customView.getOnFocusChangeListener());
    }

    @Test
    public void testDispatchDraw() throws NoSuchFieldException, IllegalAccessException {
        final Canvas canvas = new Canvas();
        customView.dispatchDraw(canvas);
        customView.setText("text");
        customView.setLabel("label");
        customView.dispatchDraw(canvas);
        Field field = FloatingLabelEditText.class.getDeclaredField("hasFocus");
        field.setAccessible(true);
        field.set(customView, true);
        customView.dispatchDraw(canvas);
        customView.setError("error.............................................................");
        customView.dispatchDraw(canvas);
        customView.setErrorMargin(10, 0);
        customView.dispatchDraw(canvas);
    }

    @Test
    public void testFloatingLabelPercentage() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        final float ratio = .3f;
        Method method = FloatingLabelEditText.class.getDeclaredMethod("setError_percentage", float.class);
        method.setAccessible(true);
        method.invoke(customView, ratio);
        Field field = FloatingLabelEditText.class.getDeclaredField("error_percentage");
        field.setAccessible(true);
        assertEquals(field.get(customView), ratio);
    }

    @Test
    public void testFloatingLabelErrorPercentage() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        final float ratio = .3f;
        Method method = FloatingLabelEditText.class.getDeclaredMethod("setFloat_label_anim_percentage", float.class);
        method.setAccessible(true);
        method.invoke(customView, ratio);
        Field field = FloatingLabelEditText.class.getDeclaredField("float_label_anim_percentage");
        field.setAccessible(true);
        assertEquals(field.get(customView), ratio);
    }


    @Test
    public void testLabelTextSize() {
        final float text_size = 20;
        customView.setLabel_text_size(text_size);
        assertEquals(customView.getLabel_text_size(), text_size);
    }

    @Test
    public void testErrorTestSize() {
        final float error_text_size = 10;
        customView.setError_text_size(error_text_size);
        assertEquals(customView.getError_text_size(), error_text_size);
    }

    @Test
    public void testMargins() {
        customView.setLabelMargins(10, 10);
        customView.setErrorMargin(10, 10);
    }

    @Test
    public void testThickness() {
        final int thickness = 5;
        customView.setThickness(thickness);
        assertEquals(thickness, customView.getThickness());
    }

    @Test
    public void testDividerVerticalMargin() {
        final int vertical_margin = 2;
        customView.setDivider_vertical_margin(vertical_margin);
        assertEquals(customView.getDivider_vertical_margin(), vertical_margin);
    }

    @Test
    public void testLabel() {
        final String label = "label";
        customView.setLabel(label);
        assertEquals(customView.getLabel(), label);
    }

    @Test
    public void testAnimDuration() {
        final int duration = -1;
        customView.setAnimDuration(0);
        customView.setAnimDuration(duration);
        assertEquals(800, customView.getAnimDuration());
    }

    @Test
    public void testErrorAnimDuration() {
        final int duration = -1;
        customView.setErrorAnimDuration(0);
        customView.setErrorAnimDuration(duration);
        assertEquals(8000, customView.getErrorAnimDuration());
    }

    @Test
    public void testHighLightColor() {
        final int color = Color.RED;
        customView.setHighlightColor(color);
        assertEquals(color, customView.getHighlightColor());
    }

    @Test
    public void testHintTextColor() {
        final int color = Color.BLUE;
        customView.setHint_text_color(color);
        assertEquals(customView.getHint_text_color(), color);
    }

    @Test
    public void testErrorColor() {
        final int color = Color.YELLOW;
        customView.setError_color(color);
        assertEquals(customView.getError_color(), color);
    }

    @Test
    public void testError() {
        customView.setError(null);
        final String error = "long error..............................................................";
        customView.setError(error);
        assertEquals(customView.getError(), error);
        customView.setError(null);
    }

    @Test
    public void testDividerColor() {
        final int color = Color.RED;
        customView.setDivider_color(color);
        assertEquals(customView.getDivider_color(), color);
    }

    @Test
    public void testOnFocusChangeListener() {
        View.OnFocusChangeListener listener = customView.getOnFocusChangeListener();
        final View view = new View(getContext());
        listener.onFocusChange(view, true);
        customView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        listener.onFocusChange(view, false);
    }

    @After
    public void tearDown() throws Exception {
        customView = null;
    }

}