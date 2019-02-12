package com.james602152002.floatinglabeledittext;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateInterpolator;

import com.james602152002.floatinglabeledittext.validator.NumberDecimalValidator;
import com.james602152002.floatinglabeledittext.validator.RegexValidator;

import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shiki60215 on 18-1-17.
 */

public class FloatingLabelEditText extends AppCompatEditText {

    private short label_horizontal_margin;
    private short label_vertical_margin;
    private final TextPaint labelPaint;

    private short divider_stroke_width;
    private final Paint dividerPaint;

    private short divider_vertical_margin;
    private short error_horizontal_margin;
    private final TextPaint errorPaint;

    private int highlight_color;
    private int divider_color;
    private int hint_text_color;
    private int error_color;
    private CharSequence label;
    private short padding_left, padding_top, padding_right, padding_bottom;
    private short text_part_height = -1;

    private float label_text_size;
    private float hint_text_size;
    private float error_text_size;
    private float float_label_anim_percentage = 0;
    private short ANIM_DURATION;
    private short ERROR_ANIM_DURATION_PER_WIDTH;
    private boolean is_error = false;
    private CharSequence error;
    private ObjectAnimator errorAnimator;
    private float error_percentage = 0;
    private OnFocusChangeListener mListener;
    private OnFocusChangeListener customizeListener;
    private boolean hasFocus = false;
    private List<RegexValidator> validatorList;
    private boolean error_disabled = false;

    private Paint clearButtonPaint;
    private short clear_btn_size;
    private String uni_code;
    private int clear_btn_color;
    private boolean enable_clear_btn = false;
    private short clear_btn_horizontal_margin;
    private Rect bounds;
    private Bitmap clear_btn_bitmap;
    private short bitmap_height;

    private boolean multiline_mode = false;

    private boolean touch_clear_btn = false;
    private float downX, downY;
    private final short touchSlop;
    private float clear_paint_alpha_ratio = 1.0f;
    private boolean terminate_click = false;
    private boolean show_clear_button_without_focus = false;

    private int max_length;
    private boolean show_max_length;
    private final TextPaint maxLengthPaint;
    private int text_length_display_color;
    private int max_length_text_width;

    public FloatingLabelEditText(Context context) {
        super(context);
        final int anti_alias_flag = Paint.ANTI_ALIAS_FLAG;
        labelPaint = new TextPaint(anti_alias_flag);
        dividerPaint = new Paint(anti_alias_flag);
        errorPaint = new TextPaint(anti_alias_flag);
        maxLengthPaint = new TextPaint(anti_alias_flag);
        touchSlop = (short) ViewConfiguration.get(context).getScaledTouchSlop();
        init(context, null);
    }

