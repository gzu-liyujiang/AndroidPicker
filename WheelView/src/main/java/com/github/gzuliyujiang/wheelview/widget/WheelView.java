/*
 * Copyright (c) 2016-present 贵州纳雍穿青人李裕江<1032694760@qq.com>
 *
 * The software is licensed under the Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *     http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 * PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package com.github.gzuliyujiang.wheelview.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.Px;
import androidx.annotation.StyleRes;

import com.github.gzuliyujiang.wheelview.R;
import com.github.gzuliyujiang.wheelview.annotation.CurtainCorner;
import com.github.gzuliyujiang.wheelview.annotation.ItemTextAlign;
import com.github.gzuliyujiang.wheelview.annotation.ScrollState;
import com.github.gzuliyujiang.wheelview.contract.OnWheelChangedListener;
import com.github.gzuliyujiang.wheelview.contract.TextProvider;
import com.github.gzuliyujiang.wheelview.contract.WheelFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * 滚轮控件。Adapted from https://github.com/florent37/SingleDateAndTimePicker/.../WheelPicker.java
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @see TextProvider
 * @see OnWheelChangedListener
 * @since 2019/5/8 11:11
 */
@SuppressWarnings({"unused"})
public class WheelView extends View implements Runnable {
    @Deprecated
    public static final int SCROLL_STATE_IDLE = ScrollState.IDLE;
    @Deprecated
    public static final int SCROLL_STATE_DRAGGING = ScrollState.DRAGGING;
    @Deprecated
    public static final int SCROLL_STATE_SCROLLING = ScrollState.SCROLLING;

    protected List<?> data = new ArrayList<>();
    protected WheelFormatter formatter;
    protected Object defaultItem;
    protected int visibleItemCount;
    protected int defaultItemPosition;
    protected int currentPosition;
    protected String maxWidthText;
    protected int textColor;
    protected int selectedTextColor;
    protected float textSize, selectedTextSize;
    protected boolean selectedTextBold;
    protected float indicatorSize;
    protected int indicatorColor;
    protected int curtainColor;
    protected int curtainCorner;
    protected float curtainRadius;
    protected int itemSpace;
    protected int textAlign;
    protected boolean sameWidthEnabled;
    protected boolean indicatorEnabled;
    protected boolean curtainEnabled;
    protected boolean atmosphericEnabled;
    protected boolean cyclicEnabled;
    protected boolean curvedEnabled;
    protected int curvedMaxAngle = 90;
    protected int curvedIndicatorSpace;

    private final Handler handler = new Handler();
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
    private final Scroller scroller;
    private VelocityTracker tracker;
    private OnWheelChangedListener onWheelChangedListener;
    private final Rect rectDrawn = new Rect();
    private final Rect rectIndicatorHead = new Rect();
    private final Rect rectIndicatorFoot = new Rect();
    private final Rect rectCurrentItem = new Rect();
    private final Camera camera = new Camera();
    private final Matrix matrixRotate = new Matrix();
    private final Matrix matrixDepth = new Matrix();
    private int lastScrollPosition;
    private int drawnItemCount;
    private int halfDrawnItemCount;
    private int textMaxWidth, textMaxHeight;
    private int itemHeight, halfItemHeight;
    private int halfWheelHeight;
    private int minFlingYCoordinate, maxFlingYCoordinate;
    private int wheelCenterXCoordinate, wheelCenterYCoordinate;
    private int drawnCenterXCoordinate, drawnCenterYCoordinate;
    private int scrollOffsetYCoordinate;
    private int lastPointYCoordinate;
    private int downPointYCoordinate;
    private final int minimumVelocity;
    private final int maximumVelocity;
    private final int touchSlop;
    private boolean isClick;
    private boolean isForceFinishScroll;

    public WheelView(Context context) {
        this(context, null);
    }

