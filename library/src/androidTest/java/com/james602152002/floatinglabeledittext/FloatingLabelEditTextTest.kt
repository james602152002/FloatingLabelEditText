package com.james602152002.floatinglabeledittext

import android.graphics.Canvas
import android.graphics.Color
import android.text.InputFilter.AllCaps
import android.text.InputFilter.LengthFilter
import android.view.MotionEvent
import android.view.View
import android.view.View.MeasureSpec.UNSPECIFIED
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.james602152002.floatinglabeledittext.intercepter.ClearBtnInterceptor
import com.james602152002.floatinglabeledittext.validator.RegexValidator
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.reflect.InvocationTargetException

@RunWith(AndroidJUnit4::class)
@SmallTest
class FloatingLabelEditTextTest {

    private var customView: FloatingLabelEditText? = null

    private fun getContext() = InstrumentationRegistry.getInstrumentation().context

    @Before
    @Throws(Exception::class)
    fun setUp() {
        customView = FloatingLabelEditText(getContext()).apply {
            layoutParams =
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
        }
    }

    @Test
    fun testInit() {
        assertNotNull(customView)
        customView = FloatingLabelEditText(getContext(), null)
        assertNotNull(customView)
        customView = FloatingLabelEditText(getContext(), null, 0)
        assertNotNull(customView)
    }

    @Test
    @Throws(
        NoSuchMethodException::class,
        InvocationTargetException::class,
        IllegalAccessException::class
    )
    fun testStartAnimator() {
        val method = FloatingLabelEditText::class.java.getDeclaredMethod(
            "startAnimator",
            Float::class.javaPrimitiveType,
            Float::class.javaPrimitiveType
        )
        method.isAccessible = true
        method.invoke(customView, 0, 1)
    }

    @Test
    fun testOnFocusChangeListener1() {
        val listener = OnFocusChangeListener { _, _ -> }
        customView?.onFocusChangeListener = listener
        assertEquals(listener, customView?.onFocusChangeListener)
    }

    @Test
    @Throws(NoSuchFieldException::class, IllegalAccessException::class)
    fun testDispatchDraw() {
        val canvas = Canvas()
        customView?.apply {
            onDraw(canvas)
            setText("text")
            label = "label"
            onDraw(canvas)
            val field = FloatingLabelEditText::class.java.getDeclaredField("hasFocus")
            field.isAccessible = true
            field[this] = true
            onDraw(canvas)
            error = "error............................................................."
            onDraw(canvas)
            setErrorMargin(10)
            onDraw(canvas)
        }
    }

    @Test
    @Throws(
        NoSuchMethodException::class,
        InvocationTargetException::class,
        IllegalAccessException::class,
        NoSuchFieldException::class
    )
    fun testFloatingLabelPercentage() {
        val ratio = .3f
        val method =
            FloatingLabelEditText::class.java.getDeclaredMethod(
                "setError_percentage",
                Float::class.javaPrimitiveType
            )
        method.isAccessible = true
        method.invoke(customView, ratio)
        val field = FloatingLabelEditText::class.java.getDeclaredField("error_percentage")
        field.isAccessible = true
        assertEquals(field[customView], ratio)
    }

    @Test
    @Throws(
        NoSuchMethodException::class,
        InvocationTargetException::class,
        IllegalAccessException::class,
        NoSuchFieldException::class
    )
    fun testFloatingLabelErrorPercentage() {
        val ratio = .3f
        val method = FloatingLabelEditText::class.java.getDeclaredMethod(
            "setFloat_label_anim_percentage",
            Float::class.javaPrimitiveType
        )
        method.isAccessible = true
        method.invoke(customView, ratio)
        val field =
            FloatingLabelEditText::class.java.getDeclaredField("float_label_anim_percentage")
        field.isAccessible = true
        assertEquals(field[customView], ratio)
    }


    @Test
    fun testLabelTextSize() {
        val textSize = 20f
        customView?.apply {
            labelTextSize = textSize
            assertEquals(labelTextSize, textSize)
        }
    }

    @Test
    fun testErrorTestSize() {
        val errorTextSize = 10f
        customView?.apply {
            this.errorTextSize = errorTextSize
            assertEquals(this.errorTextSize, errorTextSize)
        }
    }

