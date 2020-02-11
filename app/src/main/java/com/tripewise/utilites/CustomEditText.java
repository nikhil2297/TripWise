package com.tripewise.utilites;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tripewise.R;

public class CustomEditText extends TextInputLayout implements TextWatcher, TextView.OnEditorActionListener {
    private TextInputEditText editText;
    //Attributes For EditTextView
    private String text = "";
    private String hint = "";
    private float textSize = 18;
    private int textStyle;
    private int maxLines = 1;
    private int lines = 1;
    private int minLines = 0;
    private int ems;
    private int maxEms;
    private int minEms;
    private int gravity = Gravity.NO_GRAVITY;
    private int maxLength;
    private int visibility = 0;
    private int breakStrategy;
    private int inputType = InputType.TYPE_CLASS_TEXT;
    private int justification;
    private int hyphenationFrequency;
    private int textLetterSpacing;

    @ColorInt
    @ColorRes
    private int textColorLink;

    @ColorInt
    @ColorRes
    private int textHighlightColor;

    @ColorInt
    @ColorRes
    private int textColor = Color.BLACK;

    @ColorInt
    @ColorRes
    private int shadowColor = Color.TRANSPARENT;
    private int shadowDx = 0;
    private int shadowDy = 0;
    private int shadowRadius = 0;

    private Drawable drawableTop;
    private Drawable drawableBottom;
    private Drawable drawableLeft;
    private Drawable drawableRight;

    @DrawableRes
    private int textSelectHandleLeft;

    @DrawableRes
    private int textSelectHandleRight;

    @DrawableRes
    private int textSelectHandle;

    private int drawablePadding;
    private float extraLineSpacing;
    private int marqueeLimit;

    private boolean cursorVisible = true;
    private boolean scrollHorizontally = true;
    private boolean singleLine = true;
    private boolean enabled = true;
    private boolean selectOnFocus;
    private boolean includeFontPadding;
    private boolean linksClickable;
    private boolean freezesText;
    private boolean textIsSelectable;
    private boolean elegentTextHeight;

    private TextListener textListener;
    private TextEditorActionListener editorActionListener;
    private OnTextChangeListener onTextChangeListener;

    public CustomEditText(@NonNull Context context) {
        this(context, null);
    }

