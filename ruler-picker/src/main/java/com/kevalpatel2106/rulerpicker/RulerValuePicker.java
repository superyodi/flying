/*
 * Copyright 2018 Keval Patel
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance wit
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
 *  the specific language governing permissions and limitations under the License.
 */

package com.kevalpatel2106.rulerpicker;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.CheckResult;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


/**
 * Created by Kevalpatel2106 on 29-Mar-2018.
 * <p>
 * <li>Diagram:</li>
 * Observable ScrollView
 * |------------------|---------------------\--/----------------------|------------------|<br/>
 * |                  |                      \/                       |                  |<br/>
 * |                  |                                               |                  |<br/>
 * |  Left Spacer     |                 RulerView                     |  Right Spacer    |<br/>
 * |                  |                                               |                  |<br/>
 * |                  |                                               |                  |<br/>
 * |------------------|-----------------------------------------------|------------------|<br/>
 *
 * @see <a href="https://github.com/dwfox/DWRulerView>Original Repo</a>
 */
public final class RulerValuePicker extends FrameLayout implements ObservableHorizontalScrollView.ScrollChangedListener {
    /**
     * Left side empty view to add padding to the ruler.
     */
    @SuppressWarnings("NullableProblems")
    @NonNull
    private View mLeftSpacer;
    /**
     * Right side empty view to add padding to the ruler.
     */
    @SuppressWarnings("NullableProblems")
    @NonNull
    private View mRightSpacer;
    /**
     * Ruler view with values.
     */
    @SuppressWarnings("NullableProblems")
    @NonNull
    private RulerView mRulerView;
    /**
     * {@link ObservableHorizontalScrollView}, that will host all three components.
     *
     * @see #mLeftSpacer
     * @see #mRightSpacer
     * @see #mRulerView
     */
    @SuppressWarnings("NullableProblems")
    @NonNull
    private ObservableHorizontalScrollView mHorizontalScrollView;
    @Nullable
    private RulerValuePickerListener mListener;
    @SuppressWarnings("NullableProblems")
    @NonNull
    private Paint mNotchPaint;
    @SuppressWarnings("NullableProblems")
    @NonNull
    private Path mNotchPath;
    private int mNotchColor = Color.WHITE;
    /**
     * Because selectValue can be called multiple times and each of them will cause a delayed selection - we rather keep the last value from the
     * caller and concentrate on displaying only it not middle random calls.
     */
    private int value;
    /**
     * If min / max values were set outside class
     */
    private boolean isMinMaxSet = false;
    /**
     * Public constructor.
     */
    public RulerValuePicker(@NonNull final Context context) {
        super(context);
        init(null);
    }
    /**
     * Public constructor.
     */
    public RulerValuePicker(@NonNull final Context context,
                            @Nullable final AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }
    /**
     * Public constructor.
     */
    public RulerValuePicker(@NonNull final Context context,
                            @Nullable final AttributeSet attrs,
                            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }
    /**
     * Public constructor.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RulerValuePicker(@NonNull final Context context,
                            @Nullable final AttributeSet attrs,
                            final int defStyleAttr,
                            final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    /**
     * Initialize the view and parse the {@link AttributeSet}.
     *
     * @param attributeSet {@link AttributeSet} to parse or null if no attribute parameters set.
     */
    private void init(@Nullable AttributeSet attributeSet) {
        //Add all the children
        addChildViews();
        if (attributeSet != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(attributeSet,
                    R.styleable.RulerValuePicker,
                    0,
                    0);
            try { //Parse params
                if (a.hasValue(R.styleable.RulerValuePicker_notch_color)) {
                    mNotchColor = a.getColor(R.styleable.RulerValuePicker_notch_color, Color.WHITE);
                }
                if (a.hasValue(R.styleable.RulerValuePicker_ruler_text_color)) {
                    setTextColor(a.getColor(R.styleable.RulerValuePicker_ruler_text_color, Color.WHITE));
                }
                if (a.hasValue(R.styleable.RulerValuePicker_ruler_text_size)) {
                    setTextSize((int) a.getDimension(R.styleable.RulerValuePicker_ruler_text_size, 14));
                }
                if (a.hasValue(R.styleable.RulerValuePicker_indicator_color)) {
                    setIndicatorColor(a.getColor(R.styleable.RulerValuePicker_indicator_color, Color.WHITE));
                }
                if (a.hasValue(R.styleable.RulerValuePicker_indicator_width)) {
                    setIndicatorWidth(a.getDimensionPixelSize(R.styleable.RulerValuePicker_indicator_width,
                            4));
                }
                if (a.hasValue(R.styleable.RulerValuePicker_indicator_interval)) {
                    setIndicatorIntervalDistance(a.getDimensionPixelSize(R.styleable.RulerValuePicker_indicator_interval,
                            4));
                }
                if (a.hasValue(R.styleable.RulerValuePicker_long_height_height_ratio)
                        || a.hasValue(R.styleable.RulerValuePicker_short_height_height_ratio)) {
                    setIndicatorHeight(a.getFraction(R.styleable.RulerValuePicker_long_height_height_ratio,
                            1, 1, 0.6f),
                            a.getFraction(R.styleable.RulerValuePicker_short_height_height_ratio,
                                    1, 1, 0.4f));
                }
                if (a.hasValue(R.styleable.RulerValuePicker_min_value) ||
                        a.hasValue(R.styleable.RulerValuePicker_max_value)) {
                    initializeMinMaxValue(a.getInteger(R.styleable.RulerValuePicker_min_value, 0),
                            a.getInteger(R.styleable.RulerValuePicker_max_value, 100));
                }
                if (a.hasValue(R.styleable.RulerValuePicker_long_indicator_step)){
                    int mLongIndicatorStep = a.getInteger(R.styleable.RulerValuePicker_long_indicator_step, 5);
                    if (mLongIndicatorStep < 1)
                        mLongIndicatorStep = 4; /*Fallback to default to prevent unexpected behaviour*/
                    setLongIndicatorStep(mLongIndicatorStep);
                }
            } finally {
                a.recycle();
            }
        }
        //Prepare the notch color.
        mNotchPaint = new Paint();
        prepareNotchPaint();
        mNotchPath = new Path();
    }