    @Test
    fun testMargins() {
        customView?.apply {
            setLabelMargins(10, 10)
            setErrorMargin(10)
        }
    }

    @Test
    fun testThickness() {
        val thickness = 5
        customView?.apply {
            this.thickness = thickness
            assertEquals(thickness, this.thickness)
        }
    }

    @Test
    fun testDividerVerticalMargin() {
        val verticalMargin = 2
        customView?.apply {
            dividerVerticalMargin = verticalMargin
            assertEquals(dividerVerticalMargin, verticalMargin)
        }
    }

    @Test
    fun testLabel() {
        val label = "label"
        customView?.apply {
            this.label = label
            assertEquals(this.label, label)
        }
    }

    @Test
    fun testAnimDuration() {
        val duration = -1
        customView?.apply {
            setAnimDuration(0)
            setAnimDuration(duration)
            assertEquals(800, animDuration)
        }
    }

    @Test
    fun testErrorAnimDuration() {
        val duration = -1
        customView?.apply {
            setErrorAnimDuration(0)
            setErrorAnimDuration(duration)
            assertEquals(800, errorAnimDuration)
        }
    }

    @Test
    fun testHighLightColor() {
        val color = Color.RED
        customView?.apply {
            highlightColor = color
            assertEquals(color, highlightColor)
        }
    }

    @Test
    fun testHintTextColor() {
        val color = Color.BLUE
        customView?.apply {
            fleHintTextColor = color
            assertEquals(fleHintTextColor, color)
        }
    }

    @Test
    fun testErrorColor() {
        val color = Color.YELLOW
        customView?.apply {
            errorColor = color
            assertEquals(errorColor, color)
        }
    }

    @Test
    fun testError() {
        customView?.apply {
            error = null
            val error = "long error.............................................................."
            setError(error)
            assertEquals(this.error, error)
            setError(null)
        }
    }

    @Test
    fun testDividerColor() {
        val color = Color.RED
        customView?.apply {
            dividerColor = color
            assertEquals(dividerColor, color)
        }
    }

    @Test
    fun testOnFocusChangeListener() {
        customView?.apply {
            val listener = onFocusChangeListener
            val view = View(this@FloatingLabelEditTextTest.getContext())
            listener.onFocusChange(view, true)
            onFocusChangeListener = OnFocusChangeListener { _, _ -> }
            listener.onFocusChange(view, false)
        }
    }

    @VisibleForTesting
    fun testValidatorList() {
        customView?.apply {
            setValidatorList(null)
            assertNull(getValidatorList())
            var data: MutableList<RegexValidator>? = null
            setValidatorList(data)
            setText("1")
            data = mutableListOf()
            data.add(RegexValidator("error", "\\d+"))
            setValidatorList(data)
            assertEquals(getValidatorList(), data)
            setText("123")
        }
    }

    @VisibleForTesting
    fun testValidatorListOnNull() {
        customView?.apply {
            val data: MutableList<RegexValidator> = ArrayList()
            data.add(RegexValidator("error", "\\d+"))
            setValidatorList(data)
            setText("abc")
        }
    }

    @Test
    fun testAddValidator() {
        customView?.apply {
            addValidator(null)
            val regexValidator = RegexValidator("msg", "regex")
            addValidator(regexValidator)
            assertEquals(getValidatorList()?.getOrNull(0), regexValidator)
        }
    }

    @Test
    fun testErrorDisabled() {
        customView?.apply {
            setErrorDisabled()
            assertTrue(errorDisabled)
        }
    }

    @Test
    fun testErrorEnabled() {
        customView?.apply {
            setErrorEnabled()
            assertFalse(errorDisabled)
        }
    }

    @Test
    @Throws(NoSuchFieldException::class, IllegalAccessException::class)
    fun testEnableClearBtn() {
        customView?.apply {
            val canvas = Canvas()
            var field = FloatingLabelEditText::class.java.getDeclaredField("hasFocus")
            field.isAccessible = true
            field[this] = true
            field = FloatingLabelEditText::class.java.getDeclaredField("clearButtonPaint")
            field.isAccessible = true
            enableClearBtn(true)
            assertNotNull(field[this])
            onDraw(canvas)
            setText("text")
            onDraw(canvas)
            enableClearBtn(true)
            enableClearBtn(false)
            assertNull(field[this])
        }
    }

