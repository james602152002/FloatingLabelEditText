package com.james602152002.floatinglabeledittext;

import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.VisibleForTesting;
import android.test.AndroidTestCase;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.james602152002.floatinglabeledittext.validator.RegexValidator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shiki60215 on 18-1-17.
 */
public class FloatingLabelEditTextTest extends AndroidTestCase {

    private FloatingLabelEditText customView;

    @Before
    public void setUp() throws Exception {
        customView = new FloatingLabelEditText(getContext());
        customView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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
        customView.onDraw(canvas);
        customView.setText("text");
        customView.setLabel("label");
        customView.onDraw(canvas);
        Field field = FloatingLabelEditText.class.getDeclaredField("hasFocus");
        field.setAccessible(true);
        field.set(customView, true);
        customView.onDraw(canvas);
        customView.setError("error.............................................................");
        customView.onDraw(canvas);
        customView.setErrorMargin(10);
        customView.onDraw(canvas);
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
        customView.setErrorMargin(10);
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

    @VisibleForTesting
    public void testValidatorList() {
        customView.setValidatorList(null);
        assertNull(customView.getValidatorList());
        final List<RegexValidator> data = new ArrayList<>();
        data.add(null);
        customView.setValidatorList(data);
        customView.setText("1");
        data.clear();
        data.add(new RegexValidator("error", "\\d+"));
        customView.setValidatorList(data);
        assertEquals(customView.getValidatorList(), data);
        customView.setText("123");
    }

    @VisibleForTesting
    public void testValidatorListOnNull() {
        final List<RegexValidator> data = new ArrayList<>();
        data.add(new RegexValidator("error", "\\d+"));
        customView.setValidatorList(data);
        customView.setText("abc");
    }

    @Test
    public void testAddValidator() {
        customView.addValidator(null);
        RegexValidator regexValidator = new RegexValidator("msg", "regex");
        customView.addValidator(regexValidator);
        assertEquals(customView.getValidatorList().get(0), regexValidator);
    }

    @Test
    public void testErrorDisabled() {
        customView.setError_disabled();
        assertTrue(customView.isError_disabled());
    }

    @Test
    public void testErrorEnabled() {
        customView.setError_enabled();
        assertFalse(customView.isError_disabled());
    }

    @Test
    public void testEnableClearBtn() throws NoSuchFieldException, IllegalAccessException {
        Field field = FloatingLabelEditText.class.getDeclaredField("clearButtonPaint");
        field.setAccessible(true);
        customView.enableClearBtn(true);
        assertNotNull(field.get(customView));
        customView.onDraw(new Canvas());
        customView.enableClearBtn(true);
        customView.enableClearBtn(false);
        assertNull(field.get(customView));
    }

    @Test
    public void testSingleLine() throws IllegalAccessException, NoSuchFieldException {
        customView.setSingleLine();
        customView.setMultiline_mode(true);
        customView.setSingleLine();
        Field field = FloatingLabelEditText.class.getDeclaredField("multiline_mode");
        field.setAccessible(true);
        assertTrue((boolean) field.get(customView));
        customView.setMultiline_mode(false);
    }

    @Test
    public void testClearBtnHorizontalMargin() {
        final int margin = 20;
        customView.setClear_btn_horizontal_margin(margin);
        assertEquals(margin, customView.getClear_btn_horizontal_margin());
    }

    @Test
    public void testTouchEventOnClearBtnMode() {
        final int metaState = 0;
        final int x = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int y = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        customView.measure(x, y);
        final int point_x = (int) (customView.getMeasuredWidth() * .99f);
        final int point_y = customView.getMeasuredHeight() >> 1;
        MotionEvent motionEvent = MotionEvent.obtain(100, 100, MotionEvent.ACTION_DOWN,
                point_x, point_y, metaState);
        customView.onTouchEvent(motionEvent);
        customView.enableClearBtn(true);
        customView.onTouchEvent(motionEvent);
        motionEvent = MotionEvent.obtain(100, 100, MotionEvent.ACTION_MOVE,
                point_x, point_y, metaState);
        customView.onTouchEvent(motionEvent);
        motionEvent = MotionEvent.obtain(100, 100, MotionEvent.ACTION_UP,
                point_x, point_y, metaState);
        customView.onTouchEvent(motionEvent);
        motionEvent = MotionEvent.obtain(100, 100, MotionEvent.ACTION_CANCEL,
                point_x, point_y, metaState);
        customView.onTouchEvent(motionEvent);
    }

    @Test
    public void testTouchEventOnCancelClearBtnMode() {
        customView.enableClearBtn(true);
        final int metaState = 0;
        final int x = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int y = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        customView.measure(x, y);
        final int point_x = (int) (customView.getMeasuredWidth() * .99f);
        final int point_y = customView.getMeasuredHeight() >> 1;
        MotionEvent motionEvent = MotionEvent.obtain(100, 100, MotionEvent.ACTION_DOWN,
                point_x, point_y, metaState);
        customView.onTouchEvent(motionEvent);
        motionEvent = MotionEvent.obtain(100, 100, MotionEvent.ACTION_MOVE,
                point_x << 10, point_y, metaState);
        customView.onTouchEvent(motionEvent);
        motionEvent = MotionEvent.obtain(100, 100, MotionEvent.ACTION_DOWN,
                point_x, point_y, metaState);
        customView.onTouchEvent(motionEvent);
        motionEvent = MotionEvent.obtain(100, 100, MotionEvent.ACTION_MOVE,
                point_x, point_x << 10, metaState);
        customView.onTouchEvent(motionEvent);
        motionEvent = MotionEvent.obtain(100, 100, MotionEvent.ACTION_MOVE,
                point_x << 10, point_x << 10, metaState);
        customView.onTouchEvent(motionEvent);
        motionEvent = MotionEvent.obtain(100, 100, MotionEvent.ACTION_UP,
                point_x, point_y, metaState);
        customView.onTouchEvent(motionEvent);
    }

    @Test
    public void testTouchEventOnNoTouchClearBtnMode() {
        customView.enableClearBtn(true);
        final int metaState = 0;
        final int x = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int y = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        customView.measure(x, y);
        final int point_x = customView.getMeasuredWidth() << 10;
        final int point_y = customView.getMeasuredHeight() << 10;
        MotionEvent motionEvent = MotionEvent.obtain(100, 100, MotionEvent.ACTION_DOWN,
                point_x, point_y, metaState);
        customView.onTouchEvent(motionEvent);
        motionEvent = MotionEvent.obtain(100, 100, MotionEvent.ACTION_MOVE,
                0, 0, metaState);
        customView.onTouchEvent(motionEvent);
        motionEvent = MotionEvent.obtain(100, 100, MotionEvent.ACTION_MOVE,
                0, 0, metaState);
        customView.onTouchEvent(motionEvent);
        motionEvent = MotionEvent.obtain(100, 100, MotionEvent.ACTION_UP,
                0, 0, metaState);
        customView.onTouchEvent(motionEvent);
    }

    @Test
    public void testScaleRatio() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Method method = FloatingLabelEditText.class.getDeclaredMethod("setClear_paint_alpha_ratio", float.class);
        method.setAccessible(true);
        final float scale_ratio = 3.14f;
        method.invoke(customView, scale_ratio);
        Field field = FloatingLabelEditText.class.getDeclaredField("clear_paint_alpha_ratio");
        field.setAccessible(true);
        assertEquals(scale_ratio, field.get(customView));
    }

    @After
    public void tearDown() throws Exception {
        customView = null;
    }

}