    /**
     * Create the paint for notch. This will
     */
    private void prepareNotchPaint() {
        mNotchPaint.setColor(mNotchColor);
        mNotchPaint.setStrokeWidth(5f);
        mNotchPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    /**
     * Programmatically add the children to the view.
     * <p>
     * <li>The main view contains the {@link android.widget.HorizontalScrollView}. That allows
     * {@link RulerView} to scroll horizontally.</li>
     * <li>{@link #mHorizontalScrollView} contains {@link LinearLayout} that will act as the container
     * to hold the children inside the horizontal view.</li>
     * <li>{@link LinearLayout} container will contain three children.
     * <ul><b>Left spacer:</b> Width of this view will be the half width of the view. This will add staring at the start of the ruler.</ul>
     * <ul><b>Right spacer:</b> Width of this view will be the half width of the view. This will add ending at the end of the ruler.</ul>
     * <ul><b>{@link RulerView}:</b> Ruler view will contain the ruler with indicator.</ul>
     * </li>
     */
    private void addChildViews() {
        mHorizontalScrollView = new ObservableHorizontalScrollView(getContext(), this);
        mHorizontalScrollView.setHorizontalScrollBarEnabled(false); //Don't display the scrollbar
        final LinearLayout rulerContainer = new LinearLayout(getContext());
        //Add left spacing to the container
        mLeftSpacer = new View(getContext());
        rulerContainer.addView(mLeftSpacer);
        //Add ruler to the container
        mRulerView = new RulerView(getContext());
        rulerContainer.addView(mRulerView);
        //Add right spacing to the container
        mRightSpacer = new View(getContext());
        rulerContainer.addView(mRightSpacer);
        //Add this container to the scroll view.
        mHorizontalScrollView.removeAllViews();
        mHorizontalScrollView.addView(rulerContainer);
        //Add scroll view to this view.
        removeAllViews();
        addView(mHorizontalScrollView);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Draw the top notch
        canvas.drawPath(mNotchPath, mNotchPaint);
    }

    @Override
    protected void onLayout(boolean isChanged, int left, int top, int right, int bottom) {
        super.onLayout(isChanged, left, top, right, bottom);
        if (isChanged) {
            final int width = getWidth();
            //Set width of the left spacer to the half of this view.
            final ViewGroup.LayoutParams leftParams = mLeftSpacer.getLayoutParams();
            leftParams.width = width / 2;
            mLeftSpacer.setLayoutParams(leftParams);
            //Set width of the right spacer to the half of this view.
            final ViewGroup.LayoutParams rightParams = mRightSpacer.getLayoutParams();
            rightParams.width = width / 2;
            mRightSpacer.setLayoutParams(rightParams);
            calculateNotchPath();
            invalidate();
        }
    }

    /**
     * Calculate notch path. Notch will be in the triangle shape at the top-center of this view.
     *
     * @see #mNotchPath
     */
    private void calculateNotchPath() {
        mNotchPath.reset();
        mNotchPath.moveTo(getWidth() / 2 - 30, 0);
        mNotchPath.lineTo(getWidth() / 2, 40);
        mNotchPath.lineTo(getWidth() / 2 + 30, 0);
    }

    /**
     * Scroll the ruler to the given value.
     *
     * @param value Value to select. Value must be between {@link #getMinValue()} and {@link #getMaxValue()}.
     *              If the value is less than {@link #getMinValue()}, {@link #getMinValue()} will be
     *              selected.If the value is greater than {@link #getMaxValue()}, {@link #getMaxValue()}
     *              will be selected.
     */
    public void selectValue(final int value) {
        //System.out.println("Save RulerValuePicker selectValue="+value+" called");
        this.value = value;
        selectValueDelayed(0);
    }

    /**
     * More precice method on returning the set value - usefull during rotation, etc.
     * @return
     */
    public int getValue(){
        return value;
    }

    /**
     * Scroll the ruler to the given value.
     * If value isn't reached yet - loop with delay 3 times till we manage to set the value ASAP.
     //* @param value Value to select. Value must be between {@link #getMinValue()} and {@link #getMaxValue()}.
     *              If the value is less than {@link #getMinValue()}, {@link #getMinValue()} will be
     *              selected.If the value is greater than {@link #getMaxValue()}, {@link #getMaxValue()}
     *              will be selected.
     * @param iteration - initial call should pass 0, the rest are done internally
     */
    private void selectValueDelayed(/*final int value, */ final int iteration){
        int valuesToScroll;
        if (value < mRulerView.getMinValue()) {
            valuesToScroll = 0;
        } else if (value > mRulerView.getMaxValue()) {
            valuesToScroll = mRulerView.getMaxValue() - mRulerView.getMinValue();
        } else {
            valuesToScroll = value - mRulerView.getMinValue();
        }
        mHorizontalScrollView.smoothScrollTo(
                (valuesToScroll+1) * mRulerView.getIndicatorIntervalWidth(), 0);//extra 1 for spacing which we artificially added in case the labels may wanna get drawn
        //we have all the condition in postDelay, because smoothScrollTo is asynchronious and will take time to finish
        mHorizontalScrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getCurrentValue() != value && //for the rest value selection
                        iteration < 3 || //loop protection with iteration from infinity
                        mHorizontalScrollView.getScrollX() == 0 //if minimum value is initially selected, but we actually have extra spaces (for the text) in front and back of the ruler, so it can't be 0.
                ) {
                    selectValueDelayed(iteration + 1);//we rise iteration for loop protection
                }
            }
        }, 150);//maybe MhorizontalScrollView will be initialized faster than 400ms?
    }

    /**
     * Not the best method to get current value as selection may be in progress
     * but it is a good method to check weather we finally got our value selected
     * and it is usefull during ruler initialization while smoothScrollTo will accept values
     * but in reality - there will be no changes till it's fully initialized.
     * @return Get the current selected value.
     */
    public int getCurrentValue() {
        int absoluteValue = mHorizontalScrollView.getScrollX() / mRulerView.getIndicatorIntervalWidth();
        int value = mRulerView.getMinValue() + absoluteValue - 1;//extra 1 for spacing which we artificially added in case the labels may wanna get drawn
        if (value > mRulerView.getMaxValue()) {
            return mRulerView.getMaxValue();
        } else if (value < mRulerView.getMinValue()) {
            return mRulerView.getMinValue();
        } else {
            return value;
        }
    }

    @Override
    public void onScrollChanged() {
        if (mListener != null) mListener.onIntermediateValueChange(getCurrentValue());
    }

    @Override
    public void onScrollStopped() {
        makeOffsetCorrection(mRulerView.getIndicatorIntervalWidth());
        if (mListener != null) {
            mListener.onValueChange(getCurrentValue());
        }
    }

    private void makeOffsetCorrection(final int indicatorInterval) {
        int offsetValue = mHorizontalScrollView.getScrollX() % indicatorInterval;
        /*
        Uncommenting the code in this function would make left / right boundaries strict on their values.
        But eventually i've noticed it's nicer when you're able to scroll around without
        these restrictions.
         */
        //int maxScrollX = indicatorInterval * getMaxValue();
        if (offsetValue < indicatorInterval / 2 ){//&&
            //mHorizontalScrollView.getScrollX() >= indicatorInterval && //this is our left padding of the indicator interval length
            //mHorizontalScrollView.getScrollX() <= maxScrollX ){//this is our right edge of the padding
            mHorizontalScrollView.scrollBy(-offsetValue, 0);
        } else {
            //if (mHorizontalScrollView.getScrollX() <= maxScrollX)//if we're not on the right edge of the padding
            mHorizontalScrollView.scrollBy(indicatorInterval - offsetValue, 0);
            //else
            //mHorizontalScrollView.scrollTo( maxScrollX, 0);//back to the right most max value
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.value = getCurrentValue();
        ss.minVal = getMinValue();//may change during runtime
        ss.maxVal = getMaxValue();//may change during runtime
        //System.out.println("Save RulerValuePicker onSave. "+getValue()+" or *"+getCurrentValue()+"* in ["+ss.minVal+"; "+ss.maxVal+"]");
        return ss;
    }
    //**********************************************************************************//
    //******************************** GETTERS/SETTERS *********************************//
    //**********************************************************************************//

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        //System.out.println("Save RulerValuePicker onRestore I. "+value+"  *"+getValue()+" / "+ getCurrentValue()+"* in ["+getMinValue()+"; "+getMaxValue()+"]");
        setMinMaxValue(ss.minVal, ss.maxVal);
        selectValue(ss.value);
        //System.out.println("Save RulerValuePicker onRestore II. "+value+" *"+getValue()+" / "+ getCurrentValue()+"* in ["+getMinValue()+"; "+getMaxValue()+"]");
    }

    /**
     * @param notchColorRes Color resource of the notch to display. Default color os {@link Color#WHITE}.
     * @see #setNotchColor(int)
     * @see #getNotchColor()
     */
    public void setNotchColorRes(@ColorRes final int notchColorRes) {
        setNotchColor(ContextCompat.getColor(getContext(), notchColorRes));
    }

    /**
     * @return Integer color of the notch. Default color os {@link Color#WHITE}.
     * @see #setNotchColor(int)
     * @see #setNotchColorRes(int)
     */
    @ColorInt
    public int getNotchColor() {
        return mNotchColor;
    }

    /**
     * @param notchColor Integer color of the notch to display. Default color os {@link Color#WHITE}.
     * @see #prepareNotchPaint()
     * @see #getNotchColor()
     */
    public void setNotchColor(@ColorInt final int notchColor) {
        mNotchColor = notchColor;
        prepareNotchPaint();
        invalidate();
    }

    /**
     * @return Color integer value of the ruler text color.
     * @see #setTextColor(int)
     * @see #setTextColorRes(int)
     */
    @CheckResult
    @ColorInt
    public int getTextColor() {
        return mRulerView.getTextColor();
    }

    /**
     * Set the color of the text to display on the ruler.
     *
     * @param color Color integer value.
     * @see #getTextColor()
     * @see RulerView#mTextColor
     */
    public void setTextColor(@ColorInt final int color) {
        mRulerView.setTextColor(color);
    }

    /**
     * Set the color of the text to display on the ruler.
     *
     * @param color Color resource id.
     * @see RulerView#mTextColor
     */
    public void setTextColorRes(@ColorRes final int color) {
        setTextColor(ContextCompat.getColor(getContext(), color));
    }

    /**
     * @return Size of the text of ruler in dp.
     * @see #setTextSize(int)
     * @see #setTextSizeRes(int)
     * @see RulerView#mTextColor
     */
    @CheckResult
    public float getTextSize() {
        return mRulerView.getTextSize();
    }

    /**
     * Set the size of the text to display on the ruler.
     *
     * @param dimensionDp Text size dimension in dp.
     * @see #getTextSize()
     * @see RulerView#mTextSize
     */
    public void setTextSize(final int dimensionDp) {
        mRulerView.setTextSize(dimensionDp);
    }

    /**
     * Set the size of the text to display on the ruler.
     *
     * @param dimension Text size dimension resource.
     * @see #getTextSize()
     * @see RulerView#mTextSize
     */
    public void setTextSizeRes(@DimenRes final int dimension) {
        setTextSize((int) getContext().getResources().getDimension(dimension));
    }

    /**
     * @return Color integer value of the indicator color.
     * @see #setIndicatorColor(int)
     * @see #setIndicatorColorRes(int)
     * @see RulerView#mIndicatorColor
     */
    @CheckResult
    @ColorInt
    public int getIndicatorColor() {
        return mRulerView.getIndicatorColor();
    }

    /**
     * Set the indicator color.
     *
     * @param color Color integer value.
     * @see #getIndicatorColor()
     * @see RulerView#mIndicatorColor
     */
    public void setIndicatorColor(@ColorInt final int color) {
        mRulerView.setIndicatorColor(color);
    }

    /**
     * Set the indicator color.
     *
     * @param color Color resource id.
     * @see #getIndicatorColor()
     * @see RulerView#mIndicatorColor
     */
    public void setIndicatorColorRes(@ColorRes final int color) {
        setIndicatorColor(ContextCompat.getColor(getContext(), color));
    }

    /**
     * @return Width of the indicator in pixels.
     * @see #setIndicatorWidth(int)
     * @see #setIndicatorWidthRes(int)
     * @see RulerView#mIndicatorWidthPx
     */
    @CheckResult
    public float getIndicatorWidth() {
        return mRulerView.getIndicatorWidth();
    }

    /**
     * Set the width of the indicator line in the ruler.
     *
     * @param widthPx Width in pixels.
     * @see #getIndicatorWidth()
     * @see RulerView#mIndicatorWidthPx
     */
    public void setIndicatorWidth(final int widthPx) {
        mRulerView.setIndicatorWidth(widthPx);
    }

    /**
     * @return Long indicator step
     * @see #setLongIndicatorStep(int)
     * @see RulerView#mLongIndicatorStep
     */
    public int getLongIndicatorStep(){
        return mRulerView.getLongIndicatorStep();
    }

    /**
     * Set a step for a long indictor to be drawn.
     * Every step a long indicator will be drawn.
     * @param step - Step in decimal
     * @see #getLongIndicatorStep()
     * @see RulerView#mLongIndicatorStep
     */
    public void setLongIndicatorStep(final int step){
        mRulerView.setLongIndicatorStep(step);
    }

    /**
     * Set the width of the indicator line in the ruler.
     *
     * @param width Dimension resource for indicator width.
     * @see #getIndicatorWidth()
     * @see RulerView#mIndicatorWidthPx
     */
    public void setIndicatorWidthRes(@DimenRes final int width) {
        setIndicatorWidth(getContext().getResources().getDimensionPixelSize(width));
    }

    /**
     * @return Get the minimum value displayed on the ruler.
     * @see #setMinMaxValue(int, int)
     * @see RulerView#mMinValue
     */
    @CheckResult
    public int getMinValue() {
        return mRulerView.getMinValue();
    }

    /**
     * @return Get the maximum value displayed on the ruler.
     * @see #setMinMaxValue(int, int)
     * @see RulerView#mMaxValue
     */
    @CheckResult
    public int getMaxValue() {
        return mRulerView.getMaxValue();
    }

    /**
     * Set the maximum value to display on the ruler. This will decide the range of values and number
     * of indicators that ruler will draw.
     *
     * @param minValue Value to display at the left end of the ruler. This can be positive, negative
     *                 or zero. Default minimum value is 0.
     * @param maxValue Value to display at the right end of the ruler. This can be positive, negative
     *                 or zero.This value must be greater than min value. Default minimum value is 100.
     * @see #getMinValue()
     * @see #getMaxValue()
     */
    public void setMinMaxValue(final int minValue, final int maxValue) {
        //System.out.println("Save RulerValuePicker setMinMax("+minValue+", "+maxValue+");");
        int oldDiff = mRulerView.getMaxValue() - mRulerView.getMinValue();
        int curVal = getCurrentValue();
        mRulerView.setValueRange(minValue, maxValue);
        if (isMinMaxSet && oldDiff != maxValue - minValue) {//it means we need to recalculate width
            mRulerView.triggerOnMeasure();
        }
        invalidate();
        if (curVal >= minValue && curVal <= maxValue)
            selectValue(curVal);//we select same old value on change if we can
        else
            selectValue(minValue);//we select minimum value once we're out of edges
        isMinMaxSet = true;
    }
    private void initializeMinMaxValue(final int minValue, final int maxValue){
        mRulerView.setValueRange(minValue, maxValue);
        invalidate();
        selectValue(minValue);
    }
    /**
     * @return Get distance between two indicator in pixels.
     * @see #setIndicatorIntervalDistance(int)
     * @see RulerView#mIndicatorInterval
     */
    @CheckResult
    public int getIndicatorIntervalWidth() {
        return mRulerView.getIndicatorIntervalWidth();
    }
    /**
     * Set the spacing between two vertical lines/indicators. Default value is 14 pixels.
     *
     * @param indicatorIntervalPx Distance in pixels. This cannot be negative number or zero.
     * @see RulerView#mIndicatorInterval
     */
    public void setIndicatorIntervalDistance(final int indicatorIntervalPx) {
        mRulerView.setIndicatorIntervalDistance(indicatorIntervalPx);
    }
    /**
     * @return Ratio of long indicator height to the ruler height.
     * @see #setIndicatorHeight(float, float)
     * @see RulerView#mLongIndicatorHeightRatio
     */
    @CheckResult
    public float getLongIndicatorHeightRatio() {
        return mRulerView.getLongIndicatorHeightRatio();
    }
    /**
     * @return Ratio of short indicator height to the ruler height.
     * @see #setIndicatorHeight(float, float)
     * @see RulerView#mShortIndicatorHeight
     */
    @CheckResult
    public float getShortIndicatorHeightRatio() {
        return mRulerView.getShortIndicatorHeightRatio();
    }
    /**
     * Set the height of the long and short indicators.
     *
     * @param longHeightRatio  Ratio of long indicator height to the ruler height. This value must
     *                         be between 0 to 1. The value should greater than {@link #getShortIndicatorHeightRatio()}.
     *                         Default value is 0.6 (i.e. 60%). If the value is 0, indicator won't
     *                         be displayed. If the value is 1, indicator height will be same as the
     *                         ruler height.
     * @param shortHeightRatio Ratio of short indicator height to the ruler height. This value must
     *                         be between 0 to 1. The value should less than {@link #getLongIndicatorHeightRatio()}.
     *                         Default value is 0.4 (i.e. 40%). If the value is 0, indicator won't
     *                         be displayed. If the value is 1, indicator height will be same as
     *                         the ruler height.
     * @see #getLongIndicatorHeightRatio()
     * @see #getShortIndicatorHeightRatio()
     */
    public void setIndicatorHeight(final float longHeightRatio,
                                   final float shortHeightRatio) {
        mRulerView.setIndicatorHeight(longHeightRatio, shortHeightRatio);
    }
    /**
     * Set the {@link RulerValuePickerListener} to get callbacks when the value changes.
     *
     * @param listener {@link RulerValuePickerListener}
     */
    public void setValuePickerListener(@Nullable final RulerValuePickerListener listener) {
        mListener = listener;
    }
    /**
     * User interface state that is stored by RulerView for implementing
     * {@link View#onSaveInstanceState}.
     */
    public static class SavedState extends BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }
                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
        private int value = 0;
        private int minVal = 0;
        private int maxVal = 0;
        SavedState(Parcelable superState) {
            super(superState);
        }
        private SavedState(Parcel in) {
            super(in);
            value = in.readInt();
            minVal = in.readInt();
            maxVal = in.readInt();
            //System.out.println("SaveState: savedState="+value+" in ["+minVal+"; "+maxVal+"]");
        }
        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(value);
            out.writeInt(minVal);
            out.writeInt(maxVal);
            //System.out.println("SaveState: writeToParcel="+value+" in ["+minVal+"; "+maxVal+"]");
        }
    }
}