    @Test
    @Throws(IllegalAccessException::class, NoSuchFieldException::class)
    fun testCustomizeClearBtn() {
        val field = FloatingLabelEditText::class.java.getDeclaredField("hasFocus")
        field.isAccessible = true
        field[customView] = true
        customView?.customizeClearBtn(null, null, Color.RED, 10)
    }

//    @Test
//    @Throws(IllegalAccessException::class, NoSuchFieldException::class)
//    fun testCustomizeDrawableClearBtn() {
//        val field = FloatingLabelEditText::class.java.getDeclaredField("hasFocus")
//        field.isAccessible = true
//        field[customView] = true
//        customView!!.customizeClearBtn(R.drawable.ic_launcher, 10)
//        customView!!.customizeClearBtn(R.drawable.ic_launcher, 200)
//    }
//
//    @Test
//    @Throws(NoSuchFieldException::class, IllegalAccessException::class)
//    fun testCustomizeVectorDrawableClearBtn() {
//        val canvas = Canvas()
//        customView!!.showClearButtonWithoutFocus()
//        customView!!.onDraw(canvas)
//        val field = FloatingLabelEditText::class.java.getDeclaredField("hasFocus")
//        field.isAccessible = true
//        field[customView] = true
//        customView!!.customizeClearBtn(R.drawable.ic_launcher_background, 10)
//        customView!!.setText("text")
//        customView!!.onDraw(canvas)
//    }

    @Test
    @Throws(IllegalAccessException::class, NoSuchFieldException::class)
    fun testSingleLine() {
        customView?.apply {
            setSingleLine()
            multilineMode = true
            setSingleLine()
            val field = FloatingLabelEditText::class.java.getDeclaredField("multiline_mode")
            field.isAccessible = true
            assertTrue(field[this] as Boolean)
            multilineMode = false
        }
    }

    @Test
    fun testClearBtnHorizontalMargin() {
        val margin = 20
        customView?.apply {
            clearBtnHorizontalMargin(margin)
            assertEquals(margin, clearBtnHorizontalMargin)
        }
    }

    @Test
    fun testClearBtnColor() {
        val color = Color.BLUE
        customView?.apply {
            clearBtnColor = color
            assertEquals(clearBtnColor, color)
        }
    }

    @Test
    fun testClearBtnSize() {
        val size = 100
        customView?.apply {
            clearBtnSize = size
            assertEquals(size, clearBtnSize)
        }
    }

    @Test
    @Throws(IllegalAccessException::class, NoSuchFieldException::class)
    fun testTouchEventOnClearBtnMode() {
        customView?.apply {
            val field = FloatingLabelEditText::class.java.getDeclaredField("hasFocus")
            field.isAccessible = true
            field[this] = true
            val metaState = 0
            val x = View.MeasureSpec.makeMeasureSpec(0, UNSPECIFIED)
            val y = View.MeasureSpec.makeMeasureSpec(0, UNSPECIFIED)
            measure(x, y)
            val pointX = (measuredWidth * .99f).toInt()
            val pointY = measuredHeight shr 1
            var motionEvent = MotionEvent.obtain(
                100, 100, MotionEvent.ACTION_DOWN,
                pointX.toFloat(), pointY.toFloat(), metaState
            )
            onTouchEvent(motionEvent)
            showClearButtonWithoutFocus()
            enableClearBtn(true)
            onTouchEvent(motionEvent)
            motionEvent = MotionEvent.obtain(
                100, 100, MotionEvent.ACTION_MOVE,
                pointX.toFloat(), pointY.toFloat(), metaState
            )
            onTouchEvent(motionEvent)
            motionEvent = MotionEvent.obtain(
                100, 100, MotionEvent.ACTION_UP,
                pointX.toFloat(), pointY.toFloat(), metaState
            )
            onTouchEvent(motionEvent)
            motionEvent = MotionEvent.obtain(
                100, 100, MotionEvent.ACTION_CANCEL,
                pointX.toFloat(), pointY.toFloat(), metaState
            )
            onTouchEvent(motionEvent)
        }
    }