    public FloatingLabelEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        final int anti_alias_flag = Paint.ANTI_ALIAS_FLAG;
        labelPaint = new TextPaint(anti_alias_flag);
        dividerPaint = new Paint(anti_alias_flag);
        errorPaint = new TextPaint(anti_alias_flag);
        maxLengthPaint = new TextPaint(anti_alias_flag);
        touchSlop = (short) ViewConfiguration.get(context).getScaledTouchSlop();
        init(context, attrs);
    }

    public FloatingLabelEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final int anti_alias_flag = Paint.ANTI_ALIAS_FLAG;
        labelPaint = new TextPaint(anti_alias_flag);
        dividerPaint = new Paint(anti_alias_flag);
        errorPaint = new TextPaint(anti_alias_flag);
        maxLengthPaint = new TextPaint(anti_alias_flag);
        touchSlop = (short) ViewConfiguration.get(context).getScaledTouchSlop();
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        TypedArray defaultArray = context.obtainStyledAttributes(new int[]{R.attr.colorPrimary});
        final int primary_color = defaultArray.getColor(0, 0);
        defaultArray.recycle();
        defaultArray = null;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FloatingLabelEditText);
        label_horizontal_margin = (short) typedArray.getDimensionPixelOffset(R.styleable.FloatingLabelEditText_j_fle_label_horizontal_margin, 0);
        label_vertical_margin = (short) typedArray.getDimensionPixelOffset(R.styleable.FloatingLabelEditText_j_fle_label_vertical_margin, 0);
        error_horizontal_margin = (short) typedArray.getDimensionPixelOffset(R.styleable.FloatingLabelEditText_j_fle_error_horizontal_margin, 0);
        divider_vertical_margin = (short) typedArray.getDimensionPixelOffset(R.styleable.FloatingLabelEditText_j_fle_divider_vertical_margin, 0);
        highlight_color = typedArray.getColor(R.styleable.FloatingLabelEditText_j_fle_colorHighlight, primary_color);
        divider_color = typedArray.getColor(R.styleable.FloatingLabelEditText_j_fle_colorDivider, Color.GRAY);
        error_color = typedArray.getColor(R.styleable.FloatingLabelEditText_j_fle_colorError, Color.RED);
        label = typedArray.getString(R.styleable.FloatingLabelEditText_j_fle_hint);
        divider_stroke_width = (short) typedArray.getDimensionPixelOffset(R.styleable.FloatingLabelEditText_j_fle_thickness, dp2px(2));
        label_text_size = typedArray.getDimensionPixelOffset(R.styleable.FloatingLabelEditText_j_fle_label_textSize, sp2Px(16));
        error_text_size = typedArray.getDimensionPixelOffset(R.styleable.FloatingLabelEditText_j_fle_error_textSize, sp2Px(16));
        dividerPaint.setStrokeWidth(divider_stroke_width);
        errorPaint.setTextSize(error_text_size);
        ANIM_DURATION = (short) typedArray.getInteger(R.styleable.FloatingLabelEditText_j_fle_float_anim_duration, 800);
        ERROR_ANIM_DURATION_PER_WIDTH = (short) typedArray.getInteger(R.styleable.FloatingLabelEditText_j_fle_error_anim_duration, 8000);
        error_disabled = typedArray.getBoolean(R.styleable.FloatingLabelEditText_j_fle_error_disable, false);
        multiline_mode = typedArray.getBoolean(R.styleable.FloatingLabelEditText_j_fle_multiline_mode_enable, false);
        enable_clear_btn = typedArray.getBoolean(R.styleable.FloatingLabelEditText_j_fle_enable_clear_btn, false);
        clear_btn_color = typedArray.getColor(R.styleable.FloatingLabelEditText_j_fle_clear_btn_color, 0xAA000000);
        clear_btn_horizontal_margin = ((short) typedArray.getDimensionPixelOffset(R.styleable.FloatingLabelEditText_j_fle_clear_btn_horizontal_margin, dp2px(5)));
        int clear_btn_id = typedArray.getResourceId(R.styleable.FloatingLabelEditText_j_fle_clear_btn_id, -1);
        show_clear_button_without_focus = typedArray.getBoolean(R.styleable.FloatingLabelEditText_j_fle_show_clear_btn_without_focus, false);
        show_max_length = typedArray.getBoolean(R.styleable.FloatingLabelEditText_j_fle_show_text_length, false);
        text_length_display_color = typedArray.getColor(R.styleable.FloatingLabelEditText_j_fle_text_length_display_color, highlight_color);
        String decimalValidation = typedArray.getString(R.styleable.FloatingLabelEditText_j_fle_number_decimal_validation);
        if (!TextUtils.isEmpty(decimalValidation)) {
            addValidator(new NumberDecimalValidator(decimalValidation));
        }

        if (ANIM_DURATION < 0)
            ANIM_DURATION = 800;
        if (ERROR_ANIM_DURATION_PER_WIDTH < 0)
            ERROR_ANIM_DURATION_PER_WIDTH = 8000;

        TypedArray textTypedArray = context.obtainStyledAttributes(attrs, new int[]{android.R.attr.textSize});
        hint_text_size = textTypedArray.getDimensionPixelOffset(0, sp2Px(20));
        if (getTextSize() != hint_text_size) {
            setTextSize(hint_text_size);
        }
        labelPaint.setTextSize(hint_text_size);
        text_part_height = (short) (Math.round(hint_text_size) * 1.2f);
        textTypedArray.recycle();
        textTypedArray = null;

        TypedArray hintTypedArray = context.obtainStyledAttributes(attrs, new int[]{android.R.attr.hint});
        if (TextUtils.isEmpty(label))
            label = hintTypedArray.getString(0);
        else
            setHint(label);
        hint_text_color = getCurrentHintTextColor();
        setHintTextColor(0);
        hintTypedArray.recycle();
        hintTypedArray = null;

        TypedArray backgroundTypedArray = context.obtainStyledAttributes(attrs, new int[]{android.R.attr.background});
        Drawable background = backgroundTypedArray.getDrawable(0);
        if (background != null) {
            setBackgroundDrawable(background);
        } else
            setBackgroundColor(0);
        backgroundTypedArray.recycle();
        backgroundTypedArray = null;

        clear_btn_size = (short) typedArray.getDimensionPixelOffset(R.styleable.FloatingLabelEditText_j_fle_clear_btn_size, (int) (getTextSize() * .8f));

        typedArray.recycle();
        typedArray = null;

        TypedArray paddingArray = context.obtainStyledAttributes(attrs, new int[]
                {android.R.attr.padding, android.R.attr.paddingLeft, android.R.attr.paddingTop, android.R.attr.paddingRight, android.R.attr.paddingBottom});
        if (paddingArray.hasValue(0)) {
            padding_left = padding_top = padding_right = padding_bottom = (short) paddingArray.getDimensionPixelOffset(0, 0);
        } else {
            padding_left = (short) (paddingArray.hasValue(1) ? paddingArray.getDimensionPixelOffset(1, getPaddingLeft()) : 0);
            padding_top = (short) (paddingArray.hasValue(2) ? paddingArray.getDimensionPixelOffset(2, getPaddingTop()) : 0);
            padding_right = (short) (paddingArray.hasValue(3) ? paddingArray.getDimensionPixelOffset(3, getPaddingRight()) : 0);
            padding_bottom = (short) (paddingArray.hasValue(4) ? paddingArray.getDimensionPixelOffset(4, getPaddingBottom()) : 0);
        }
        paddingArray.recycle();
        paddingArray = null;

        TypedArray text_length_array = context.obtainStyledAttributes(attrs, new int[]{android.R.attr.maxLength});
        max_length = text_length_array.getInteger(0, -1);
        text_length_array.recycle();
        text_length_array = null;

        TypedArray onClickTypedArray = context.obtainStyledAttributes(attrs, new int[]{android.R.attr.onClick});
        if (!TextUtils.isEmpty(onClickTypedArray.getString(0))) {
            initOnClickListener();
        }
        onClickTypedArray.recycle();
        onClickTypedArray = null;

        setIncludeFontPadding(false);
        initFocusChangeListener();
        setSingleLine();
        initTextWatcher();
        if (enable_clear_btn) {
            enableClearBtn(enable_clear_btn);
        }
        updatePadding();
        if (clear_btn_id >= 0) {
            customizeClearBtn(clear_btn_id, clear_btn_size);
        }
        if (show_clear_button_without_focus) {
            enableClearBtn(true);
        }
    }

    private void initFocusChangeListener() {
        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                FloatingLabelEditText.this.hasFocus = hasFocus;
                if (hasFocus) {
                    if (float_label_anim_percentage != 1)
                        startAnimator(0, 1);
                } else if (TextUtils.isEmpty(getText().toString()) && float_label_anim_percentage != 0) {
                    startAnimator(1, 0);
                }
                if (customizeListener != null) {
                    customizeListener.onFocusChange(v, hasFocus);
                }
            }
        });
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
        if (l != null) {
//            setRawInputType(InputType.TYPE_NULL);
//        setInputType(InputType.TYPE_NULL);
            initOnClickListener();
        }
    }

    private void initOnClickListener() {
        setFocusable(false);
        setFocusableInTouchMode(false);
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(getText().toString())) {
                    if (float_label_anim_percentage != 1)
                        startAnimator(0, 1);
                } else if (float_label_anim_percentage != 0) {
                    startAnimator(1, 0);
                }
            }
        });
    }

    private void startAnimator(float startValue, float endValue) {
        final ObjectAnimator animator = ObjectAnimator.ofFloat(this, "float_label_anim_percentage", startValue, endValue);
        animator.setInterpolator(new AccelerateInterpolator(3));
        animator.setDuration(ANIM_DURATION);
        post(new Runnable() {
            @Override
            public void run() {
                animator.start();
            }
        });
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        if (mListener == null) {
            mListener = l;
        } else {
            customizeListener = l;
        }
        super.setOnFocusChangeListener(mListener);
    }

    @Override
    public OnFocusChangeListener getOnFocusChangeListener() {
        if (customizeListener != null)
            return customizeListener;
        return super.getOnFocusChangeListener();
    }

    private void initTextWatcher() {
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                max_length_text_width = measureTextMaxLength();
                boolean error = false;
                if (validatorList != null) {
                    for (RegexValidator regex : validatorList) {
                        if (regex != null) {
                            String error_msg = regex.getError_message(s);
                            if (error_msg != null) {
                                setError(error_msg);
                                error = true;
                                break;
                            }
                        }
                    }
                }
                if (!error) {
                    setError(null);
                    error_percentage = 0;
                }
                if (!hasFocus && !TextUtils.isEmpty(s)) {
                    startAnimator(0, 1);
                }
            }
        });
    }

    private int dp2px(float dpValue) {
        return (int) (0.5f + dpValue * getResources().getDisplayMetrics().density);
    }

    private int sp2Px(float spValue) {
        return (int) (spValue * getResources().getDisplayMetrics().scaledDensity);
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        this.padding_left = (short) left;
        this.padding_top = (short) top;
        this.padding_right = (short) right;
        this.padding_bottom = (short) bottom;
        super.setPadding(left, top + label_vertical_margin + (int) label_text_size, right + getClearBtnModePadding(),
                bottom + divider_stroke_width + divider_vertical_margin + (!error_disabled ? (int) (error_text_size * 1.2f) + (divider_vertical_margin << 1) : 0));
    }

    private int getClearBtnModePadding() {
        return (enable_clear_btn ? clear_btn_size + (clear_btn_horizontal_margin << 1) : 0);
    }

    private void updatePadding() {
        setPadding(padding_left, padding_top, padding_right, padding_bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final int width = getWidth();
        labelPaint.setColor(hasFocus ? highlight_color : hint_text_color);
        final float current_text_size = hint_text_size + (label_text_size - hint_text_size) * float_label_anim_percentage;
        labelPaint.setTextSize(current_text_size);

        final int scrollX = getScrollX();
        if (getText().length() > 0) {
            labelPaint.setAlpha((int) (255 * float_label_anim_percentage));
        } else {
            labelPaint.setAlpha(255);
        }

        final int label_paint_dy = (int) (padding_top + label_text_size + current_text_size * (1 - float_label_anim_percentage) * .93f);

        if (label != null)
            drawSpannableString(canvas, label, labelPaint, scrollX + label_horizontal_margin, label_paint_dy);

        final int divider_y = (int) (padding_top + label_text_size + label_vertical_margin + text_part_height * getLineCount() + (divider_stroke_width >> 1) + divider_vertical_margin);
        if (!is_error) {
            dividerPaint.setColor(hasFocus ? highlight_color : divider_color);
        } else {
            dividerPaint.setColor(error_color);
            final int error_paint_dy = (int) (divider_y + error_text_size + divider_vertical_margin);
            final float error_text_width = errorPaint.measureText(error.toString());
            final int hint_repeat_space_width = width / 3;
            final float max_dx = hint_repeat_space_width + error_text_width;
            final int start_x = error_horizontal_margin - (int) (max_dx * error_percentage) + scrollX;
            errorPaint.setColor(error_color);
            if (errorAnimator != null) {
                if (error_horizontal_margin > 0 && errorPaint.getShader() == null) {
                    final float margin_ratio = (float) error_horizontal_margin / width;
                    final float gradient_ratio = .025f;
                    LinearGradient shader = new LinearGradient(0, 0, width, 0, new int[]{0, error_color, error_color, 0},
                            new float[]{margin_ratio, margin_ratio + gradient_ratio, 1 - margin_ratio - gradient_ratio, 1 - margin_ratio}, Shader.TileMode.CLAMP);
                    errorPaint.setShader(shader);
                } else if (error_horizontal_margin == 0) {
                    errorPaint.setShader(null);
                }
            }
            RectF widget_layer_rect = new RectF(0, 0, width + scrollX,
                    getHeight());
            canvas.saveLayer(widget_layer_rect, new Paint(), Canvas.ALL_SAVE_FLAG);
            drawSpannableString(canvas, error, errorPaint, start_x, error_paint_dy);
            if (start_x < 0 && start_x + max_dx < width) {
                drawSpannableString(canvas, error, errorPaint, (int) (start_x + max_dx), error_paint_dy);
            }
            if (max_length_text_width > 0) {
                Paint paint = new Paint();
                paint.setColor(Color.WHITE);
                RectF rect = new RectF(width + scrollX - max_length_text_width - error_horizontal_margin, divider_y, width + scrollX,
                        getHeight());
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                canvas.drawRect(rect, paint);
                paint.setXfermode(null);
                canvas.restore();
            }
        }
        canvas.drawLine(scrollX, divider_y, width + scrollX, divider_y, dividerPaint);
        if (hasFocus || show_clear_button_without_focus) {
            drawClearBtn(canvas, scrollX);
        }
        if (show_max_length)
            drawMaxLength(canvas, width + scrollX, divider_y + error_text_size + divider_vertical_margin);
    }

    private void drawSpannableString(final Canvas canvas, CharSequence hint, final TextPaint paint, final int start_x, final int start_y) {
        // draw each span one at a time
        int next;
        float xStart = start_x;
        float xEnd;

        if (paint != errorPaint)
            hint = TextUtils.ellipsize(hint, paint, getWidth() - padding_left - padding_right - label_horizontal_margin - getClearBtnModePadding(), TextUtils.TruncateAt.END);

        if (hint instanceof SpannableString) {
            SpannableString spannableString = (SpannableString) hint;
            for (int i = 0; i < spannableString.length(); i = next) {

                // find the next span transition
                next = spannableString.nextSpanTransition(i, spannableString.length(), CharacterStyle.class);

                // measure the length of the span
                xEnd = xStart + paint.measureText(spannableString, i, next);

                // draw the highlight (background color) first
                BackgroundColorSpan[] bgSpans = spannableString.getSpans(i, next, BackgroundColorSpan.class);
                if (bgSpans.length > 0) {
                    Paint mHighlightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    mHighlightPaint.setColor(bgSpans[0].getBackgroundColor());
                    canvas.drawRect(xStart, start_y + paint.getFontMetrics().top, xEnd, start_y + paint.getFontMetrics().bottom, mHighlightPaint);
                }

                // draw the text with an optional foreground color
                ForegroundColorSpan[] fgSpans = spannableString.getSpans(i, next, ForegroundColorSpan.class);
                if (fgSpans.length > 0) {
                    int saveColor = paint.getColor();
                    paint.setColor(fgSpans[0].getForegroundColor());
                    canvas.drawText(spannableString, i, next, xStart, start_y, paint);
                    paint.setColor(saveColor);
                } else {
                    canvas.drawText(spannableString, i, next, xStart, start_y, paint);
                }

                xStart = xEnd;
            }
        } else {
            canvas.drawText(hint, 0, hint.length(), xStart, start_y, paint);
        }
    }

    private void drawClearBtn(final Canvas canvas, final int scrollX) {
        if (enable_clear_btn && getText().length() > 0) {
            if (clear_btn_bitmap == null) {
                final int alpha = (int) (((clear_btn_color >> 24) & 0xFF) * clear_paint_alpha_ratio);
                final int color = (alpha << 24) + (clear_btn_color & 0x00FFFFFF);
                clearButtonPaint.setColor(color);
                String spanned = Html.fromHtml(uni_code).toString();
                if (bounds == null)
                    bounds = new Rect();
                clearButtonPaint.getTextBounds(spanned, 0, spanned.length(), bounds);
                canvas.drawText(spanned, getWidth() - padding_right + scrollX - (clear_btn_size + clearButtonPaint.measureText(spanned)) * .5f - clear_btn_horizontal_margin,
                        padding_top + label_text_size + ((label_vertical_margin + bounds.height() + text_part_height + divider_vertical_margin) >> 1), clearButtonPaint);
            } else {
                clearButtonPaint.setAlpha((int) (clear_paint_alpha_ratio * 255));
                canvas.drawBitmap(clear_btn_bitmap, getWidth() - padding_right + scrollX - clear_btn_size - clear_btn_horizontal_margin,
                        padding_top + label_text_size + (label_vertical_margin + hint_text_size + divider_vertical_margin - bitmap_height) / 2, clearButtonPaint);
            }
        }
    }

    private int measureTextMaxLength() {
        if (max_length <= 0)
            return 0;
        if (show_max_length) {
            final int text_length = getText().length();
            maxLengthPaint.setTextSize(error_text_size);
            StringBuilder length_str_builder = new StringBuilder();
            length_str_builder.append(text_length).append("/").append(max_length);
            String length_str = length_str_builder.toString();
            int width = Math.round(maxLengthPaint.measureText(length_str));
            return width;
        } else {
            return 0;
        }
    }

    private void drawMaxLength(final Canvas canvas, final int dx, final float dy) {
        if (max_length_text_width == 0)
            return;
        maxLengthPaint.setColor(text_length_display_color);
        final int text_length = getText().length();
        StringBuilder length_str_builder = new StringBuilder();
        length_str_builder.append(text_length).append("/").append(max_length);
        String length_str = length_str_builder.toString();
        canvas.drawText(length_str, dx - max_length_text_width - padding_right, dy, maxLengthPaint);
    }

    final private void setFloat_label_anim_percentage(float float_label_anim_percentage) {
        this.float_label_anim_percentage = float_label_anim_percentage;
        postInvalidate();
    }

    public float getLabel_text_size() {
        return label_text_size;
    }

    public void setLabel_text_size(float label_text_size) {
        this.label_text_size = label_text_size;
        updatePadding();
    }

    public void setError_text_size(float error_text_size) {
        this.error_text_size = error_text_size;
        errorPaint.setTextSize(error_text_size);
        updatePadding();
    }

    public float getError_text_size() {
        return error_text_size;
    }

    public void setLabelMargins(int horizontal_margin, int vertical_margin) {
        this.label_horizontal_margin = (short) horizontal_margin;
        this.label_vertical_margin = (short) vertical_margin;
        updatePadding();
    }

    public void setThickness(int thickness) {
        this.divider_stroke_width = (short) thickness;
        dividerPaint.setStrokeWidth(divider_stroke_width);
        updatePadding();
    }

    public int getThickness() {
        return divider_stroke_width;
    }

    public void setErrorMargin(int horizontal_margin) {
        this.error_horizontal_margin = (short) horizontal_margin;
        updatePadding();
    }

    public void setDivider_vertical_margin(int divider_vertical_margin) {
        this.divider_vertical_margin = (short) divider_vertical_margin;
        updatePadding();
    }

    public int getDivider_vertical_margin() {
        return divider_vertical_margin;
    }

    public CharSequence getLabel() {
        return label;
    }


    public void setLabel(CharSequence hint) {
        this.label = hint;
        invalidate();
    }

    public void setAnimDuration(int ANIM_DURATION) {
        if (ANIM_DURATION < 0)
            ANIM_DURATION = 800;
        this.ANIM_DURATION = (short) ANIM_DURATION;
    }

    public short getAnimDuration() {
        return ANIM_DURATION;
    }

    public void setErrorAnimDuration(int ERROR_ANIM_DURATION) {
        if (ERROR_ANIM_DURATION < 0)
            ERROR_ANIM_DURATION = 8000;
        this.ERROR_ANIM_DURATION_PER_WIDTH = (short) ERROR_ANIM_DURATION;
    }

    public short getErrorAnimDuration() {
        return ERROR_ANIM_DURATION_PER_WIDTH;
    }

    @Override
    public void setHighlightColor(int color) {
        this.highlight_color = color;
        super.setHighlightColor(color);
    }

    @Override
    public int getHighlightColor() {
        return highlight_color;
    }

    public int getHint_text_color() {
        return hint_text_color;
    }

    public void setHint_text_color(int hint_text_color) {
        this.hint_text_color = hint_text_color;
    }

    public int getError_color() {
        return error_color;
    }

    public void setError_color(int error_color) {
        this.error_color = error_color;
    }

    public CharSequence getError() {
        return error;
    }

    public void setError(CharSequence error) {
        if (error_disabled) {
            return;
        }
        this.is_error = !TextUtils.isEmpty(error);
        this.error = error;
        if (is_error) {
            if (getWidth() > 0) {
                startErrorAnimation();
            } else {
                startErrorAnimation();
            }
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    if (errorAnimator != null) {
                        errorAnimator.cancel();
                        errorAnimator = null;
                    }
                }
            });
        }
        invalidate();
    }

    private void startErrorAnimation() {
        final float error_length = errorPaint.measureText(error.toString());
        int w = View.MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        measure(w, h);
        final int width = getWidth() > 0 ? getWidth() : getMeasuredWidth();
        int max_length_width = 0;
        if (show_max_length) {
            max_length_text_width = measureTextMaxLength();
        }
        if (error_length > width - (error_horizontal_margin << 1) - max_length_width) {
            error_percentage = 0;
            if (errorAnimator == null)
                errorAnimator = ObjectAnimator.ofFloat(this, "error_percentage", 0, 1);
            errorAnimator.setRepeatCount(ValueAnimator.INFINITE);
            errorAnimator.setRepeatMode(ValueAnimator.RESTART);
            errorAnimator.setStartDelay(ANIM_DURATION);
            short duration = (short) (ERROR_ANIM_DURATION_PER_WIDTH * error_length / width);
            if (duration < 0)
                duration = 8000;
            errorAnimator.setDuration(duration);
            post(new Runnable() {
                @Override
                public void run() {
                    if (errorAnimator != null)
                        errorAnimator.start();
                }
            });
        } else {
            error_percentage = 0;
        }
    }

    private final void setError_percentage(float error_percentage) {
        this.error_percentage = error_percentage;
        invalidate();
    }

    @Override
    public void setTextSize(float size) {
        hint_text_size = size;
        text_part_height = (short) (Math.round(hint_text_size) * 1.2f);
        super.setTextSize(size);
    }

    @Override
    public void setTextSize(int unit, float size) {
        Context c = getContext();
        Resources r = c.getResources();
        hint_text_size = TypedValue.applyDimension(unit, size, r.getDisplayMetrics());
        text_part_height = (short) (Math.round(hint_text_size) * 1.2f);
        super.setTextSize(unit, size);
    }

    public int getDivider_color() {
        return divider_color;
    }

    public void setDivider_color(int divider_color) {
        this.divider_color = divider_color;
    }

    public void setValidatorList(List<RegexValidator> list) {
        if (list != null) {
            if (validatorList == null)
                validatorList = new ArrayList<>();
            else
                validatorList.clear();
            validatorList.addAll(list);
        }
    }

    public List<RegexValidator> getValidatorList() {
        return validatorList;
    }

    public void addValidator(RegexValidator validator) {
        if (validatorList == null)
            validatorList = new ArrayList<>();
        if (validator != null)
            validatorList.add(validator);
    }

    public boolean isError_disabled() {
        return error_disabled;
    }

    public void setError_disabled() {
        this.error_disabled = true;
        updatePadding();
    }

    public void setError_enabled() {
        this.error_disabled = false;
        updatePadding();
    }

    // enable default clear button
    public void enableClearBtn(boolean enable) {
        enable_clear_btn = enable;
        if (enable) {
            initClearBtn();
            clearButtonPaint.setTextSize(clear_btn_size);
            Typeface tf = Typeface.createFromAsset(getResources().getAssets(), "floating_label_edit_text_iconfont.ttf");
            clearButtonPaint.setTypeface(tf);
            clearButtonPaint.setColor(clear_btn_color);
            uni_code = "&#xe724;";
        } else {
            clearButtonPaint = null;
            bounds = null;
        }
        updatePadding();
    }

    // customize your clear button by ttf
    public void customizeClearBtn(Typeface typeface, String uni_code, int color, int clear_btn_size) {
        enable_clear_btn = true;
        initClearBtn();
        clearButtonPaint.setTextSize(clear_btn_size);
        clearButtonPaint.setTypeface(typeface);
        clearButtonPaint.setColor(color);
        this.uni_code = uni_code;
        this.clear_btn_color = color;
        this.clear_btn_size = (short) clear_btn_size;
        updatePadding();
    }

    public void customizeClearBtn(int drawableId, int clear_btn_width) {
        enable_clear_btn = true;
        this.clear_btn_size = (short) clear_btn_width;
        final Context context = getContext();

        final Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        final Resources resources = getResources();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap sampleBitmap = createBitmap(drawable, resources, drawableId, options);
        int sampleSize = 1;
        final int destinationWidth = clear_btn_width;
        int width = options.outWidth;
        int height = options.outHeight;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && drawable instanceof VectorDrawable) {
            width = drawable.getIntrinsicWidth();
            height = drawable.getIntrinsicHeight();
        }
        bitmap_height = (short) (height * destinationWidth / width);
        int destinationHeight = bitmap_height;
        if (height > destinationHeight || width > destinationWidth) {
            int halfHeight = height >> 1;
            int halfWidth = width >> 1;
            while ((halfHeight / sampleSize) > destinationHeight && (halfWidth / sampleSize) > destinationWidth) {
                sampleSize *= 2;
            }
        }
        if (sampleBitmap != null)
            sampleBitmap.recycle();
        sampleBitmap = null;
        options.inSampleSize = sampleSize;
        options.inJustDecodeBounds = false;
        Bitmap oldBitmap = createBitmap(drawable, resources, drawableId, options);
        width = oldBitmap.getWidth();
        height = oldBitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleX = ((float) destinationWidth / width);
        float scaleY = ((float) destinationHeight / height);
        matrix.postScale(scaleX, scaleY);
        clear_btn_bitmap = new SoftReference<>(
                Bitmap.createBitmap(oldBitmap, 0, 0, width, height, matrix, true)).get();
        oldBitmap = null;
        matrix = null;
        initClearBtn();
        updatePadding();
    }

    private Bitmap createBitmap(Drawable drawable, Resources resources, int drawableId, BitmapFactory.Options options) {
        if (drawable instanceof BitmapDrawable) {
            return getBitmap(resources, drawableId, options);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && drawable instanceof VectorDrawable) {
            return getVectorBitmap((VectorDrawable) drawable);
        } else {
            throw new IllegalArgumentException("unsupported drawable type");
        }
    }

    private Bitmap getBitmap(Resources resources, int drawableId, BitmapFactory.Options options) {
        Bitmap bitmap = new SoftReference<>(
                BitmapFactory.decodeResource(resources, drawableId, options)).get();
        return bitmap;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private Bitmap getVectorBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    private final void initClearBtn() {
        if (clearButtonPaint == null) {
            clearButtonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }
    }

    @Override
    public void setSingleLine() {
        if (multiline_mode)
            return;
        super.setSingleLine();
    }

    public final void setMultiline_mode(boolean enable) {
        this.multiline_mode = enable;
        setSingleLine(!enable);
    }

    public void setClear_btn_color(int clear_btn_color) {
        this.clear_btn_color = clear_btn_color;
        invalidate();
    }

    public int getClear_btn_color() {
        return clear_btn_color;
    }

    public void setClear_btn_size(int clear_btn_size) {
        this.clear_btn_size = (short) clear_btn_size;
        invalidate();
    }

    public int getClear_btn_size() {
        return clear_btn_size;
    }

    public short getClear_btn_horizontal_margin() {
        return clear_btn_horizontal_margin;
    }

    public void setClear_btn_horizontal_margin(int clear_btn_horizontal_margin) {
        this.clear_btn_horizontal_margin = (short) clear_btn_horizontal_margin;
        invalidate();
    }

    //Even your edit text doesn't have focus, your clear button still show at right.
    public void showClearButtonWithoutFocus() {
        this.show_clear_button_without_focus = true;
        enableClearBtn(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (enable_clear_btn && (hasFocus || show_clear_button_without_focus)) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = event.getX();
                    downY = event.getY();
                    touch_clear_btn = touchClearBtn(downX, downY);
                    if (touch_clear_btn) {
                        fadeClearBtnIcon(true);
                        post(new Runnable() {
                            @Override
                            public void run() {
                                requestFocus();
                            }
                        });
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (touch_clear_btn && (Math.abs(downX - event.getX()) >= touchSlop || Math.abs(downY - event.getY()) >= touchSlop)) {
                        touch_clear_btn = false;
                        terminate_click = true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    boolean interrupt_action_up = touch_clear_btn || terminate_click;
                    if (touch_clear_btn) {
                        setText(null);
                    }
                    reset();
                    if (interrupt_action_up)
                        return false;
                    break;
                case MotionEvent.ACTION_CANCEL:
                    reset();
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    private boolean touchClearBtn(float x, float y) {
        final int width = getWidth();
        if (getMeasuredWidth() <= 0) {
            final int w = View.MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            final int h = w;
            measure(w, h);
        }
        final int right = width != 0 ? width : getMeasuredWidth();
        final int clear_btn_width = (int) (clear_btn_size + (clear_btn_horizontal_margin << 1) + getScaleX());
        final int clear_btn_top = (int) (padding_top + label_text_size);
        final int clear_btn_bottom = clear_btn_top + label_vertical_margin + text_part_height + divider_vertical_margin;
        if (x >= right - clear_btn_width && x <= right && y >= clear_btn_top && y <= clear_btn_bottom) {
            return true;
        }
        return false;
    }

    private final synchronized void fadeClearBtnIcon(boolean focus) {
        final float default_value = 1f;
        final float focus_value = 0.5f;
        final ObjectAnimator fadeClearBtnAnimator = ObjectAnimator.ofFloat(this, "clear_paint_alpha_ratio",
                focus ? default_value : focus_value, focus ? focus_value : default_value);
        fadeClearBtnAnimator.setDuration(500);
        if (fadeClearBtnAnimator != null)
            post(new Runnable() {
                @Override
                public void run() {
                    if (fadeClearBtnAnimator != null)
                        fadeClearBtnAnimator.start();
                }
            });
    }

    private void reset() {
        if (terminate_click || touch_clear_btn)
            fadeClearBtnIcon(false);
        terminate_click = false;
        touch_clear_btn = false;
    }

    private final void setClear_paint_alpha_ratio(float clear_paint_alpha_ratio) {
        this.clear_paint_alpha_ratio = clear_paint_alpha_ratio;
        postInvalidate();
    }

    @Override
    public void setFilters(InputFilter[] filters) {
        for (int i = 0; i < filters.length; i++) {
            InputFilter filter = filters[i];
            if (filter instanceof InputFilter.LengthFilter) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    max_length = ((InputFilter.LengthFilter) filter).getMax();
                } else {
                    try {
                        Field field = InputFilter.LengthFilter.class.getDeclaredField("mMax");
                        field.setAccessible(true);
                        max_length = (int) field.get(filter);
                    } catch (Exception e) {

                    }
                }
                break;
            }
        }
        super.setFilters(filters);
    }

    public void showMaxTextLength(boolean show) {
        this.show_max_length = show;
        invalidate();
    }

    public boolean isShowMaxTextLength() {
        return show_max_length;
    }

    public int getText_length_display_color() {
        return text_length_display_color;
    }

    public void setText_length_display_color(int text_length_display_color) {
        this.text_length_display_color = text_length_display_color;
        invalidate();
    }

    public boolean isError() {
        return is_error;
    }
}