    public CustomEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.style.Widget_MaterialComponents_TextInputLayout_OutlinedBox);
    }

    public CustomEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        setWillNotDraw(false);
        editText = new TextInputEditText(getContext());
        initAttributes(context, attrs, defStyleAttr);
        createEditBox(editText);
    }

    private void createEditBox(TextInputEditText editText) {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        editText.setLayoutParams(layoutParams);
        editText.setTextSize(textSize);
        editText.setHint(hint);
        editText.setGravity(gravity);
        editText.setInputType(inputType);
        editText.setVisibility(visibility);
        editText.setMaxLines(maxLines);
        editText.setMinLines(minLines);
        editText.setLines(lines);
        editText.setTextColor(textColor);
        editText.setShadowLayer(shadowRadius, shadowDx, shadowDy, shadowColor);
        editText.setCursorVisible(cursorVisible);
        editText.setHorizontallyScrolling(scrollHorizontally);
        editText.setSingleLine(singleLine);
        editText.setEnabled(enabled);

        if (maxLength != 0){
            InputFilter[] filters = new InputFilter[1];
            filters[0] = new InputFilter.LengthFilter(maxLength);

            editText.setFilters(filters);
        }

        editText.addTextChangedListener(this);
        editText.setOnEditorActionListener(this);

        if (this.getId() != 0) {
            editText.setId(this.getId());
        }

        addView(editText);
    }

    private void initAttributes(Context context, AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomEditText, defStyleAttr, R.style.Widget_AppCompat_TextView);
            text = ta.getString(R.styleable.CustomEditText_text);
            hint = ta.getString(R.styleable.CustomEditText_hint);
            textColor = ta.getColor(R.styleable.CustomEditText_textColor, textColor);
            textColorLink = ta.getColor(R.styleable.CustomEditText_textColorLink, textColorLink);
            textHighlightColor = ta.getColor(R.styleable.CustomEditText_textColorHighlight, textHighlightColor);
            textSize = (ta.getDimension(R.styleable.CustomEditText_textSize, textSize)) / getResources().getDisplayMetrics().scaledDensity;
            textIsSelectable = ta.getBoolean(R.styleable.CustomEditText_textIsSelectable, false);
            textLetterSpacing = ta.getInt(R.styleable.CustomEditText_letterSpacing, textLetterSpacing);
            maxLines = ta.getInt(R.styleable.CustomEditText_maxLines, maxLines);
            lines = ta.getInt(R.styleable.CustomEditText_lines, minLines);
            minLines = ta.getInt(R.styleable.CustomEditText_minLines, minLines);
            ems = ta.getInt(R.styleable.CustomEditText_ems, ems);
            maxEms = ta.getInt(R.styleable.CustomEditText_maxEms, maxEms);
            minEms = ta.getInt(R.styleable.CustomEditText_minEms, minEms);
            gravity = ta.getInt(R.styleable.CustomEditText_gravity, gravity);
            maxLength = ta.getInt(R.styleable.CustomEditText_maxLength, maxLength);
            visibility = ta.getInt(R.styleable.CustomEditText_visibilty, visibility);
            breakStrategy = ta.getInt(R.styleable.CustomEditText_breakStrategy, breakStrategy);
            inputType = ta.getInt(R.styleable.CustomEditText_etInputType, inputType);
            justification = ta.getInt(R.styleable.CustomEditText_etJustificationMode, justification);
            hyphenationFrequency = ta.getInt(R.styleable.CustomEditText_hyphenationFrequency, hyphenationFrequency);
            drawablePadding = ta.getInt(R.styleable.CustomEditText_drawablePadding, drawablePadding);
            extraLineSpacing = ta.getFloat(R.styleable.CustomEditText_lineSpacingExtra, extraLineSpacing);
            marqueeLimit = ta.getInt(R.styleable.CustomEditText_marqueeRepeatLimit, marqueeLimit);
            shadowDx = ta.getInt(R.styleable.CustomEditText_shadowDx, shadowDx);
            shadowDy = ta.getInt(R.styleable.CustomEditText_shadowDy, shadowDy);
            shadowRadius = ta.getInt(R.styleable.CustomEditText_shadowRadius, shadowRadius);

            cursorVisible = ta.getBoolean(R.styleable.CustomEditText_cursorVisible, true);
            scrollHorizontally = ta.getBoolean(R.styleable.CustomEditText_scrollHorizontally, true);
            singleLine = ta.getBoolean(R.styleable.CustomEditText_singleLine, singleLine);
            enabled = ta.getBoolean(R.styleable.CustomEditText_enabled, true);
            selectOnFocus = ta.getBoolean(R.styleable.CustomEditText_selectAllOnFocus, true);
            includeFontPadding = ta.getBoolean(R.styleable.CustomEditText_includeFontPadding, false);
            linksClickable = ta.getBoolean(R.styleable.CustomEditText_linksClickable, true);
            freezesText = ta.getBoolean(R.styleable.CustomEditText_freezesText, false);
            textIsSelectable = ta.getBoolean(R.styleable.CustomEditText_textIsSelectable, true);
            elegentTextHeight = ta.getBoolean(R.styleable.CustomEditText_elegantTextHeight, false);

            textSelectHandle = ta.getInt(R.styleable.CustomEditText_textSelectHandle, textSelectHandle);
            textSelectHandleLeft = ta.getInt(R.styleable.CustomEditText_textSelectHandleLeft, textSelectHandleLeft);
            textSelectHandleRight = ta.getInt(R.styleable.CustomEditText_textSelectHandleRight, textSelectHandleRight);
            shadowColor = ta.getColor(R.styleable.CustomEditText_shadowColor, shadowColor);
            drawableTop = ta.getDrawable(R.styleable.CustomEditText_drawableTop);
            drawableBottom = ta.getDrawable(R.styleable.CustomEditText_drawableBottom);
            drawableLeft = ta.getDrawable(R.styleable.CustomEditText_drawableLeft);
            drawableRight = ta.getDrawable(R.styleable.CustomEditText_drawableRight);
            ta.recycle();
        }
    }

    public void addTextChangeListener(TextListener listener) {
        this.textListener = listener;
    }

    public void setOnEditorActionListiner(TextEditorActionListener listener) {
        this.editorActionListener = listener;
    }

    public void addOnTextChangeListener(OnTextChangeListener onTextChangeListener){
        this.onTextChangeListener = onTextChangeListener;
    }

    public String getText() {
        return editText.getText().toString();
    }

    public void setText(String text) {
        this.text = text;
        editText.setText(text);
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
        editText.setHint(hint);
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        editText.setTextSize(textSize);
    }

    public int getMaxLines() {
        return maxLines;
    }

    public void setMaxLines(int maxLines) {
        this.maxLines = maxLines;
        editText.setMaxLines(maxLines);
    }

    public int getLines() {
        return lines;
    }

    public void setLines(int lines) {
        this.lines = lines;
        editText.setLines(lines);
    }

    public int getMinLines() {
        return minLines;
    }

    public void setMinLines(int minLines) {
        this.minLines = minLines;
        editText.setMinLines(minLines);
    }

    public int getEms() {
        return ems;
    }

    public void setEms(int ems) {
        this.ems = ems;
        editText.setEms(ems);
    }

    public int getMaxEms() {
        return maxEms;
    }

    public void setMaxEms(int maxEms) {
        this.maxEms = maxEms;
        editText.setMaxEms(maxEms);
    }

    public int getMinEms() {
        return minEms;
    }

    public void setMinEms(int minEms) {
        this.minEms = minEms;
        editText.setMinEms(minEms);
    }

    public int getGravity() {
        return gravity;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
        editText.setGravity(gravity);
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
        editText.setMaxWidth(maxLength);
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
        editText.setVisibility(visibility);
    }

    public int getBreakStrategy() {
        return breakStrategy;
    }

    public void setBreakStrategy(int breakStrategy) {
        this.breakStrategy = breakStrategy;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            editText.setBreakStrategy(breakStrategy);
        }
    }

    public int getInputType() {
        return inputType;
    }

    public void setInputType(int inputType) {
        this.inputType = inputType;
        editText.setInputType(inputType);
    }

    public int getJustification() {
        return justification;
    }

    public void setJustification(int justification) {
        this.justification = justification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            editText.setJustificationMode(justification);
        }
    }

    public int getHyphenationFrequency() {
        return hyphenationFrequency;
    }

    public void setHyphenationFrequency(int hyphenationFrequency) {
        this.hyphenationFrequency = hyphenationFrequency;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            editText.setHyphenationFrequency(hyphenationFrequency);
        }
    }

    public int getTextLetterSpacing() {
        return textLetterSpacing;
    }

    public void setTextLetterSpacing(int textLetterSpacing) {
        this.textLetterSpacing = textLetterSpacing;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            editText.setLetterSpacing(textLetterSpacing);
        }
    }

    public boolean getElegentTextHeight() {
        return elegentTextHeight;
    }

    public void setElegentTextHeight(boolean elegentTextHeight) {
        this.elegentTextHeight = elegentTextHeight;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            editText.setElegantTextHeight(elegentTextHeight);
        }
    }

    public int getTextColorLink() {
        return textColorLink;
    }

    public void setTextColorLink(int textColorLink) {
        this.textColorLink = textColorLink;
        editText.setLinkTextColor(textColorLink);
    }

    public int getTextHighlightColor() {
        return textHighlightColor;
    }

    public void setTextHighlightColor(int textHighlightColor) {
        this.textHighlightColor = textHighlightColor;
        editText.setHighlightColor(textHighlightColor);
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        editText.setTextColor(textColor);
    }

    public void setShadowLayer(int radius, int dx, int dy, int color) {
        this.shadowRadius = radius;
        this.shadowDx = dx;
        this.shadowDy = dy;
        this.shadowColor = color;
        editText.setShadowLayer(radius, dx, dy, color);
    }

    public void setCompoundDrawables(Drawable etDrawableLeft, Drawable etDrawableTop, Drawable etDrawableRight, Drawable etDrawableBottom) {
        this.drawableLeft = etDrawableLeft;
        this.drawableRight = etDrawableRight;
        this.drawableTop = etDrawableTop;
        this.drawableBottom = etDrawableBottom;
        editText.setCompoundDrawables(etDrawableLeft, etDrawableTop, etDrawableRight, etDrawableBottom);
    }

    public int getTextSelectHandleLeft() {
        return textSelectHandleLeft;
    }

    public void setTextSelectHandleLeft(int textSelectHandleLeft) {
        this.textSelectHandleLeft = textSelectHandleLeft;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            editText.setTextSelectHandleLeft(textSelectHandleLeft);
        }
    }

    public int getTextSelectHandleRight() {
        return textSelectHandleRight;
    }

    public void setTextSelectHandleRight(int textSelectHandleRight) {
        this.textSelectHandleRight = textSelectHandleRight;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            editText.setTextSelectHandleRight(textSelectHandleRight);
        }
    }

    public int getTextSelectHandle() {
        return textSelectHandle;
    }

    public void setTextSelectHandle(int textSelectHandle) {
        this.textSelectHandle = textSelectHandle;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            editText.setTextSelectHandle(textSelectHandle);
        }
    }

    public int getDrawablePadding() {
        return drawablePadding;
    }

    public void setDrawablePadding(int drawablePadding) {
        this.drawablePadding = drawablePadding;
        editText.setCompoundDrawablePadding(drawablePadding);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setdrawableTintMode(PorterDuff.Mode drawableTintMode) {
        editText.setCompoundDrawableTintMode(drawableTintMode);
    }

    public float getExtraLineSpacing() {
        return extraLineSpacing;
    }

    public void setExtraLineSpacing(float extraLineSpacing) {
        this.extraLineSpacing = extraLineSpacing;
        editText.setLineSpacing(extraLineSpacing, 1.0f);
    }

    public int getMarqueeLimit() {
        return marqueeLimit;
    }

    public void setMarqueeLimit(int marqueeLimit) {
        this.marqueeLimit = marqueeLimit;
        editText.setMarqueeRepeatLimit(marqueeLimit);
    }

    public boolean isCursorVisible() {
        return cursorVisible;
    }

    public void setCursorVisible(boolean cursorVisible) {
        this.cursorVisible = cursorVisible;
        editText.setCursorVisible(cursorVisible);
    }

    public boolean isScrollHorizontally() {
        return scrollHorizontally;
    }

    public void setScrollHorizontally(boolean scrollHorizontally) {
        this.scrollHorizontally = scrollHorizontally;
        editText.setHorizontallyScrolling(scrollHorizontally);
    }

    public boolean isSingleLine() {
        return singleLine;
    }

    public void setSingleLine(boolean singleLine) {
        this.singleLine = singleLine;
        editText.setSingleLine(singleLine);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setIsEnabled(boolean enabled) {
        this.enabled = enabled;
        editText.setEnabled(enabled);
    }

    public boolean isSelectOnFocus() {
        return selectOnFocus;
    }

    public void setSelectOnFocus(boolean selectOnFocus) {
        this.selectOnFocus = selectOnFocus;
        editText.setSelectAllOnFocus(selectOnFocus);
    }

    public boolean isIncludeFontPadding() {
        return includeFontPadding;
    }

    public void setIncludeFontPadding(boolean includeFontPadding) {
        this.includeFontPadding = includeFontPadding;
        editText.setIncludeFontPadding(includeFontPadding);
    }

    public boolean isLinksClickable() {
        return linksClickable;
    }

    public void setLinksClickable(boolean linksClickable) {
        this.linksClickable = linksClickable;
        editText.setLinksClickable(linksClickable);
    }

    public boolean isFreezesText() {
        return freezesText;
    }

    public void setFreezesText(boolean freezesText) {
        this.freezesText = freezesText;
        editText.setFreezesText(freezesText);
    }

    public boolean isTextIsSelectable() {
        return textIsSelectable;
    }

    public void setTextIsSelectable(boolean textIsSelectable) {
        this.textIsSelectable = textIsSelectable;
        editText.setTextIsSelectable(textIsSelectable);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (textListener != null) {
            textListener.beforeTextChanged(s, start, count, after);
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (textListener != null) {
            textListener.onTextChanged(s, start, before, count);
        }

        if (onTextChangeListener != null){
            onTextChangeListener.onTextChange(s, start, before, count);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (textListener != null) {
            textListener.afterTextChanged(s);
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        editorActionListener.onEditorAction(v, actionId, event);
        return false;
    }

    public interface TextListener {

        void beforeTextChanged(CharSequence s, int start, int count, int after);

        void afterTextChanged(Editable s);

        void onTextChanged(CharSequence s, int start, int before, int count);
    }

    public interface TextEditorActionListener {

        boolean onEditorAction(TextView v, int actionId, KeyEvent event);
    }

    public interface OnTextChangeListener {
        void onTextChange(CharSequence s, int start, int before, int count);
    }
}