    @Test
    @Throws(IllegalAccessException::class, NoSuchFieldException::class)
    fun testTouchEventOnCancelClearBtnMode() {
        customView?.apply {
            val field = FloatingLabelEditText::class.java.getDeclaredField("hasFocus")
            field.isAccessible = true
            field[this] = true
            enableClearBtn(true)
            val metaState = 0
            val x = View.MeasureSpec.makeMeasureSpec(0, UNSPECIFIED)
            val y = View.MeasureSpec.makeMeasureSpec(0, UNSPECIFIED)
            measure(x, y)
            val pointX = (measuredWidth * .99f).toInt()
            val pointY = measuredHeight shr 1
            var motionEvent = MotionEvent.obtain(
                100, 100, MotionEvent.ACTION_DOWN,
                pointX.toFloat(), pointY.toFloat(), metaState
            )
            onTouchEvent(motionEvent)
            showClearButtonWithoutFocus()
            motionEvent = MotionEvent.obtain(
                100, 100, MotionEvent.ACTION_MOVE, (
                        pointX shl 10).toFloat(), pointY.toFloat(), metaState
            )
            onTouchEvent(motionEvent)
            motionEvent = MotionEvent.obtain(
                100, 100, MotionEvent.ACTION_DOWN,
                pointX.toFloat(), pointY.toFloat(), metaState
            )
            onTouchEvent(motionEvent)
            motionEvent = MotionEvent.obtain(
                100, 100, MotionEvent.ACTION_MOVE,
                pointX.toFloat(), (pointX shl 10).toFloat(), metaState
            )
            onTouchEvent(motionEvent)
            motionEvent = MotionEvent.obtain(
                100, 100, MotionEvent.ACTION_MOVE, (
                        pointX shl 10).toFloat(), (pointX shl 10).toFloat(), metaState
            )
            onTouchEvent(motionEvent)
            motionEvent = MotionEvent.obtain(
                100, 100, MotionEvent.ACTION_UP,
                pointX.toFloat(), pointY.toFloat(), metaState
            )
            onTouchEvent(motionEvent)
        }
    }

    @Test
    @Throws(NoSuchFieldException::class, IllegalAccessException::class)
    fun testTouchEventOnNoTouchClearBtnMode() {
        customView?.apply {
            val field = FloatingLabelEditText::class.java.getDeclaredField("hasFocus")
            field.isAccessible = true
            field[this] = true
            enableClearBtn(true)
            val metaState = 0
            val x = View.MeasureSpec.makeMeasureSpec(0, UNSPECIFIED)
            val y = View.MeasureSpec.makeMeasureSpec(0, UNSPECIFIED)
            measure(x, y)
            val pointX = measuredWidth shl 10
            val pointY = measuredHeight shl 10
            var motionEvent = MotionEvent.obtain(
                100, 100, MotionEvent.ACTION_DOWN,
                pointX.toFloat(), pointY.toFloat(), metaState
            )
            onTouchEvent(motionEvent)
            showClearButtonWithoutFocus()
            motionEvent = MotionEvent.obtain(100, 100, MotionEvent.ACTION_MOVE, 0f, 0f, metaState)
            onTouchEvent(motionEvent)
            motionEvent = MotionEvent.obtain(100, 100, MotionEvent.ACTION_MOVE, 0f, 0f, metaState)
            onTouchEvent(motionEvent)
            motionEvent = MotionEvent.obtain(100, 100, MotionEvent.ACTION_UP, 0f, 0f, metaState)
            onTouchEvent(motionEvent)
        }
    }

    @Test
    @Throws(NoSuchFieldException::class, IllegalAccessException::class)
    fun testTouchEventFocus() {
        customView?.apply {
            enableClearBtn(false)
            val metaState = 0
            val x = View.MeasureSpec.makeMeasureSpec(0, UNSPECIFIED)
            val y = View.MeasureSpec.makeMeasureSpec(0, UNSPECIFIED)
            measure(x, y)
            val pointX = (measuredWidth * .99f).toInt()
            val pointY = measuredHeight shr 1
            val motionEvent = MotionEvent.obtain(
                100, 100, MotionEvent.ACTION_DOWN,
                pointX.toFloat(), pointY.toFloat(), metaState
            )
            onTouchEvent(motionEvent)
            val field = FloatingLabelEditText::class.java.getDeclaredField("hasFocus")
            field.isAccessible = true
            field[this] = true
            onTouchEvent(motionEvent)
            field[this] = false
            showClearButtonWithoutFocus()
            onTouchEvent(motionEvent)
        }
    }

