package com.james602152002.floatinglabeledittext

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.os.Build
import android.text.*
import android.text.InputFilter.LengthFilter
import android.text.style.BackgroundColorSpan
import android.text.style.CharacterStyle
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewConfiguration
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.FragmentManager
import com.james602152002.floatinglabeledittext.intercepter.ClearBtnInterceptor
import com.james602152002.floatinglabeledittext.validator.NumberDecimalValidator
import com.james602152002.floatinglabeledittext.validator.RegexValidator
import kotlinx.coroutines.*
import java.lang.Integer.max
import java.lang.ref.SoftReference
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Created by shiki60215 on 18-1-17.
 */
class FloatingLabelEditText : AppCompatEditText {

    //避免onDraw一直实例化LinearGradient，防止卡顿
    private var errorEraseShader: LinearGradient? = null
    private var labelHorizontalMargin = 0
    private var labelVerticalMargin = 0
    private val labelPaint: TextPaint
    private var dividerStrokeWidth = 0
    private val dividerPaint: Paint
    private val errorPaint: TextPaint
    private var highlightColor = 0
    var dividerColor = 0
    var fleHintTextColor = 0
    private var savedLabel: CharSequence? = null
    private var flePaddingLeft = 0
    private var flePaddingTop = 0
    private var flePaddingRight = 0
    private var flePaddingBottom = 0
    private var textPartHeight = -1
    private var hintTextSize = 0f
    private var floatLabelAnimPercentage = 0f
    var animDuration = 0
        private set
    var errorAnimDuration = 0
        private set
    private var isError = false
    private var error: CharSequence? = null
    private var errorPercentage = 0f
    private var mListener: OnFocusChangeListener? = null
    private var customizeListener: OnFocusChangeListener? = null
    private var hasFocus = false
    private var validatorList: MutableList<RegexValidator>? = null
    var errorDisabled = false
    private var clearButtonPaint: Paint? = null
    private var uniCode: String? = null
    private var enableClearBtn = false
    var clearBtnHorizontalMargin = 0
        private set
    private var clearBtnRect: Rect? = null
    private var clearBtnBitmap: Bitmap? = null
    private var bitmapHeight = 0
    private var touchClearBtn = false
    private var downX = 0f
    private var downY = 0f
    private val touchSlop by lazy { ViewConfiguration.get(context).scaledTouchSlop }
    private var clearPaintAlphaRatio = 1.0f
    private var terminateClick = false
    private var showClearButtonWithoutFocus = false
    private var maxLength = 0
    var isShowMaxTextLength = false
    private val maxLengthPaint: TextPaint
    private var maxLengthTextWidth = 0
    private var startValue = -1f
    var isMustFill = false
        private set

    //if mark changed then updateLabel
    var mustFillMark: CharSequence? = " *"
        set(value) {
            if (field != value) {
                field = value
                updateLabel()
            }
        }
    var normalMark: CharSequence? = null
        set(value) {
            if (field != value) {
                field = value
                updateLabel()
            }
        }

    private var clearBtnInterceptor: ClearBtnInterceptor? = null

    private val widgetLayerRect = RectF()
    private val widgetLayerPaint = Paint()
    private val eraserRect = RectF()
    private val eraserPorterDuff by lazy { PorterDuffXfermode(PorterDuff.Mode.CLEAR) }
    private val eraserPaint by lazy {
        Paint().apply {
            color = Color.WHITE
        }
    }

    private var jobValidator: Job? = null