    public WheelView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.WheelStyle);
    }

    public WheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs, defStyleAttr, R.style.WheelDefault);
        initTextPaint();
        updateVisibleItemCount();
        scroller = new Scroller(context);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        minimumVelocity = configuration.getScaledMinimumFlingVelocity();
        maximumVelocity = configuration.getScaledMaximumFlingVelocity();
        touchSlop = configuration.getScaledTouchSlop();
        if (isInEditMode()) {
            setData(generatePreviewData());
        }
    }

    private void initTextPaint() {
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setFakeBoldText(false);
        paint.setStyle(Paint.Style.FILL);
    }

    public void setStyle(@StyleRes int style) {
        initAttrs(getContext(), null, R.attr.WheelStyle, style);
        initTextPaint();
        updatePaintTextAlign();
        computeTextWidthAndHeight();
        computeFlingLimitYCoordinate();
        computeIndicatorRect();
        computeCurrentItemRect();
        requestLayout();
        invalidate();
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        float density = context.getResources().getDisplayMetrics().density;
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WheelView, defStyleAttr, defStyleRes);
        visibleItemCount = typedArray.getInt(R.styleable.WheelView_wheel_visibleItemCount, 5);
        sameWidthEnabled = typedArray.getBoolean(R.styleable.WheelView_wheel_sameWidthEnabled, false);
        maxWidthText = typedArray.getString(R.styleable.WheelView_wheel_maxWidthText);
        textColor = typedArray.getColor(R.styleable.WheelView_wheel_itemTextColor, 0xFF888888);
        selectedTextColor = typedArray.getColor(R.styleable.WheelView_wheel_itemTextColorSelected, 0xFF000000);
        textSize = typedArray.getDimension(R.styleable.WheelView_wheel_itemTextSize, 15 * scaledDensity);
        selectedTextSize = typedArray.getDimension(R.styleable.WheelView_wheel_itemTextSizeSelected, textSize);
        selectedTextBold = typedArray.getBoolean(R.styleable.WheelView_wheel_itemTextBoldSelected, false);
        textAlign = typedArray.getInt(R.styleable.WheelView_wheel_itemTextAlign, ItemTextAlign.CENTER);
        itemSpace = typedArray.getDimensionPixelSize(R.styleable.WheelView_wheel_itemSpace, (int) (20 * density));
        cyclicEnabled = typedArray.getBoolean(R.styleable.WheelView_wheel_cyclicEnabled, false);
        indicatorEnabled = typedArray.getBoolean(R.styleable.WheelView_wheel_indicatorEnabled, true);
        indicatorColor = typedArray.getColor(R.styleable.WheelView_wheel_indicatorColor, 0xFFC9C9C9);
        indicatorSize = typedArray.getDimension(R.styleable.WheelView_wheel_indicatorSize, 1 * density);
        curvedIndicatorSpace = typedArray.getDimensionPixelSize(R.styleable.WheelView_wheel_curvedIndicatorSpace, (int) (1 * density));
        curtainEnabled = typedArray.getBoolean(R.styleable.WheelView_wheel_curtainEnabled, false);
        curtainColor = typedArray.getColor(R.styleable.WheelView_wheel_curtainColor, 0xFFFFFFFF);
        curtainCorner = typedArray.getInt(R.styleable.WheelView_wheel_curtainCorner, CurtainCorner.NONE);
        curtainRadius = typedArray.getDimension(R.styleable.WheelView_wheel_curtainRadius, 0);
        atmosphericEnabled = typedArray.getBoolean(R.styleable.WheelView_wheel_atmosphericEnabled, false);
        curvedEnabled = typedArray.getBoolean(R.styleable.WheelView_wheel_curvedEnabled, false);
        curvedMaxAngle = typedArray.getInteger(R.styleable.WheelView_wheel_curvedMaxAngle, 90);
        typedArray.recycle();
    }

    protected List<?> generatePreviewData() {
        List<String> data = new ArrayList<>();
        data.add("贵州穿青人");
        data.add("大定府羡民");
        data.add("不在五十六个民族之内");
        data.add("已识别待定民族");
        data.add("穿青山魈人马");
        data.add("李裕江");
        return data;
    }

    private void updateVisibleItemCount() {
        final int minCount = 2;
        if (visibleItemCount < minCount) {
            throw new ArithmeticException("Visible item count can not be less than " + minCount);
        }
        //可见条目只能是奇数个，设置可见条目时偶数个将自动矫正为奇数个
        int evenNumberFlag = 2;
        if (visibleItemCount % evenNumberFlag == 0) {
            visibleItemCount += 1;
        }
        drawnItemCount = visibleItemCount + 2;
        halfDrawnItemCount = drawnItemCount / 2;
    }

    private void computeTextWidthAndHeight() {
        textMaxWidth = textMaxHeight = 0;
        if (sameWidthEnabled) {
            textMaxWidth = (int) paint.measureText(formatItem(0));
        } else if (!TextUtils.isEmpty(maxWidthText)) {
            textMaxWidth = (int) paint.measureText(maxWidthText);
        } else {
            // 未指定最宽的文本，须遍历测量查找最宽的作为基准
            int itemCount = getItemCount();
            for (int i = 0; i < itemCount; ++i) {
                int width = (int) paint.measureText(formatItem(i));
                textMaxWidth = Math.max(textMaxWidth, width);
            }
        }
        Paint.FontMetrics metrics = paint.getFontMetrics();
        textMaxHeight = (int) (metrics.bottom - metrics.top);
    }

    public int getItemCount() {
        return data.size();
    }

    public <T> T getItem(int position) {
        final int size = data.size();
        if (size == 0) {
            return null;
        }
        int index = (position + size) % size;
        if (index >= 0 && index <= size - 1) {
            //noinspection unchecked
            return (T) data.get(index);
        }
        return null;
    }

    public int getPosition(Object item) {
        if (item == null) {
            return 0;
        }
        return data.indexOf(item);
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public <T> T getCurrentItem() {
        return getItem(currentPosition);
    }

    public int getVisibleItemCount() {
        return visibleItemCount;
    }

    public void setVisibleItemCount(@IntRange(from = 2) int count) {
        visibleItemCount = count;
        updateVisibleItemCount();
        requestLayout();
    }

    public boolean isCyclicEnabled() {
        return cyclicEnabled;
    }

    public void setCyclicEnabled(boolean isCyclic) {
        this.cyclicEnabled = isCyclic;
        computeFlingLimitYCoordinate();
        invalidate();
    }

    public void setOnWheelChangedListener(OnWheelChangedListener listener) {
        onWheelChangedListener = listener;
    }

    public void setFormatter(WheelFormatter formatter) {
        this.formatter = formatter;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> newData) {
        setData(newData, 0);
    }

    public void setData(List<?> newData, Object defaultValue) {
        setData(newData, findPosition(defaultValue));
    }

    public void setData(List<?> newData, int defaultPosition) {
        if (newData == null) {
            newData = new ArrayList<>();
        }
        data = newData;
        notifyDataSetChanged(defaultPosition);
    }

    public void setDefaultValue(Object value) {
        setDefaultPosition(findPosition(value));
    }

    public void setDefaultPosition(int position) {
        notifyDataSetChanged(position);
    }

    private int findPosition(Object value) {
        if (value == null) {
            return 0;
        }
        boolean found = false;
        int position = 0;
        for (Object item : data) {
            if (item == null) {
                continue;
            }
            if (item.equals(value)) {
                found = true;
                break;
            }
            if (formatter != null && formatter.formatItem(item).equals(formatter.formatItem(value))) {
                found = true;
                break;
            }
            if (item instanceof TextProvider) {
                String text = ((TextProvider) item).provideText();
                if (text.equals(value.toString())) {
                    found = true;
                    break;
                }
            }
            if (item.toString().equals(value.toString())) {
                found = true;
                break;
            }
            position++;
        }
        if (!found) {
            position = 0;
        }
        return position;
    }

    public boolean isSameWidthEnabled() {
        return sameWidthEnabled;
    }

    public void setSameWidthEnabled(boolean sameWidthEnabled) {
        this.sameWidthEnabled = sameWidthEnabled;
        computeTextWidthAndHeight();
        requestLayout();
        invalidate();
    }

    public String getMaxWidthText() {
        return maxWidthText;
    }

    public void setMaxWidthText(String text) {
        if (null == text) {
            throw new NullPointerException("Maximum width text can not be null!");
        }
        maxWidthText = text;
        computeTextWidthAndHeight();
        requestLayout();
        invalidate();
    }

    @ColorInt
    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(@ColorInt int color) {
        textColor = color;
        invalidate();
    }

    @ColorInt
    public int getSelectedTextColor() {
        return selectedTextColor;
    }

    public void setSelectedTextColor(@ColorInt int color) {
        selectedTextColor = color;
        computeCurrentItemRect();
        invalidate();
    }

    @Px
    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(@Px float size) {
        textSize = size;
        computeTextWidthAndHeight();
        requestLayout();
        invalidate();
    }

    @Px
    public float getSelectedTextSize() {
        return selectedTextSize;
    }

    public void setSelectedTextSize(@Px float size) {
        selectedTextSize = size;
        computeTextWidthAndHeight();
        requestLayout();
        invalidate();
    }

    public boolean getSelectedTextBold() {
        return selectedTextBold;
    }

    public void setSelectedTextBold(boolean bold) {
        this.selectedTextBold = bold;
        computeTextWidthAndHeight();
        requestLayout();
        invalidate();
    }

    @Px
    public int getItemSpace() {
        return itemSpace;
    }

    public void setItemSpace(@Px int space) {
        itemSpace = space;
        requestLayout();
        invalidate();
    }

    public boolean isIndicatorEnabled() {
        return indicatorEnabled;
    }

    public void setIndicatorEnabled(boolean indicatorEnabled) {
        this.indicatorEnabled = indicatorEnabled;
        computeIndicatorRect();
        invalidate();
    }

    @Px
    public float getIndicatorSize() {
        return indicatorSize;
    }

    public void setIndicatorSize(@Px float size) {
        indicatorSize = size;
        computeIndicatorRect();
        invalidate();
    }

    @ColorInt
    public int getIndicatorColor() {
        return indicatorColor;
    }

    public void setIndicatorColor(@ColorInt int color) {
        indicatorColor = color;
        invalidate();
    }

    @Px
    public int getCurvedIndicatorSpace() {
        return curvedIndicatorSpace;
    }

    public void setCurvedIndicatorSpace(@Px int space) {
        curvedIndicatorSpace = space;
        computeIndicatorRect();
        invalidate();
    }

    public boolean isCurtainEnabled() {
        return curtainEnabled;
    }

    public void setCurtainEnabled(boolean curtainEnabled) {
        this.curtainEnabled = curtainEnabled;
        if (curtainEnabled) {
            indicatorEnabled = false;
        }
        computeCurrentItemRect();
        invalidate();
    }

    @ColorInt
    public int getCurtainColor() {
        return curtainColor;
    }

    public void setCurtainColor(@ColorInt int color) {
        curtainColor = color;
        invalidate();
    }

    @CurtainCorner
    public int getCurtainCorner() {
        return curtainCorner;
    }

    public void setCurtainCorner(@CurtainCorner int curtainCorner) {
        this.curtainCorner = curtainCorner;
        invalidate();
    }

    @Px
    public float getCurtainRadius() {
        return curtainRadius;
    }

    public void setCurtainRadius(@Px float curtainRadius) {
        this.curtainRadius = curtainRadius;
        invalidate();
    }

    public boolean isAtmosphericEnabled() {
        return atmosphericEnabled;
    }

    public void setAtmosphericEnabled(boolean atmosphericEnabled) {
        this.atmosphericEnabled = atmosphericEnabled;
        invalidate();
    }

    public boolean isCurvedEnabled() {
        return curvedEnabled;
    }

    public void setCurvedEnabled(boolean isCurved) {
        this.curvedEnabled = isCurved;
        requestLayout();
        invalidate();
    }

    public int getCurvedMaxAngle() {
        return curvedMaxAngle;
    }

    public void setCurvedMaxAngle(int curvedMaxAngle) {
        this.curvedMaxAngle = curvedMaxAngle;
        requestLayout();
        invalidate();
    }

    @ItemTextAlign
    public int getTextAlign() {
        return textAlign;
    }

    public void setTextAlign(@ItemTextAlign int align) {
        textAlign = align;
        updatePaintTextAlign();
        computeDrawnCenterCoordinate();
        invalidate();
    }

    private void updatePaintTextAlign() {
        switch (textAlign) {
            case ItemTextAlign.LEFT:
                paint.setTextAlign(Paint.Align.LEFT);
                break;
            case ItemTextAlign.RIGHT:
                paint.setTextAlign(Paint.Align.RIGHT);
                break;
            case ItemTextAlign.CENTER:
            default:
                paint.setTextAlign(Paint.Align.CENTER);
                break;
        }
    }

    public Typeface getTypeface() {
        return paint.getTypeface();
    }

    public void setTypeface(Typeface typeface) {
        if (typeface == null) {
            return;
        }
        paint.setTypeface(typeface);
        computeTextWidthAndHeight();
        requestLayout();
        invalidate();
    }

    private void notifyDataSetChanged(int position) {
        position = Math.min(position, getItemCount() - 1);
        position = Math.max(position, 0);
        scrollOffsetYCoordinate = 0;
        defaultItem = getItem(position);
        defaultItemPosition = position;
        currentPosition = position;
        updatePaintTextAlign();
        //当设置了选中项文字加大加粗，此处重新计算文字宽高的话滑动会导致其他条目错位
        //computeTextWidthAndHeight();
        computeFlingLimitYCoordinate();
        computeIndicatorRect();
        computeCurrentItemRect();
        requestLayout();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        final int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        final int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        final int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        // Correct sizes of original content
        int resultWidth = textMaxWidth;
        int resultHeight = textMaxHeight * visibleItemCount + itemSpace * (visibleItemCount - 1);
        // Correct view sizes again if curved is enable
        if (curvedEnabled) {
            resultHeight = (int) (2 * resultHeight / Math.PI);
        }
        // Consideration padding influence the view sizes
        resultWidth += getPaddingLeft() + getPaddingRight();
        resultHeight += getPaddingTop() + getPaddingBottom();
        // Consideration sizes of parent can influence the view sizes
        resultWidth = measureSize(modeWidth, sizeWidth, resultWidth);
        resultHeight = measureSize(modeHeight, sizeHeight, resultHeight);
        setMeasuredDimension(resultWidth, resultHeight);
    }

    private int measureSize(int mode, int sizeExpect, int sizeActual) {
        int realSize;
        if (mode == MeasureSpec.EXACTLY) {
            realSize = sizeExpect;
        } else {
            realSize = sizeActual;
            if (mode == MeasureSpec.AT_MOST) {
                realSize = Math.min(realSize, sizeExpect);
            }
        }
        return realSize;
    }

    @Override
    protected void onSizeChanged(int w, int h, int ow, int oh) {
        // Set content region
        rectDrawn.set(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(),
                getHeight() - getPaddingBottom());
        // Get the center coordinates of content region
        wheelCenterXCoordinate = rectDrawn.centerX();
        wheelCenterYCoordinate = rectDrawn.centerY();
        // Correct item drawn center
        computeDrawnCenterCoordinate();
        halfWheelHeight = rectDrawn.height() / 2;
        itemHeight = rectDrawn.height() / visibleItemCount;
        halfItemHeight = itemHeight / 2;
        // Initialize fling max Y-coordinates
        computeFlingLimitYCoordinate();
        // Correct region of indicator
        computeIndicatorRect();
        // Correct region of current select item
        computeCurrentItemRect();
    }

    private void computeDrawnCenterCoordinate() {
        switch (textAlign) {
            case ItemTextAlign.LEFT:
                drawnCenterXCoordinate = rectDrawn.left;
                break;
            case ItemTextAlign.RIGHT:
                drawnCenterXCoordinate = rectDrawn.right;
                break;
            case ItemTextAlign.CENTER:
            default:
                drawnCenterXCoordinate = wheelCenterXCoordinate;
                break;
        }
        drawnCenterYCoordinate = (int) (wheelCenterYCoordinate -
                ((paint.ascent() + paint.descent()) / 2));
    }

    private void computeFlingLimitYCoordinate() {
        int currentItemOffset = defaultItemPosition * itemHeight;
        minFlingYCoordinate = cyclicEnabled ? Integer.MIN_VALUE
                : -itemHeight * (getItemCount() - 1) + currentItemOffset;
        maxFlingYCoordinate = cyclicEnabled ? Integer.MAX_VALUE : currentItemOffset;
    }

    private void computeIndicatorRect() {
        if (!indicatorEnabled) {
            return;
        }
        int indicatorSpace = curvedEnabled ? curvedIndicatorSpace : 0;
        int halfIndicatorSize = (int) (indicatorSize / 2f);
        int indicatorHeadCenterYCoordinate = wheelCenterYCoordinate + halfItemHeight + indicatorSpace;
        int indicatorFootCenterYCoordinate = wheelCenterYCoordinate - halfItemHeight - indicatorSpace;
        rectIndicatorHead.set(rectDrawn.left, indicatorHeadCenterYCoordinate - halfIndicatorSize,
                rectDrawn.right, indicatorHeadCenterYCoordinate + halfIndicatorSize);
        rectIndicatorFoot.set(rectDrawn.left, indicatorFootCenterYCoordinate - halfIndicatorSize,
                rectDrawn.right, indicatorFootCenterYCoordinate + halfIndicatorSize);
    }

    private void computeCurrentItemRect() {
        if (!curtainEnabled && selectedTextColor == 0/* Color.TRANSPARENT */) {
            return;
        }
        rectCurrentItem.set(rectDrawn.left, wheelCenterYCoordinate - halfItemHeight,
                rectDrawn.right, wheelCenterYCoordinate + halfItemHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (null != onWheelChangedListener) {
            onWheelChangedListener.onWheelScrolled(this, scrollOffsetYCoordinate);
        }
        if (itemHeight - halfDrawnItemCount <= 0) {
            return;
        }
        drawCurtain(canvas);
        drawIndicator(canvas);
        drawAllItem(canvas);
    }

    private void drawAllItem(Canvas canvas) {
        int drawnDataStartPos = -1 * scrollOffsetYCoordinate / itemHeight - halfDrawnItemCount;
        for (int drawnDataPosition = drawnDataStartPos + defaultItemPosition,
             drawnOffsetPos = -1 * halfDrawnItemCount;
             drawnDataPosition < drawnDataStartPos + defaultItemPosition + drawnItemCount;
             drawnDataPosition++, drawnOffsetPos++) {

            initTextPaint();
            boolean isCenterItem = drawnDataPosition == drawnDataStartPos + defaultItemPosition + drawnItemCount / 2;

            int drawnItemCenterYCoordinate = drawnCenterYCoordinate + (drawnOffsetPos * itemHeight)
                    + scrollOffsetYCoordinate % itemHeight;
            int centerYCoordinateAbs = Math.abs(drawnCenterYCoordinate - drawnItemCenterYCoordinate);
            // Correct ratio of item's drawn center to wheel center
            float ratio = (drawnCenterYCoordinate - centerYCoordinateAbs - rectDrawn.top) * 1f /
                    (drawnCenterYCoordinate - rectDrawn.top);
            float degree = computeDegree(drawnItemCenterYCoordinate, ratio);
            float distanceToCenter = computeYCoordinateAtAngle(degree);

            if (curvedEnabled) {
                int transXCoordinate = wheelCenterXCoordinate;
                switch (textAlign) {
                    case ItemTextAlign.LEFT:
                        transXCoordinate = rectDrawn.left;
                        break;
                    case ItemTextAlign.RIGHT:
                        transXCoordinate = rectDrawn.right;
                        break;
                    case ItemTextAlign.CENTER:
                    default:
                        break;
                }
                float transYCoordinate = wheelCenterYCoordinate - distanceToCenter;

                camera.save();
                camera.rotateX(degree);
                camera.getMatrix(matrixRotate);
                camera.restore();
                matrixRotate.preTranslate(-transXCoordinate, -transYCoordinate);
                matrixRotate.postTranslate(transXCoordinate, transYCoordinate);

                camera.save();
                camera.translate(0, 0, computeDepth(degree));
                camera.getMatrix(matrixDepth);
                camera.restore();
                matrixDepth.preTranslate(-transXCoordinate, -transYCoordinate);
                matrixDepth.postTranslate(transXCoordinate, transYCoordinate);
                matrixRotate.postConcat(matrixDepth);
            }

            computeAndSetAtmospheric(centerYCoordinateAbs);
            // Correct item's drawn center Y coordinate base on curved state
            float drawCenterYCoordinate = curvedEnabled ? drawnCenterYCoordinate - distanceToCenter
                    : drawnItemCenterYCoordinate;
            drawItemRect(canvas, drawnDataPosition, isCenterItem, drawCenterYCoordinate);
        }
    }

    private void drawItemRect(Canvas canvas, int dataPosition, boolean isCenterItem, float drawCenterYCoordinate) {
        // Judges need to draw different color for current item or not
        if (selectedTextColor == 0/* Color.TRANSPARENT */) {
            //没有设置选中项颜色，绘制所有项
            canvas.save();
            canvas.clipRect(rectDrawn);
            if (curvedEnabled) {
                canvas.concat(matrixRotate);
            }
            drawItemText(canvas, dataPosition, drawCenterYCoordinate);
            canvas.restore();
            return;
        }

        if (textSize == selectedTextSize && !selectedTextBold) {
            //没有设置选中项的加大加粗，绘制所有项
            canvas.save();
            if (curvedEnabled) {
                canvas.concat(matrixRotate);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                canvas.clipOutRect(rectCurrentItem);
            } else {
                canvas.clipRect(rectCurrentItem, Region.Op.DIFFERENCE);
            }
            drawItemText(canvas, dataPosition, drawCenterYCoordinate);
            canvas.restore();
            paint.setColor(selectedTextColor);
            canvas.save();
            if (curvedEnabled) {
                canvas.concat(matrixRotate);
            }
            canvas.clipRect(rectCurrentItem);
            drawItemText(canvas, dataPosition, drawCenterYCoordinate);
            canvas.restore();
            return;
        }

        if (!isCenterItem) {
            // 绘制非选中项
            canvas.save();
            if (curvedEnabled) {
                canvas.concat(matrixRotate);
            }
            drawItemText(canvas, dataPosition, drawCenterYCoordinate);
            canvas.restore();
            return;
        }

        // 绘制选中项
        paint.setColor(selectedTextColor);
        paint.setTextSize(selectedTextSize);
        paint.setFakeBoldText(selectedTextBold);
        canvas.save();
        if (curvedEnabled) {
            canvas.concat(matrixRotate);
        }
        drawItemText(canvas, dataPosition, drawCenterYCoordinate);
        canvas.restore();
    }

    private void drawItemText(Canvas canvas, int dataPosition, float drawCenterYCoordinate) {
        boolean hasCut = false;
        String ellipsis = "...";
        int measuredWidth = getMeasuredWidth();
        float ellipsisWidth = paint.measureText(ellipsis);
        String data = obtainItemText(dataPosition);
        while (paint.measureText(data) + ellipsisWidth - measuredWidth > 0) {
            // 超出控件宽度则省略部分文字
            int length = data.length();
            if (length > 1) {
                data = data.substring(0, length - 1);
                hasCut = true;
            } else {
                break;
            }
        }
        if (hasCut) {
            data = data + ellipsis;
        }
        canvas.drawText(data, drawnCenterXCoordinate, drawCenterYCoordinate, paint);
    }

    private float computeDegree(int drawnItemCenterYCoordinate, float ratio) {
        // Correct unit
        int unit = 0;
        if (drawnItemCenterYCoordinate > drawnCenterYCoordinate) {
            unit = 1;
        } else if (drawnItemCenterYCoordinate < drawnCenterYCoordinate) {
            unit = -1;
        }
        return clamp((-(1 - ratio) * curvedMaxAngle * unit), -curvedMaxAngle, curvedMaxAngle);
    }

    private float clamp(float value, float min, float max) {
        if (value < min) {
            return min;
        }
        return Math.min(value, max);
    }

    private String obtainItemText(int drawnDataPosition) {
        String data = "";
        final int itemCount = getItemCount();
        if (cyclicEnabled) {
            if (itemCount != 0) {
                int actualPosition = drawnDataPosition % itemCount;
                actualPosition = actualPosition < 0 ? (actualPosition + itemCount) : actualPosition;
                data = formatItem(actualPosition);
            }
        } else {
            if (isPositionInRange(drawnDataPosition, itemCount)) {
                data = formatItem(drawnDataPosition);
            }
        }
        return data;
    }

    public String formatItem(int position) {
        return formatItem(getItem(position));
    }

    public String formatItem(Object item) {
        if (item == null) {
            return "";
        }
        if (item instanceof TextProvider) {
            return ((TextProvider) item).provideText();
        }
        if (formatter != null) {
            return formatter.formatItem(item);
        }
        return item.toString();
    }

    private void computeAndSetAtmospheric(int abs) {
        if (atmosphericEnabled) {
            int alpha = (int) ((drawnCenterYCoordinate - abs) * 1.0F / drawnCenterYCoordinate * 255);
            alpha = Math.max(alpha, 0);
            paint.setAlpha(alpha);
        }
    }

    private void drawCurtain(Canvas canvas) {
        // Need to draw curtain or not
        if (!curtainEnabled) {
            return;
        }
        paint.setColor(curtainColor);
        paint.setStyle(Paint.Style.FILL);
        if (curtainRadius > 0) {
            Path path = new Path();
            float[] radii;
            switch (curtainCorner) {
                case CurtainCorner.ALL:
                    radii = new float[]{
                            curtainRadius, curtainRadius, curtainRadius, curtainRadius,
                            curtainRadius, curtainRadius, curtainRadius, curtainRadius
                    };
                    break;
                case CurtainCorner.TOP:
                    radii = new float[]{
                            curtainRadius, curtainRadius, curtainRadius, curtainRadius, 0, 0, 0, 0
                    };
                    break;
                case CurtainCorner.BOTTOM:
                    radii = new float[]{
                            0, 0, 0, 0, curtainRadius, curtainRadius, curtainRadius, curtainRadius
                    };
                    break;
                case CurtainCorner.LEFT:
                    radii = new float[]{
                            curtainRadius, curtainRadius, 0, 0, 0, 0, curtainRadius, curtainRadius
                    };
                    break;
                case CurtainCorner.RIGHT:
                    radii = new float[]{
                            0, 0, curtainRadius, curtainRadius, curtainRadius, curtainRadius, 0, 0
                    };
                    break;
                default:
                    radii = new float[]{0, 0, 0, 0, 0, 0, 0, 0};
                    break;
            }
            path.addRoundRect(new RectF(rectCurrentItem), radii, Path.Direction.CCW);
            canvas.drawPath(path, paint);
            return;
        }
        canvas.drawRect(rectCurrentItem, paint);
    }

    private void drawIndicator(Canvas canvas) {
        // Need to draw indicator or not
        if (!indicatorEnabled) {
            return;
        }
        paint.setColor(indicatorColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(rectIndicatorHead, paint);
        canvas.drawRect(rectIndicatorFoot, paint);
    }

    private boolean isPositionInRange(int position, int itemCount) {
        return position >= 0 && position < itemCount;
    }

    private float computeYCoordinateAtAngle(float degree) {
        // Compute y-coordinate for item at degree.
        return sinDegree(degree) / sinDegree(curvedMaxAngle) * halfWheelHeight;
    }

    private float sinDegree(float degree) {
        return (float) Math.sin(Math.toRadians(degree));
    }

    private int computeDepth(float degree) {
        return (int) (halfWheelHeight - Math.cos(Math.toRadians(degree)) * halfWheelHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isEnabled()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    handleActionDown(event);
                    break;
                case MotionEvent.ACTION_MOVE:
                    handleActionMove(event);
                    break;
                case MotionEvent.ACTION_UP:
                    handleActionUp(event);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    handleActionCancel(event);
                    break;
                default:
                    break;
            }
        }
        if (isClick) {
            //onTouchEvent should call performClick when a click is detected
            performClick();
        }
        return true;
    }

    private void handleActionDown(MotionEvent event) {
        if (null != getParent()) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        obtainOrClearTracker();
        tracker.addMovement(event);
        if (!scroller.isFinished()) {
            scroller.abortAnimation();
            isForceFinishScroll = true;
        }
        downPointYCoordinate = lastPointYCoordinate = (int) event.getY();
    }

    private void handleActionMove(MotionEvent event) {
        int endPoint = computeDistanceToEndPoint(scroller.getFinalY() % itemHeight);
        if (Math.abs(downPointYCoordinate - event.getY()) < touchSlop && endPoint > 0) {
            isClick = true;
            return;
        }
        isClick = false;
        if (null != tracker) {
            tracker.addMovement(event);
        }
        if (null != onWheelChangedListener) {
            onWheelChangedListener.onWheelScrollStateChanged(this, ScrollState.DRAGGING);
        }
        // Scroll WheelPicker's content
        float move = event.getY() - lastPointYCoordinate;
        if (Math.abs(move) < 1) {
            return;
        }

        // Scroll limit
        float topLimit = (defaultItemPosition) * itemHeight;
        float bottomLimit = -(getItemCount() - 1 - defaultItemPosition) * itemHeight;
        boolean disableScrollTop = scrollOffsetYCoordinate >=topLimit && move > 0;
        boolean disableScrollBottom = scrollOffsetYCoordinate <= bottomLimit && move < 0;
        if(!cyclicEnabled)
        {
            if(!disableScrollBottom && !disableScrollTop)
            {
                scrollOffsetYCoordinate += move;
            }
        }else
        {
            scrollOffsetYCoordinate += move;
        }
        //
        
        lastPointYCoordinate = (int) event.getY();
        invalidate();
    }

    private void handleActionUp(MotionEvent event) {
        if (null != getParent()) {
            getParent().requestDisallowInterceptTouchEvent(false);
        }
        if (isClick) {
            return;
        }
        int yVelocity = 0;
        if (null != tracker) {
            tracker.addMovement(event);
            tracker.computeCurrentVelocity(1000, maximumVelocity);
            yVelocity = (int) tracker.getYVelocity();
        }

        // Judge scroll or fling base on current velocity
        isForceFinishScroll = false;
        if (Math.abs(yVelocity) > minimumVelocity) {
            scroller.fling(0, scrollOffsetYCoordinate, 0, yVelocity, 0,
                    0, minFlingYCoordinate, maxFlingYCoordinate);
            int endPoint = computeDistanceToEndPoint(scroller.getFinalY() % itemHeight);
            scroller.setFinalY(scroller.getFinalY() + endPoint);
        } else {
            int endPoint = computeDistanceToEndPoint(scrollOffsetYCoordinate % itemHeight);
            scroller.startScroll(0, scrollOffsetYCoordinate, 0, endPoint);
        }
        // Correct coordinates
        if (!cyclicEnabled) {
            if (scroller.getFinalY() > maxFlingYCoordinate) {
                scroller.setFinalY(maxFlingYCoordinate);
            } else if (scroller.getFinalY() < minFlingYCoordinate) {
                scroller.setFinalY(minFlingYCoordinate);
            }
        }
        handler.post(this);
        cancelTracker();
    }

    private void handleActionCancel(MotionEvent event) {
        if (null != getParent()) {
            getParent().requestDisallowInterceptTouchEvent(false);
        }
        cancelTracker();
    }

    private void obtainOrClearTracker() {
        if (null == tracker) {
            tracker = VelocityTracker.obtain();
        } else {
            tracker.clear();
        }
    }

    private void cancelTracker() {
        if (null != tracker) {
            tracker.recycle();
            tracker = null;
        }
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private int computeDistanceToEndPoint(int remainder) {
        if (Math.abs(remainder) > halfItemHeight) {
            if (scrollOffsetYCoordinate < 0) {
                return -itemHeight - remainder;
            } else {
                return itemHeight - remainder;
            }
        } else {
            return -1 * remainder;
        }
    }

    @Override
    public void run() {
        if (itemHeight == 0) {
            return;
        }
        int itemCount = getItemCount();
        if (itemCount == 0) {
            if (null != onWheelChangedListener) {
                onWheelChangedListener.onWheelScrollStateChanged(this, ScrollState.IDLE);
            }
            return;
        }
        if (scroller.isFinished() && !isForceFinishScroll) {
            int position = computePosition(itemCount);
            position = position < 0 ? position + itemCount : position;
            currentPosition = position;
            if (null != onWheelChangedListener) {
                onWheelChangedListener.onWheelSelected(this, position);
                onWheelChangedListener.onWheelScrollStateChanged(this, ScrollState.IDLE);
            }
            postInvalidate();
            return;
        }
        // Scroll not finished
        if (scroller.computeScrollOffset()) {
            if (null != onWheelChangedListener) {
                onWheelChangedListener.onWheelScrollStateChanged(this, ScrollState.SCROLLING);
            }
            scrollOffsetYCoordinate = scroller.getCurrY();
            int position = computePosition(itemCount);
            if (lastScrollPosition != position) {
                if (position == 0 && lastScrollPosition == itemCount - 1) {
                    if (null != onWheelChangedListener) {
                        onWheelChangedListener.onWheelLoopFinished(this);
                    }
                }
                lastScrollPosition = position;
            }
            postInvalidate();
            handler.postDelayed(this, 20);
        }
    }

    private int computePosition(int itemCount) {
        return (-1 * scrollOffsetYCoordinate / itemHeight + defaultItemPosition) % itemCount;
    }

    public void scrollTo(final int position) {
        post(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged(position);
            }
        });
    }

    public final void smoothScrollTo(final int position) {
        if (isInEditMode()) {
            scrollTo(position);
            return;
        }
        int differencesLines = currentPosition - position;
        int newScrollOffsetYCoordinate = scrollOffsetYCoordinate + (differencesLines * itemHeight);
        ValueAnimator animator = ValueAnimator.ofInt(scrollOffsetYCoordinate, newScrollOffsetYCoordinate);
        animator.setDuration(300);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scrollOffsetYCoordinate = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                scrollTo(position);
            }
        });
        animator.start();
    }

}