    @Test
    @Throws(NoSuchFieldException::class, IllegalAccessException::class)
    fun testTouchEventFocusWhenEnableClearBtn() {
        customView?.apply {
            enableClearBtn(true)
            val metaState = 0
            val x = View.MeasureSpec.makeMeasureSpec(0, UNSPECIFIED)
            val y = View.MeasureSpec.makeMeasureSpec(0, UNSPECIFIED)
            measure(x, y)
            val pointX = (measuredWidth * .99f).toInt()
            val pointY = measuredHeight shr 1
            val motionEvent = MotionEvent.obtain(
                100, 100, MotionEvent.ACTION_DOWN,
                pointX.toFloat(), pointY.toFloat(), metaState
            )
            onTouchEvent(motionEvent)
            val field = FloatingLabelEditText::class.java.getDeclaredField("hasFocus")
            field.isAccessible = true
            field[this] = true
            onTouchEvent(motionEvent)
            field[this] = false
            showClearButtonWithoutFocus()
            onTouchEvent(motionEvent)
        }
    }

    @Test
    @Throws(
        NoSuchMethodException::class,
        InvocationTargetException::class,
        IllegalAccessException::class,
        NoSuchFieldException::class
    )
    fun testScaleRatio() {
        customView?.apply {
            val method =
                FloatingLabelEditText::class.java.getDeclaredMethod(
                    "setClear_paint_alpha_ratio",
                    Float::class.javaPrimitiveType
                )
            method.isAccessible = true
            val scaleRatio = 3.14f
            method.invoke(this, scaleRatio)
            val field =
                FloatingLabelEditText::class.java.getDeclaredField("clear_paint_alpha_ratio")
            field.isAccessible = true
            assertEquals(scaleRatio, field[this])
        }
    }

    @Test
    fun testInputFilters() {
        customView?.apply {
            var inputFilters = arrayOf(LengthFilter(1), AllCaps())
            filters = inputFilters
            inputFilters = arrayOf(AllCaps(), LengthFilter(1))
            filters = inputFilters
        }
    }

    @Test
    fun testShowMaxTextLength() {
        customView?.apply {
            showMaxTextLength(true)
            assertTrue(isShowMaxTextLength)
        }
    }

    @Test
    fun testTextLengthColor() {
        val color = Color.RED
        customView?.apply {
            textLengthDisplayColor = color
            assertEquals(textLengthDisplayColor, color)
        }
    }

    @Test
    fun testFragManger() {
        customView?.apply {
            fragManager = null
            assertEquals(fragManager, null)
        }
    }

    @Test
    fun testHintColor() {
        customView?.apply {
            setFLEHintTextColor(Color.WHITE)
            assertEquals(fleHintTextColor, Color.WHITE)
        }
    }

    @Test
    fun testHint() {
        customView?.apply {
            setFLEHint(0)
        }
    }

    @Test
    fun testClearBtnImpl() {
        customView?.apply {
            initClearBtnInterceptor(null)
            val impl = object : ClearBtnInterceptor {
                override fun onProcessClear(clear: Boolean?) {
                    TODO("Not yet implemented")
                }

                override fun invokeTouchClearBtn() {
                    TODO("Not yet implemented")
                }
            }
            initClearBtnInterceptor(impl)
        }
    }

    @Test
    fun testMustFill() {
        customView?.apply {
            setMustFillMode(true)
            assertTrue(isMustFill)
            setMustFillMode(false)
            assertFalse(isMustFill)
        }
    }

    @Test
    fun testCheckError() {
        customView?.apply {
            error = "11111"
            assertTrue(isError())
            error = null
            assertFalse(isError())
        }
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        customView = null
    }
}