    var dividerVerticalMargin = 0
        set(value) {
            field = value
            updatePadding()
        }
    private var errorHorizontalMargin = 0
        set(value) {
            field = value
            updateErrorEraseShader()
        }
    var errorColor = 0
        set(value) {
            field = value
            updateErrorEraseShader()
        }
    var label: CharSequence? = null
        set(value) {
            savedLabel = value
            field = initLabel()
            invalidate()
        }
    var labelTextSize = 0f
        set(value) {
            field = value
            updatePadding()
        }
    var errorTextSize = 0f
        set(value) {
            field = value
            errorPaint.textSize = value
            measureTextMaxLength()
            updatePadding()
        }
    private var errorAnimator: ObjectAnimator? = null
        set(value) {
            field = value
            updateErrorEraseShader()
        }
    var clearBtnColor = 0
        set(value) {
            field = value
            invalidate()
        }
    var clearBtnSize = 0
        set(value) {
            field = value
            invalidate()
        }
    var multilineMode = false
        set(value) {
            field = value
            if (value) {
                overScrollMode = View.OVER_SCROLL_ALWAYS
                scrollBarStyle = View.SCROLLBARS_INSIDE_INSET
                isVerticalScrollBarEnabled = true
                setOnTouchListener { view, event ->
                    view.parent.requestDisallowInterceptTouchEvent(true)
                    if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                        view.parent.requestDisallowInterceptTouchEvent(false)
                    }
                    return@setOnTouchListener false
                }
            } else {
                setOnTouchListener(null)
            }
            isSingleLine = !value
        }
    var textLengthDisplayColor = 0
        set(value) {
            field = value
            invalidate()
        }

    //You can bind supportFragmentManager now
    var fragManager: FragmentManager? = null

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    init {
        val textFlag = Paint.ANTI_ALIAS_FLAG
        labelPaint = TextPaint(textFlag)
        dividerPaint = Paint(textFlag)
        errorPaint = TextPaint(textFlag)
        maxLengthPaint = TextPaint(textFlag)

        addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            if (isError) {
                errorPaint.shader = null
                updateErrorEraseShader()
            }
        }
    }

    private fun updateErrorEraseShader() {
        when (errorAnimator) {
            null -> errorEraseShader = null
            else -> {
                if (errorHorizontalMargin > 0 && errorPaint.shader == null && width > 0) {
                    val marginRatio = errorHorizontalMargin.toFloat() / width
                    val gradientRatio = .025f
                    errorEraseShader = LinearGradient(
                        0f,
                        0f,
                        width.toFloat(),
                        0f,
                        intArrayOf(0, errorColor, errorColor, 0),
                        floatArrayOf(
                            marginRatio,
                            marginRatio + gradientRatio,
                            1 - marginRatio - gradientRatio,
                            1 - marginRatio
                        ),
                        Shader.TileMode.CLAMP
                    )
                    errorPaint.shader = errorEraseShader
                } else if (errorHorizontalMargin == 0) {
                    errorPaint.shader = null
                }
            }
        }
    }

    @SuppressLint("Recycle")
    private inline fun attrSetInitializer(
        attrSet: AttributeSet?,
        attrRes: IntArray,
        crossinline impl: (TypedArray) -> Unit
    ) {
        with(attrSet?.let { context.obtainStyledAttributes(it, attrRes) }
            ?: context.obtainStyledAttributes(attrRes)) {
            impl(this)
            recycle()
        }
    }

    private fun init(attrs: AttributeSet?) {
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        var primaryColor = 0
        var clearBtnId = 0
        var textColorHint = 0

        attrSetInitializer(
            null,
            intArrayOf(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) android.R.attr.colorPrimary else primaryColor)
        ) {
            primaryColor = it.getColor(0, 0)
        }

        attrSetInitializer(attrs, R.styleable.FloatingLabelEditText) { typedArray ->
            labelHorizontalMargin = typedArray.getDimensionPixelOffset(
                R.styleable.FloatingLabelEditText_j_fle_label_horizontal_margin,
                0
            )
            labelVerticalMargin = typedArray.getDimensionPixelOffset(
                R.styleable.FloatingLabelEditText_j_fle_label_vertical_margin,
                0
            )
            errorHorizontalMargin = typedArray.getDimensionPixelOffset(
                R.styleable.FloatingLabelEditText_j_fle_error_horizontal_margin,
                0
            )
            dividerVerticalMargin = typedArray.getDimensionPixelOffset(
                R.styleable.FloatingLabelEditText_j_fle_divider_vertical_margin,
                0
            )
            highlightColor = typedArray.getColor(
                R.styleable.FloatingLabelEditText_j_fle_colorHighlight,
                primaryColor
            )
            dividerColor =
                typedArray.getColor(
                    R.styleable.FloatingLabelEditText_j_fle_colorDivider,
                    Color.GRAY
                )
            errorColor =
                typedArray.getColor(R.styleable.FloatingLabelEditText_j_fle_colorError, Color.RED)
            label = typedArray.getString(R.styleable.FloatingLabelEditText_j_fle_hint)
            dividerStrokeWidth = typedArray.getDimensionPixelOffset(
                R.styleable.FloatingLabelEditText_j_fle_thickness,
                dp2px(2f)
            )
            labelTextSize = typedArray.getDimensionPixelOffset(
                R.styleable.FloatingLabelEditText_j_fle_label_textSize,
                sp2Px(16f)
            ).toFloat()
            errorTextSize = typedArray.getDimensionPixelOffset(
                R.styleable.FloatingLabelEditText_j_fle_error_textSize,
                sp2Px(16f)
            ).toFloat()
            dividerPaint.strokeWidth = dividerStrokeWidth.toFloat()
            errorPaint.textSize = errorTextSize
            animDuration =
                typedArray.getInteger(
                    R.styleable.FloatingLabelEditText_j_fle_float_anim_duration,
                    800
                )

            errorAnimDuration =
                typedArray.getInteger(
                    R.styleable.FloatingLabelEditText_j_fle_error_anim_duration,
                    8000
                )

            errorDisabled =
                typedArray.getBoolean(R.styleable.FloatingLabelEditText_j_fle_error_disable, false)
            multilineMode = typedArray.getBoolean(
                R.styleable.FloatingLabelEditText_j_fle_multiline_mode_enable,
                false
            )
            enableClearBtn =
                typedArray.getBoolean(
                    R.styleable.FloatingLabelEditText_j_fle_enable_clear_btn,
                    false
                )
            clearBtnColor = typedArray.getColor(
                R.styleable.FloatingLabelEditText_j_fle_clear_btn_color,
                -0x56000000
            )
            clearBtnHorizontalMargin = typedArray.getDimensionPixelOffset(
                R.styleable.FloatingLabelEditText_j_fle_clear_btn_horizontal_margin,
                dp2px(5f)
            )
            clearBtnId =
                typedArray.getResourceId(R.styleable.FloatingLabelEditText_j_fle_clear_btn_id, -1)
            showClearButtonWithoutFocus = typedArray.getBoolean(
                R.styleable.FloatingLabelEditText_j_fle_show_clear_btn_without_focus,
                false
            )
            isShowMaxTextLength =
                typedArray.getBoolean(
                    R.styleable.FloatingLabelEditText_j_fle_show_text_length,
                    false
                )
            textLengthDisplayColor = typedArray.getColor(
                R.styleable.FloatingLabelEditText_j_fle_text_length_display_color,
                highlightColor
            )
            isMustFill =
                typedArray.getBoolean(R.styleable.FloatingLabelEditText_j_fle_must_fill_type, false)
            textColorHint =
                typedArray.getInt(R.styleable.FloatingLabelEditText_j_fle_textColorHint, 0)
            val decimalValidation =
                typedArray.getString(R.styleable.FloatingLabelEditText_j_fle_number_decimal_validation)
            if (!decimalValidation.isNullOrEmpty()) {
                addValidator(NumberDecimalValidator(decimalValidation))
            }
            if (animDuration < 0) animDuration = 800
            if (errorAnimDuration < 0) errorAnimDuration = 8000

            clearBtnSize = typedArray.getDimensionPixelOffset(
                R.styleable.FloatingLabelEditText_j_fle_clear_btn_size,
                (textSize * .8f).toInt()
            )
        }

        attrSetInitializer(attrs, intArrayOf(android.R.attr.textSize)) { textTypedArray ->
            hintTextSize = textTypedArray.getDimensionPixelOffset(0, sp2Px(20f)).toFloat()
            if (textSize != hintTextSize) {
                textSize = hintTextSize
            }
            labelPaint.textSize = hintTextSize
            textPartHeight = (hintTextSize.roundToInt() * 1.2f).toInt()
        }

        attrSetInitializer(attrs, intArrayOf(android.R.attr.hint)) { hintTypedArray ->
            if (TextUtils.isEmpty(label)) label = hintTypedArray.getString(0)
            savedLabel = label
            fleHintTextColor = currentHintTextColor
            setHintTextColor(textColorHint)
            if (textColorHint != 0 || !TextUtils.isEmpty(hint)) {
                floatLabelAnimPercentage = 1f
            }
        }

        attrSetInitializer(attrs, intArrayOf(android.R.attr.background)) { backgroundTypedArray ->
            val background = backgroundTypedArray.getDrawable(0)
            if (background != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    setBackground(background)
                } else {
                    @Suppress("DEPRECATION")
                    setBackgroundDrawable(background)
                }
            } else setBackgroundColor(0)
        }

        attrSetInitializer(
            attrs, intArrayOf(
                android.R.attr.padding,
                android.R.attr.paddingLeft,
                android.R.attr.paddingTop,
                android.R.attr.paddingRight,
                android.R.attr.paddingBottom
            )
        ) { paddingArray ->
            val convPaddingValue: (index: Int, paddingDir: Int) -> Int = { index, paddingDir ->
                @SuppressLint("ResourceType")
                if (paddingArray.hasValue(index))
                    paddingArray.getDimensionPixelOffset(index, paddingDir)
                else
                    0
            }

            if (paddingArray.hasValue(0)) {
                flePaddingBottom = paddingArray.getDimensionPixelOffset(0, 0)
                flePaddingRight = flePaddingBottom
                flePaddingTop = flePaddingRight
                flePaddingLeft = flePaddingTop
            } else {
                flePaddingLeft = convPaddingValue(1, paddingLeft)
                flePaddingTop = convPaddingValue(2, paddingTop)
                flePaddingRight = convPaddingValue(3, paddingRight)
                flePaddingBottom = convPaddingValue(4, paddingBottom)
            }
        }

        attrSetInitializer(
            attrs,
            intArrayOf(android.R.attr.maxLength)
        ) { textLengthArray ->
            maxLength = textLengthArray.getInteger(0, -1)
        }

        attrSetInitializer(attrs, intArrayOf(android.R.attr.onClick)) { onClickTypedArray ->
            if (!onClickTypedArray.getString(0).isNullOrEmpty()) {
                initOnClickListener()
            }
        }

        includeFontPadding = false
        initFocusChangeListener()
        setSingleLine()
        initTextWatcher()
        if (enableClearBtn) {
            enableClearBtn(true)
        }
        updatePadding()
        if (clearBtnId >= 0) {
            customizeClearBtn(clearBtnId, clearBtnSize)
        }
        if (showClearButtonWithoutFocus) {
            enableClearBtn(true)
        }
        updateLabel()
    }

    private fun initFocusChangeListener() {
        onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            this@FloatingLabelEditText.hasFocus = hasFocus
            if (text.isNullOrEmpty()) {
                if ((hasFocus || !hint.isNullOrEmpty() && currentHintTextColor != 0) && floatLabelAnimPercentage != 1f) {
                    startAnimator(0f, 1f)
                } else if (!hasFocus && floatLabelAnimPercentage != 0f) {
                    startAnimator(1f, 0f)
                }
            }
            customizeListener?.onFocusChange(v, hasFocus)
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
        if (l != null) {
//            setRawInputType(InputType.TYPE_NULL);
//        setInputType(InputType.TYPE_NULL);
            initOnClickListener()
        }
    }

    private fun initOnClickListener() {
        isFocusable = false
        isFocusableInTouchMode = false
        //        addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
    }

    private fun startAnimator(startValue: Float, endValue: Float) {
        val sameAnimator = this.startValue == startValue
        this.startValue = startValue
        if (sameAnimator) {
            return
        }
        val animator =
            ObjectAnimator.ofFloat(this, "FloatLabelAnimPercentage", startValue, endValue)
        animator.interpolator = AccelerateInterpolator(3f)
        animator.duration = animDuration.toLong()
        post { animator.start() }
    }

    override fun setMaxLines(maxLines: Int) {
        super.setMaxLines(maxLines)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            when (maxHeight) {
                Integer.MAX_VALUE,
                -1 -> {
                }

                else -> maxHeight
            }
        }
    }

    override fun setOnFocusChangeListener(l: OnFocusChangeListener) {
        if (mListener == null) {
            mListener = l
        } else {
            customizeListener = l
        }
        super.setOnFocusChangeListener(mListener)
    }

    override fun getOnFocusChangeListener(): OnFocusChangeListener {
        return customizeListener ?: super.getOnFocusChangeListener()
    }

    private fun initTextWatcher() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                jobValidator?.cancel()
                jobValidator = CoroutineScope(Dispatchers.Default).launch {
                    maxLengthTextWidth = measureTextMaxLength()
                    var error = false
                    validatorList?.find { it.getError_message(s) != null }?.let {
                        withContext(Dispatchers.Main) {
                            setError(it.getError_message(s))
                            error = true
                        }
                    }
                    withContext(Dispatchers.Main) {
                        if (!error) {
                            setError(null)
                            errorPercentage = 0f
                        }
                        changeLabelState()
                    }
                }
            }
        })
    }

    private fun changeLabelState() {
        if ((!TextUtils.isEmpty(text) || !TextUtils.isEmpty(hint) && currentHintTextColor != 0) && floatLabelAnimPercentage != 1f) {
            startAnimator(0f, 1f)
        } else if (TextUtils.isEmpty(text) && !(!TextUtils.isEmpty(hint) && currentHintTextColor != 0) && floatLabelAnimPercentage != 0f) {
            startAnimator(1f, 0f)
        }
    }

    private fun dp2px(dpValue: Float): Int {
        return (0.5f + dpValue * resources.displayMetrics.density).toInt()
    }

    private fun sp2Px(spValue: Float): Int {
        return (spValue * resources.displayMetrics.scaledDensity).toInt()
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        flePaddingLeft = left
        flePaddingTop = top
        flePaddingRight = right
        flePaddingBottom = bottom
        super.setPadding(
            left,
            top + labelVerticalMargin + labelTextSize.toInt(),
            right + clearBtnModePadding,
            bottom + dividerStrokeWidth + dividerVerticalMargin + if (!errorDisabled) (errorTextSize * 1.2f).toInt() + (dividerVerticalMargin shl 1) else 0
        )
    }

    private val clearBtnModePadding: Int
        get() = if (enableClearBtn) clearBtnSize + (clearBtnHorizontalMargin shl 1) else 0

    private fun updatePadding() {
        setPadding(
            flePaddingLeft,
            flePaddingTop,
            flePaddingRight,
            flePaddingBottom
        )
    }

    public override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = width
        labelPaint.color = if (hasFocus) highlightColor else fleHintTextColor
        val currentTextSize =
            hintTextSize + (labelTextSize - hintTextSize) * floatLabelAnimPercentage
        labelPaint.textSize = currentTextSize
        val scrollX = scrollX
        if (!text.isNullOrEmpty()) {
            labelPaint.alpha = (255 * floatLabelAnimPercentage).toInt()
        } else {
            labelPaint.alpha = 255
        }
        val labelPaintDy =
            (flePaddingTop + labelTextSize + currentTextSize * (1 - floatLabelAnimPercentage) * .93f).toInt()
        if (label != null) {
            drawSpannableString(
                canvas,
                label,
                labelPaint,
                scrollX + labelHorizontalMargin,
                labelPaintDy + scrollY
            )
        }

        val measuredLineHeight = textPartHeight * max(1, lineCount)
        val linesHeight = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            when (maxHeight) {
                Integer.MAX_VALUE,
                -1 -> measuredLineHeight

                else -> min(
                    maxHeight - (paddingTop + paddingBottom),
                    measuredLineHeight
                )
            }
        } else {
            textPartHeight * measuredLineHeight
        }
        val dividerY: Int =
            (flePaddingTop + labelTextSize + labelVerticalMargin + linesHeight + (dividerStrokeWidth shr 1) + dividerVerticalMargin + scrollY).toInt()
        if (!isError) {
            dividerPaint.color = if (hasFocus) highlightColor else dividerColor
        } else {
            dividerPaint.color = errorColor
            val errorPaintDy = (dividerY + errorTextSize + dividerVerticalMargin).toInt()
            val errorTextWidth = errorPaint.measureText(error.toString())
            val hintRepeatSpaceWidth = width / 3
            val maxDx = hintRepeatSpaceWidth + errorTextWidth
            val startX = errorHorizontalMargin - (maxDx * errorPercentage).toInt() + scrollX
            errorPaint.color = errorColor
            if (errorAnimator != null) {
                if (errorHorizontalMargin > 0 && errorPaint.shader == null) {
//                    val marginRatio = error_horizontal_margin.toFloat() / width
//                    val gradientRatio = .025f
//                    val shader = LinearGradient(
//                        0f,
//                        0f,
//                        width.toFloat(),
//                        0f,
//                        intArrayOf(0, error_color, error_color, 0),
//                        floatArrayOf(
//                            marginRatio,
//                            marginRatio + gradientRatio,
//                            1 - marginRatio - gradientRatio,
//                            1 - marginRatio
//                        ),
//                        Shader.TileMode.CLAMP
//                    )
                    errorPaint.shader = errorEraseShader
                } else if (errorHorizontalMargin == 0) {
                    errorPaint.shader = null
                }
            }

            widgetLayerRect.set(
                0f, 0f, (width + scrollX).toFloat(),
                height.toFloat()
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                canvas.saveLayer(widgetLayerRect, widgetLayerPaint)
            } else {
                @Suppress("DEPRECATION")
                canvas.saveLayer(widgetLayerRect, widgetLayerPaint, Canvas.ALL_SAVE_FLAG)
            }

            drawSpannableString(canvas, error, errorPaint, startX, errorPaintDy)
            if (startX < 0 && startX + maxDx < width) {
                drawSpannableString(
                    canvas,
                    error,
                    errorPaint,
                    (startX + maxDx).toInt(),
                    errorPaintDy
                )
            }
            if (maxLengthTextWidth > 0) {
                eraserRect.set(
                    (width + scrollX - maxLengthTextWidth - errorHorizontalMargin).toFloat(),
                    dividerY.toFloat(),
                    (width + scrollX).toFloat(),
                    height.toFloat()
                )
                eraserPaint.xfermode = eraserPorterDuff
                canvas.drawRect(eraserRect, eraserPaint)
                eraserPaint.xfermode = null
                canvas.restore()
            }
        }
        canvas.drawLine(
            scrollX.toFloat(),
            dividerY.toFloat(),
            (width + scrollX).toFloat(),
            dividerY.toFloat(),
            dividerPaint
        )
        if (hasFocus || showClearButtonWithoutFocus) {
            drawClearBtn(canvas, scrollX)
        }
        if (isShowMaxTextLength) drawMaxLength(
            canvas,
            width + scrollX,
            dividerY + errorTextSize + dividerVerticalMargin
        )
    }

    private fun drawSpannableString(
        canvas: Canvas,
        beforeHint: CharSequence?,
        paint: TextPaint,
        start_x: Int,
        start_y: Int
    ) {
        // draw each span one at a time
        var afterHint = beforeHint
        var next: Int
        var xStart = start_x.toFloat()
        var xEnd: Float
        if (paint !== errorPaint) afterHint = TextUtils.ellipsize(
            afterHint,
            paint,
            (width - flePaddingLeft - flePaddingRight - labelHorizontalMargin - clearBtnModePadding).toFloat(),
            TextUtils.TruncateAt.END
        )
        if (afterHint is SpannableString) {
            val spannableString = afterHint
            var i = 0
            while (i < spannableString.length) {

                // find the next span transition
                next = spannableString.nextSpanTransition(
                    i,
                    spannableString.length,
                    CharacterStyle::class.java
                )

                // measure the length of the span
                xEnd = xStart + paint.measureText(spannableString, i, next)

                // draw the highlight (background color) first
                val bgSpans = spannableString.getSpans(i, next, BackgroundColorSpan::class.java)
                if (bgSpans.isNotEmpty()) {
                    val mHighlightPaint = Paint(Paint.ANTI_ALIAS_FLAG)
                    mHighlightPaint.color = bgSpans[0].backgroundColor
                    canvas.drawRect(
                        xStart,
                        start_y + paint.fontMetrics.top,
                        xEnd,
                        start_y + paint.fontMetrics.bottom,
                        mHighlightPaint
                    )
                }

                // draw the text with an optional foreground color
                val fgSpans = spannableString.getSpans(i, next, ForegroundColorSpan::class.java)
                if (fgSpans.isNotEmpty()) {
                    val saveColor = paint.color
                    paint.color = fgSpans[0].foregroundColor
                    canvas.drawText(spannableString, i, next, xStart, start_y.toFloat(), paint)
                    paint.color = saveColor
                } else {
                    canvas.drawText(spannableString, i, next, xStart, start_y.toFloat(), paint)
                }
                xStart = xEnd
                i = next
            }
        } else {
            afterHint?.let {
                canvas.drawText(
                    it,
                    0,
                    afterHint.length,
                    xStart,
                    start_y.toFloat(),
                    paint
                )
            }
        }
    }

    private fun drawClearBtn(canvas: Canvas, scrollX: Int) {
        if (enableClearBtn && !text.isNullOrEmpty()) {
            val (paint, bitmap) = kotlin.run { clearButtonPaint to clearBtnBitmap }
            paint ?: return
            if (bitmap == null) {
                clearBtnRect?.let { bounds ->
                    val alpha =
                        ((clearBtnColor shr 24 and 0xFF) * clearPaintAlphaRatio).toInt()
                    val color = (alpha shl 24) + (clearBtnColor and 0x00FFFFFF)
                    paint.color = color

                    val spanned = uniCode?.let {
                        HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_COMPACT).toString()
                    } ?: ""
                    paint.getTextBounds(spanned, 0, spanned.length, bounds)
                    canvas.drawText(
                        spanned,
                        width - flePaddingRight + scrollX - (clearBtnSize + paint.measureText(
                            spanned
                        )) * .5f - clearBtnHorizontalMargin,
                        flePaddingTop + labelTextSize + scrollY + (labelVerticalMargin + bounds.height() + textPartHeight + dividerVerticalMargin shr 1),
                        paint
                    )
                }
            } else {
                paint.alpha = (clearPaintAlphaRatio * 255).toInt()
                canvas.drawBitmap(
                    bitmap,
                    (width - flePaddingRight + scrollX - clearBtnSize - clearBtnHorizontalMargin).toFloat(),
                    flePaddingTop + labelTextSize + scrollY + (labelVerticalMargin + hintTextSize + dividerVerticalMargin - bitmapHeight) / 2,
                    clearButtonPaint
                )
            }
        }
    }

    private fun measureTextMaxLength(): Int {
        if (maxLength <= 0) return 0
        return if (isShowMaxTextLength) {
            val textLength = text?.length ?: 0
            maxLengthPaint.textSize = errorTextSize
            val lengthStrBuilder = StringBuilder()
            lengthStrBuilder.append(textLength).append("/").append(maxLength)
            val lengthStr = lengthStrBuilder.toString()
            maxLengthPaint.measureText(lengthStr).roundToInt()
        } else {
            0
        }
    }

    private fun drawMaxLength(canvas: Canvas, dx: Int, dy: Float) {
        if (maxLengthTextWidth == 0) return
        maxLengthPaint.color = textLengthDisplayColor
        val textLength = text?.length ?: 0
        val lengthStrBuilder = StringBuilder()
        lengthStrBuilder.append(textLength).append("/").append(maxLength)
        val lengthStr = lengthStrBuilder.toString()
        canvas.drawText(
            lengthStr,
            (dx - maxLengthTextWidth - flePaddingRight).toFloat(),
            dy,
            maxLengthPaint
        )
    }

    private fun setFloatLabelAnimPercentage(float_label_anim_percentage: Float) {
        this.floatLabelAnimPercentage = float_label_anim_percentage
        postInvalidate()
    }

    fun setLabelMargins(horizontal_margin: Int, vertical_margin: Int) {
        labelHorizontalMargin = horizontal_margin
        labelVerticalMargin = vertical_margin
        updatePadding()
    }

    var thickness: Int
        get() = dividerStrokeWidth
        set(thickness) {
            dividerStrokeWidth = thickness
            dividerPaint.strokeWidth = dividerStrokeWidth.toFloat()
            updatePadding()
        }

    fun setErrorMargin(horizontal_margin: Int) {
        errorHorizontalMargin = horizontal_margin
        updatePadding()
    }

    fun setAnimDuration(passAnimDuration: Int) {
        var measuredAnimDuration = passAnimDuration
        if (measuredAnimDuration < 0) measuredAnimDuration = 800
        animDuration = measuredAnimDuration
    }

    fun setErrorAnimDuration(passAnimDuration: Int) {
        var measuredAnimDuration = passAnimDuration
        if (measuredAnimDuration < 0) measuredAnimDuration = 8000
        errorAnimDuration = measuredAnimDuration
    }

    override fun setHighlightColor(color: Int) {
        highlightColor = color
        super.setHighlightColor(color)
    }

    override fun getHighlightColor(): Int {
        return highlightColor
    }

    override fun getError(): CharSequence? {
        return error
    }

    override fun setError(error: CharSequence?) {
        if (errorDisabled) {
            return
        }
        isError = !error.isNullOrEmpty()
        this.error = error
        if (isError) {
            if (width > 0) {
                startErrorAnimation()
            } else {
                startErrorAnimation()
            }
        } else {
            errorAnimator?.cancel()
            errorAnimator = null
        }
        invalidate()
    }

    private fun startErrorAnimation() {
        val errorLength = errorPaint.measureText(error.toString())
        val w = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        val h = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        measure(w, h)
        val width = if (width > 0) width else measuredWidth
        val maxLengthWidth = 0
        if (isShowMaxTextLength) {
            maxLengthTextWidth = measureTextMaxLength()
        }
        if (errorLength > width - (errorHorizontalMargin shl 1) - maxLengthWidth) {
            errorPercentage = 0f
            errorAnimator = when (errorAnimator) {
                null -> ObjectAnimator.ofFloat(this, "ErrorPercentage", 0f, 1f)
                else -> errorAnimator
            }?.apply {
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.RESTART
                startDelay = animDuration.toLong()
                var destDuration = (errorAnimDuration * errorLength / width).toLong()
                if (destDuration < 0) destDuration = 8000L
                duration = destDuration
                start()
            }
        } else {
            errorPercentage = 0f
        }
    }

    private fun setErrorPercentage(error_percentage: Float) {
        this.errorPercentage = error_percentage
        invalidate()
    }

    override fun setTextSize(size: Float) {
        hintTextSize = size
        textPartHeight = (hintTextSize.roundToInt() * 1.2f).toInt()
        super.setTextSize(size)
    }

    override fun setTextSize(unit: Int, size: Float) {
        val c = context
        val r = c.resources
        hintTextSize = TypedValue.applyDimension(unit, size, r.displayMetrics)
        textPartHeight = (hintTextSize.roundToInt() * 1.2f).toInt()
        super.setTextSize(unit, size)
    }

    fun setValidatorList(list: List<RegexValidator>?) {
        if (list != null) {
            validatorList = (validatorList?.apply { clear() } ?: arrayListOf()).apply {
                this += list
            }
        }
    }

    fun getValidatorList(): List<RegexValidator>? {
        return validatorList
    }

    fun addValidator(validator: RegexValidator?) {
        validatorList = (validatorList ?: arrayListOf()).apply {
            validator?.let { this += it }
        }
    }

    fun setErrorDisabled() {
        errorDisabled = true
        updatePadding()
    }

    fun setErrorEnabled() {
        errorDisabled = false
        updatePadding()
    }

    // enable default clear button
    fun enableClearBtn(enable: Boolean) {
        enableClearBtn = enable
        if (enable) {
            initClearBtn()
            clearButtonPaint?.apply {
                textSize = clearBtnSize.toFloat()
                val tf =
                    Typeface.createFromAsset(
                        resources.assets,
                        "floating_label_edit_text_iconfont.ttf"
                    )
                typeface = tf
                color = clearBtnColor
                uniCode = "&#xe724;"
            }
        } else {
            clearButtonPaint = null
            clearBtnRect = null
        }
        updatePadding()
    }

    // customize your clear button by ttf
    fun customizeClearBtn(
        typeface: Typeface?,
        uni_code: String?,
        color: Int,
        clear_btn_size: Int
    ) {
        enableClearBtn = true
        initClearBtn()
        clearButtonPaint?.apply {
            textSize = clear_btn_size.toFloat()
            this.typeface = typeface
            this.color = color
        }

        this.uniCode = uni_code
        clearBtnColor = color
        this.clearBtnSize = clear_btn_size
        updatePadding()
    }

    fun customizeClearBtn(drawableId: Int, clear_btn_width: Int) {
        enableClearBtn = true
        clearBtnSize = clear_btn_width
        val context = context
        val drawable = ContextCompat.getDrawable(context, drawableId)
        val resources = resources
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        val sampleBitmap = createBitmap(drawable, resources, drawableId, options)
        var sampleSize = 1
        var width = options.outWidth
        var height = options.outHeight
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && drawable is VectorDrawable) {
            width = drawable.getIntrinsicWidth()
            height = drawable.getIntrinsicHeight()
        }
        bitmapHeight = (height * clear_btn_width / width)
        val destinationHeight = bitmapHeight
        if (height > destinationHeight || width > clear_btn_width) {
            val halfHeight = height shr 1
            val halfWidth = width shr 1
            while (halfHeight / sampleSize > destinationHeight && halfWidth / sampleSize > clear_btn_width) {
                sampleSize *= 2
            }
        }
        sampleBitmap?.recycle()
        options.inSampleSize = sampleSize
        options.inJustDecodeBounds = false
        createBitmap(drawable, resources, drawableId, options)?.let { oldBitmap ->
            width = oldBitmap.width
            height = oldBitmap.height
            val matrix = Matrix()
            val scaleX = clear_btn_width.toFloat() / width
            val scaleY = destinationHeight.toFloat() / height
            matrix.postScale(scaleX, scaleY)
            clearBtnBitmap = SoftReference(
                Bitmap.createBitmap(oldBitmap, 0, 0, width, height, matrix, true)
            ).get()
        }
        initClearBtn()
        updatePadding()
    }

    private fun createBitmap(
        drawable: Drawable?,
        resources: Resources,
        drawableId: Int,
        options: BitmapFactory.Options
    ): Bitmap? {
        return if (drawable is BitmapDrawable) {
            getBitmap(resources, drawableId, options)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && drawable is VectorDrawable) {
            getVectorBitmap(drawable)
        } else {
            throw IllegalArgumentException("unsupported drawable type")
        }
    }

    private fun getBitmap(
        resources: Resources,
        drawableId: Int,
        options: BitmapFactory.Options
    ): Bitmap? {
        return SoftReference(
            BitmapFactory.decodeResource(resources, drawableId, options)
        ).get()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getVectorBitmap(vectorDrawable: VectorDrawable): Bitmap {
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        return bitmap
    }

    private fun initClearBtn() {
        if (clearButtonPaint == null) {
            clearButtonPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        }
        if (clearBtnRect == null) {
            clearBtnRect = Rect()
        }
    }

    override fun setSingleLine() {
        if (multilineMode) return
        super.setSingleLine()
    }

    fun clearBtnHorizontalMargin(clear_btn_horizontal_margin: Int) {
        this.clearBtnHorizontalMargin = clear_btn_horizontal_margin
        invalidate()
    }

    //Even your edit text doesn't have focus, your clear button still show at right.
    fun showClearButtonWithoutFocus() {
        showClearButtonWithoutFocus = true
        enableClearBtn(true)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (enableClearBtn && (hasFocus || showClearButtonWithoutFocus)) {
            val result = kotlin.runCatching {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        downX = event.x
                        downY = event.y
                        touchClearBtn = touchClearBtn(downX, downY)
                        if (touchClearBtn) {
                            fadeClearBtnIcon(true)
                            post { requestFocus() }
                            return@runCatching true
                        } else {

                        }
                    }

                    MotionEvent.ACTION_MOVE -> if (touchClearBtn && (abs(downX - event.x) >= touchSlop || abs(
                            downY - event.y
                        ) >= touchSlop)
                    ) {
                        touchClearBtn = false
                        terminateClick = true
                    } else {

                    }

                    MotionEvent.ACTION_UP -> {
                        val interruptActionUp = touchClearBtn || terminateClick
                        if (touchClearBtn) {
                            clearBtnInterceptor?.invokeTouchClearBtn() ?: kotlin.run {
                                text = null
                            }
                        } else {

                        }
                        reset()
                        if (interruptActionUp) {
                            return@runCatching false
                        } else {

                        }
                    }

                    MotionEvent.ACTION_CANCEL -> reset()
                    else -> {}
                }
//                return false
            }.getOrNull()
            if (result is Boolean) {
                return result
            }
        }
        return super.onTouchEvent(event)
    }

    private fun touchClearBtn(x: Float, y: Float): Boolean {
        val width = width
        if (measuredWidth <= 0) {
            val w = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            measure(w, w)
        }
        val right = if (width != 0) width else measuredWidth
        val clearBtnWidth: Int =
            (clearBtnSize + (clearBtnHorizontalMargin shl 1) + scaleX).toInt()
        val clearBtnTop = (flePaddingTop + labelTextSize + scrollY).toInt()
        val clearBtnBottom =
            clearBtnTop + labelVerticalMargin + textPartHeight + dividerVerticalMargin
        return x >= right - clearBtnWidth && x <= right && y >= clearBtnTop && y <= clearBtnBottom
    }

    @Synchronized
    private fun fadeClearBtnIcon(focus: Boolean) {
        val defaultValue = 1f
        val focusValue = 0.5f
        ObjectAnimator.ofFloat(
            this, "clearPaintAlphaRatio",
            if (focus) defaultValue else focusValue, if (focus) focusValue else defaultValue
        ).apply {
            duration = 500
            start()
        }
    }

    private fun reset() {
        if (terminateClick || touchClearBtn) fadeClearBtnIcon(false)
        terminateClick = false
        touchClearBtn = false
    }

    private fun setClearPaintAlphaRatio(clear_paint_alpha_ratio: Float) {
        this.clearPaintAlphaRatio = clear_paint_alpha_ratio
        postInvalidate()
    }

    override fun setFilters(filters: Array<InputFilter>) {
        for (i in filters.indices) {
            val filter = filters[i]
            if (filter is LengthFilter) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    maxLength = filter.max
                } else {
                    try {
                        @SuppressLint("DiscouragedPrivateApi")
                        val field = LengthFilter::class.java.getDeclaredField("mMax")
                        field.isAccessible = true
                        maxLength = (field[filter] as? Int) ?: 0
                    } catch (e: Exception) {
                    }
                }
                break
            }
        }
        super.setFilters(filters)
    }

    fun showMaxTextLength(show: Boolean) {
        isShowMaxTextLength = show
        invalidate()
    }

    fun isError(): Boolean {
        return isError
    }

    fun setMustFillMode(isMustFill: Boolean) {
        this.isMustFill = isMustFill
        updateLabel()
    }

    private fun initLabel(): CharSequence? {
        return when (isMustFill) {
            true -> {
                initMustFillSpan()
            }

            else -> {
                initNormalSpan()
            }
        }
    }

    private fun updateLabel() {
        label = savedLabel
//        invalidate()
    }

    fun setFLEHintTextColor(color: Int) {
        super.setHintTextColor(color)
        changeLabelState()
    }

    fun setFLEHint(resID: Int) {
        if (resID > 0) {
            super.setHint(resID)
        } else {
            super.setHint(null)
        }
        changeLabelState()
    }

    fun initClearBtnInterceptor(impl: ClearBtnInterceptor?) {
        if (clearBtnInterceptor == null) clearBtnInterceptor = impl
    }

    private fun initMustFillSpan() = initSpan(mustFillMark)

    private fun initNormalSpan() = initSpan(normalMark)

    private fun initSpan(mark: CharSequence?): CharSequence? {
        return when (mark.isNullOrEmpty()) {
            true -> savedLabel
            else -> SpannableString("$savedLabel$mark").apply {
                val markLength = mark.length
                val index = length - markLength
                if (index in indices) {
                    setSpan(
                        ForegroundColorSpan(Color.RED),
                        index, length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }
        }
    }
}