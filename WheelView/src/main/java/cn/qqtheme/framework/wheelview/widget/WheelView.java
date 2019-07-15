package cn.qqtheme.framework.wheelview.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import cn.qqtheme.framework.logger.CqrLog;
import cn.qqtheme.framework.wheelview.R;
import cn.qqtheme.framework.wheelview.adapter.WheelAdapter;
import cn.qqtheme.framework.wheelview.annotation.ItemAlign;
import cn.qqtheme.framework.wheelview.contract.OnWheelChangedListener;
import cn.qqtheme.framework.wheelview.contract.OnWheelSelectedListener;
import cn.qqtheme.framework.wheelview.contract.TextProvider;
import cn.qqtheme.framework.wheelview.util.DensityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 滚轮控件，核心代码参考 https://github.com/florent37/SingleDateAndTimePicker#WheelPicker
 *
 * @param <T> 泛型除了{@code CharSequence}及其子类，需要实现{@link TextProvider}
 *            或者重载{@code Object#toString}提供显示文本
 * @author Florent Champigny
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @version 2.0
 * @date 2019/5/8 11:11
 * @see WheelAdapter
 * @see TextProvider
 * @see OnWheelSelectedListener
 * @see OnWheelChangedListener
 * @since 1.0
 */
@SuppressWarnings({"unused"})
public class WheelView<T> extends View implements Runnable, WheelAdapter.Formatter {

    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_SCROLLING = 2;

    protected T defaultItem;
    protected int lastScrollPosition;
    protected Listener<T> listener;
    protected WheelAdapter<T> adapter = new WheelAdapter<>();

    private final Handler handler = new Handler();
    private Paint paint;
    private Scroller scroller;
    private VelocityTracker tracker;
    private OnWheelSelectedListener<T> wheelSelectedListener;
    private OnWheelChangedListener wheelChangedListener;
    private final Rect rectDrawn = new Rect();
    private final Rect rectIndicatorHead = new Rect();
    private final Rect rectIndicatorFoot = new Rect();
    private final Rect rectCurrentItem = new Rect();
    private final Camera camera = new Camera();
    private final Matrix matrixRotate = new Matrix();
    private final Matrix matrixDepth = new Matrix();
    private String maxWidthText;

    private int visibleItemCount, drawnItemCount;
    private int halfDrawnItemCount;
    private int textMaxWidth, textMaxHeight;
    private int itemTextColor, selectedItemTextColor;
    private int itemTextSize;
    private int indicatorSize;
    private int indicatorColor;
    private int curtainColor;
    private int itemSpace;
    private int itemAlign;
    private int itemHeight, halfItemHeight;
    private int halfWheelHeight;
    private int defaultItemPosition;
    private int currentItemPosition;
    private int minFlingYcoord, maxFlingYcoord;
    private int wheelCenterXcoord, wheelCenterYcoord;
    private int drawnCenterXcoord, drawnCenterYcoord;
    private int scrollOffsetYcoord;
    private int textMaxWidthPosition;
    private int lastPointYcoord;
    private int downPointYcoord;
    private int minimumVelocity, maximumVelocity;
    private int touchSlop;

    private boolean hasSameWidth;
    private boolean hasIndicator;
    private boolean hasCurtain;
    private boolean hasAtmospheric;
    private boolean isCyclic;
    private boolean isCurved;

    private boolean isClick;
    private boolean isForceFinishScroll;

    private AttributeSet attrs;

    public WheelView(Context context) {
        this(context, null);
    }

    public WheelView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.WheelStyle);
    }

    public WheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, R.style.WheelDefault);
    }

    public WheelView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        this.attrs = attrs;
        initAttrs(context, attrs, defStyleAttr, defStyleRes);

        updateVisibleItemCount();

        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
        paint.setTextSize(itemTextSize);

        scroller = new Scroller(context);

        ViewConfiguration configuration = ViewConfiguration.get(context);
        minimumVelocity = configuration.getScaledMinimumFlingVelocity();
        maximumVelocity = configuration.getScaledMaximumFlingVelocity();
        touchSlop = configuration.getScaledTouchSlop();

        init();
        defaultItem = assignDefault();
        adapter.setData(generateData());
        currentItemPosition = adapter.getItemPosition(defaultItem);
        defaultItemPosition = currentItemPosition;
    }

    public void setStyle(@StyleRes int style) {
        if (attrs == null) {
            throw new RuntimeException("Please use " + getClass().getSimpleName() + " in xml");
        }
        initAttrs(getContext(), attrs, R.attr.WheelStyle, style);
        requestLayout();
        postInvalidate();
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (attrs == null) {
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WheelView,
                defStyleAttr, defStyleRes);

        itemTextSize = a.getDimensionPixelSize(R.styleable.WheelView_wheel_itemTextSize,
                DensityUtils.sp2px(context, 20));
        visibleItemCount = a.getInt(R.styleable.WheelView_wheel_itemVisibleCount, 5);
        defaultItemPosition = a.getInt(R.styleable.WheelView_wheel_itemDefaultPosition, 0);
        hasSameWidth = a.getBoolean(R.styleable.WheelView_wheel_hasSameWidth, false);
        textMaxWidthPosition = a.getInt(R.styleable.WheelView_wheel_maxWidthTextPosition, 0);
        maxWidthText = a.getString(R.styleable.WheelView_wheel_maxWidthText);
        selectedItemTextColor = a.getColor(R.styleable.WheelView_wheel_itemTextColorSelected, 0xFF000000);
        itemTextColor = a.getColor(R.styleable.WheelView_wheel_itemTextColor, 0xFF888888);
        itemSpace = a.getDimensionPixelSize(R.styleable.WheelView_wheel_itemSpace,
                DensityUtils.dp2px(context, 15));
        isCyclic = a.getBoolean(R.styleable.WheelView_wheel_cyclic, false);
        hasIndicator = a.getBoolean(R.styleable.WheelView_wheel_indicator, false);
        indicatorColor = a.getColor(R.styleable.WheelView_wheel_indicatorColor, 0xFFEE3333);
        indicatorSize = a.getDimensionPixelSize(R.styleable.WheelView_wheel_indicatorSize,
                DensityUtils.sp2px(context, 1));
        hasCurtain = a.getBoolean(R.styleable.WheelView_wheel_curtain, false);
        curtainColor = a.getColor(R.styleable.WheelView_wheel_curtainColor, 0x88FFFFFF);
        hasAtmospheric = a.getBoolean(R.styleable.WheelView_wheel_atmospheric, false);
        isCurved = a.getBoolean(R.styleable.WheelView_wheel_curved, false);
        itemAlign = a.getInt(R.styleable.WheelView_wheel_itemAlign, ItemAlign.CENTER);

        a.recycle();
    }

    protected void init() {

    }

    @SuppressWarnings("unchecked")
    protected List<T> generateData() {
        final List<T> data = new ArrayList<>();
        if (isInEditMode()) {
            data.add((T) ("贵州穿青人"));
            data.add((T) ("大定府羡民"));
            data.add((T) ("不在五十六个民族之内"));
            data.add((T) ("已识别待定民族"));
            data.add((T) ("少数民族待遇"));
        }
        return data;
    }

    protected T assignDefault() {
        return null;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setAdapter(adapter);
        setDefaultItem(defaultItem);
    }

    private void updateVisibleItemCount() {
        final int minCount = 2;
        if (visibleItemCount < minCount) {
            throw new ArithmeticException("Visible item count can not be less than " + minCount);
        }

        int evenNumberFlag = 2;
        if (visibleItemCount % evenNumberFlag == 0) {
            visibleItemCount += 1;
        }
        drawnItemCount = visibleItemCount + 2;
        halfDrawnItemCount = drawnItemCount / 2;
    }

    private void computeTextSize() {
        textMaxWidth = textMaxHeight = 0;
        final int itemCount = adapter.getItemCount();
        if (hasSameWidth) {
            textMaxWidth = (int) paint.measureText(adapter.getItemText(0, this));
        } else if (isPositionInRange(textMaxWidthPosition, itemCount)) {
            textMaxWidth = (int) paint.measureText(adapter.getItemText(textMaxWidthPosition, this));
        } else if (!TextUtils.isEmpty(maxWidthText)) {
            textMaxWidth = (int) paint.measureText(maxWidthText);
        } else {
            for (int i = 0; i < itemCount; ++i) {
                int width = (int) paint.measureText(adapter.getItemText(i, this));
                textMaxWidth = Math.max(textMaxWidth, width);
            }
        }
        final Paint.FontMetrics metrics = paint.getFontMetrics();
        textMaxHeight = (int) (metrics.bottom - metrics.top);
    }

    private void updateItemTextAlign() {
        switch (itemAlign) {
            case ItemAlign.LEFT:
                paint.setTextAlign(Paint.Align.LEFT);
                break;
            case ItemAlign.RIGHT:
                paint.setTextAlign(Paint.Align.RIGHT);
                break;
            case ItemAlign.CENTER:
            default:
                paint.setTextAlign(Paint.Align.CENTER);
                break;
        }
    }

    public int getCurrentItemPosition() {
        return currentItemPosition;
    }

    public T getCurrentItem() {
        return getItemByPosition(currentItemPosition);
    }

    public T getItemByPosition(int position) {
        return adapter.getItem(position);
    }

    public int getVisibleItemCount() {
        return visibleItemCount;
    }

    public void setVisibleItemCount(int count) {
        visibleItemCount = count;
        updateVisibleItemCount();
        requestLayout();
    }

    public boolean isCyclic() {
        return isCyclic;
    }

    public void setCyclic(boolean isCyclic) {
        this.isCyclic = isCyclic;
        computeFlingLimitYcoord();
        invalidate();
    }

    public void setListener(Listener<T> listener) {
        this.listener = listener;
    }

    public void setWheelSelectedListener(OnWheelSelectedListener<T> listener) {
        wheelSelectedListener = listener;
    }

    public void setWheelChangedListener(OnWheelChangedListener listener) {
        wheelChangedListener = listener;
    }

    public void setAdapter(WheelAdapter<T> adapter) {
        this.adapter = adapter;
        updateItemTextAlign();
        notifyDataSetChanged();
    }

    public WheelAdapter<T> getAdapter() {
        return adapter;
    }

    public void setData(List<T> data) {
        adapter.setData(data);
        notifyDataSetChanged();
    }

    public void setDefaultItem(T item) {
        this.defaultItem = item;
        int position = adapter.getData().indexOf(defaultItem);
        setDefaultItemPosition(position);
    }

    public void setDefaultItemPosition(int position) {
        position = Math.min(position, adapter.getItemCount() - 1);
        position = Math.max(position, 0);
        defaultItemPosition = position;
        currentItemPosition = position;
        scrollOffsetYcoord = 0;
        computeFlingLimitYcoord();
        requestLayout();
        invalidate();
    }

    public void setSameWidth(boolean hasSameWidth) {
        this.hasSameWidth = hasSameWidth;
        computeTextSize();
        requestLayout();
        invalidate();
    }

    public boolean hasSameWidth() {
        return hasSameWidth;
    }

    public String getMaxWidthText() {
        return maxWidthText;
    }

    public void setMaxWidthText(String text) {
        if (null == text) {
            throw new NullPointerException("Maximum width text can not be null!");
        }
        maxWidthText = text;
        computeTextSize();
        requestLayout();
        postInvalidate();
    }

    public int getMaxWidthTextPosition() {
        return textMaxWidthPosition;
    }

    public void setMaxWidthTextPosition(int position) {
        int itemCount = adapter.getItemCount();
        if (itemCount == 0) {
            return;
        }
        if (!isPositionInRange(position, itemCount)) {
            throw new ArrayIndexOutOfBoundsException("Maximum width text Position must in [0, " +
                    itemCount + "), but current is " + position);
        }
        textMaxWidthPosition = position;
        computeTextSize();
        requestLayout();
        postInvalidate();
    }

    @ColorInt
    public int getSelectedTextColor() {
        return selectedItemTextColor;
    }

    public void setSelectedTextColor(@ColorInt int color) {
        selectedItemTextColor = color;
        computeCurrentItemRect();
        postInvalidate();
    }

    @ColorInt
    public int getTextColor() {
        return itemTextColor;
    }

    public void setTextColor(@ColorInt int color) {
        itemTextColor = color;
        postInvalidate();
    }

    public int getTextSize() {
        return itemTextSize;
    }

    public void setTextSize(int size) {
        if (itemTextSize != size) {
            itemTextSize = size;
            paint.setTextSize(itemTextSize);
            computeTextSize();
            requestLayout();
            postInvalidate();
        }
    }

    public int getItemSpace() {
        return itemSpace;
    }

    public void setItemSpace(int space) {
        itemSpace = space;
        requestLayout();
        postInvalidate();
    }

    public void setIndicator(boolean hasIndicator) {
        this.hasIndicator = hasIndicator;
        computeIndicatorRect();
        postInvalidate();
    }

    public boolean hasIndicator() {
        return hasIndicator;
    }

    public int getIndicatorSize() {
        return indicatorSize;
    }

    public void setIndicatorSize(int size) {
        indicatorSize = size;
        computeIndicatorRect();
        postInvalidate();
    }

    @ColorInt
    public int getIndicatorColor() {
        return indicatorColor;
    }

    public void setIndicatorColor(@ColorInt int color) {
        indicatorColor = color;
        postInvalidate();
    }

    public void setCurtain(boolean hasCurtain) {
        this.hasCurtain = hasCurtain;
        computeCurrentItemRect();
        postInvalidate();
    }

    public boolean hasCurtain() {
        return hasCurtain;
    }

    @ColorInt
    public int getCurtainColor() {
        return curtainColor;
    }

    public void setCurtainColor(@ColorInt int color) {
        curtainColor = color;
        postInvalidate();
    }

    public void setAtmospheric(boolean hasAtmospheric) {
        this.hasAtmospheric = hasAtmospheric;
        postInvalidate();
    }

    public boolean hasAtmospheric() {
        return hasAtmospheric;
    }

    public boolean isCurved() {
        return isCurved;
    }

    public void setCurved(boolean isCurved) {
        this.isCurved = isCurved;
        requestLayout();
        postInvalidate();
    }

    @ItemAlign
    public int getItemAlign() {
        return itemAlign;
    }

    public void setItemAlign(@ItemAlign int align) {
        itemAlign = align;
        updateItemTextAlign();
        computeDrawnCenterCoordinate();
        postInvalidate();
    }

    public Typeface getTypeface() {
        if (null != paint) {
            return paint.getTypeface();
        }
        return null;
    }

    public void setTypeface(Typeface tf) {
        if (null != paint) {
            paint.setTypeface(tf);
        }
        computeTextSize();
        requestLayout();
        postInvalidate();
    }

    public void notifyDataSetChanged() {
        int maxPosition = adapter.getItemCount() - 1;
        if (defaultItemPosition > maxPosition || currentItemPosition > maxPosition) {
            defaultItemPosition = currentItemPosition = maxPosition;
        } else {
            defaultItemPosition = currentItemPosition;
        }
        scrollOffsetYcoord = 0;
        computeTextSize();
        computeFlingLimitYcoord();
        requestLayout();
        postInvalidate();
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
        if (isCurved) {
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
        wheelCenterXcoord = rectDrawn.centerX();
        wheelCenterYcoord = rectDrawn.centerY();

        // Correct item drawn center
        computeDrawnCenterCoordinate();

        halfWheelHeight = rectDrawn.height() / 2;

        itemHeight = rectDrawn.height() / visibleItemCount;
        halfItemHeight = itemHeight / 2;

        // Initialize fling max Y-coordinates
        computeFlingLimitYcoord();

        // Correct region of indicator
        computeIndicatorRect();

        // Correct region of current select item
        computeCurrentItemRect();
    }

    private void computeDrawnCenterCoordinate() {
        switch (itemAlign) {
            case ItemAlign.LEFT:
                drawnCenterXcoord = rectDrawn.left;
                break;
            case ItemAlign.RIGHT:
                drawnCenterXcoord = rectDrawn.right;
                break;
            case ItemAlign.CENTER:
            default:
                drawnCenterXcoord = wheelCenterXcoord;
                break;
        }
        drawnCenterYcoord = (int) (wheelCenterYcoord -
                ((paint.ascent() + paint.descent()) / 2));
    }

    private void computeFlingLimitYcoord() {
        int currentItemOffset = defaultItemPosition * itemHeight;
        minFlingYcoord = isCyclic ? Integer.MIN_VALUE
                : -itemHeight * (adapter.getItemCount() - 1) + currentItemOffset;
        maxFlingYcoord = isCyclic ? Integer.MAX_VALUE : currentItemOffset;
    }

    private void computeIndicatorRect() {
        if (!hasIndicator) {
            return;
        }
        int halfIndicatorSize = indicatorSize / 2;
        int indicatorHeadCenterYcoord = wheelCenterYcoord + halfItemHeight;
        int indicatorFootCenterYcoord = wheelCenterYcoord - halfItemHeight;
        rectIndicatorHead.set(rectDrawn.left, indicatorHeadCenterYcoord - halfIndicatorSize,
                rectDrawn.right, indicatorHeadCenterYcoord + halfIndicatorSize);
        rectIndicatorFoot.set(rectDrawn.left, indicatorFootCenterYcoord - halfIndicatorSize,
                rectDrawn.right, indicatorFootCenterYcoord + halfIndicatorSize);
    }

    private void computeCurrentItemRect() {
        if (!hasCurtain && selectedItemTextColor == -1) {
            return;
        }
        rectCurrentItem.set(rectDrawn.left, wheelCenterYcoord - halfItemHeight,
                rectDrawn.right, wheelCenterYcoord + halfItemHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (null != wheelChangedListener) {
            wheelChangedListener.onWheelScrolled(this, scrollOffsetYcoord);
        }
        if (itemHeight - halfDrawnItemCount <= 0) {
            return;
        }
        drawAllItemText(canvas);
        drawCurtain(canvas);
        drawIndicator(canvas);
    }

    private void drawAllItemText(Canvas canvas) {
        int drawnDataStartPos = -scrollOffsetYcoord / itemHeight - halfDrawnItemCount;
        for (int drawnDataPosition = drawnDataStartPos + defaultItemPosition,
             drawnOffsetPos = -halfDrawnItemCount;
             drawnDataPosition < drawnDataStartPos + defaultItemPosition + drawnItemCount;
             drawnDataPosition++, drawnOffsetPos++) {
            paint.setColor(itemTextColor);
            paint.setStyle(Paint.Style.FILL);
            int drawnItemCenterYcoord = drawnCenterYcoord + (drawnOffsetPos * itemHeight)
                    + scrollOffsetYcoord % itemHeight;

            int centerYcoordAbs = Math.abs(drawnCenterYcoord - drawnItemCenterYcoord);
            // Correct ratio of item's drawn center to wheel center
            float ratio = (drawnCenterYcoord - centerYcoordAbs - rectDrawn.top) * 1.0F /
                    (drawnCenterYcoord - rectDrawn.top);
            float degree = computeDegree(drawnItemCenterYcoord, ratio);
            int distanceToCenter = computeSpace(degree);

            if (isCurved) {
                int transXcoord = wheelCenterXcoord;
                switch (itemAlign) {
                    case ItemAlign.LEFT:
                        transXcoord = rectDrawn.left;
                        break;
                    case ItemAlign.RIGHT:
                        transXcoord = rectDrawn.right;
                        break;
                    case ItemAlign.CENTER:
                    default:
                        break;
                }
                int transYcoord = wheelCenterYcoord - distanceToCenter;

                camera.save();
                camera.rotateX(degree);
                camera.getMatrix(matrixRotate);
                camera.restore();
                matrixRotate.preTranslate(-transXcoord, -transYcoord);
                matrixRotate.postTranslate(transXcoord, transYcoord);

                camera.save();
                camera.translate(0, 0, computeDepth(degree));
                camera.getMatrix(matrixDepth);
                camera.restore();
                matrixDepth.preTranslate(-transXcoord, -transYcoord);
                matrixDepth.postTranslate(transXcoord, transYcoord);
                matrixRotate.postConcat(matrixDepth);
            }

            computeAndSetAtmospheric(centerYcoordAbs);
            // Correct item's drawn center Y coordinate base on curved state
            int drawCenterYcoord = isCurved ? this.drawnCenterYcoord - distanceToCenter
                    : drawnItemCenterYcoord;
            String data = obtainItemText(drawnDataPosition);
            drawItemText(canvas, data, drawCenterYcoord);
        }
    }

    private void drawItemText(Canvas canvas, String data, int drawCenterYcoord) {
        // Judges need to draw different color for current item or not
        if (selectedItemTextColor != -1) {
            canvas.save();
            if (isCurved) {
                canvas.concat(matrixRotate);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                canvas.clipOutRect(rectCurrentItem);
            } else {
                canvas.clipRect(rectCurrentItem, Region.Op.DIFFERENCE);
            }
            canvas.drawText(data, drawnCenterXcoord, drawCenterYcoord, paint);
            canvas.restore();

            paint.setColor(selectedItemTextColor);
            canvas.save();
            if (isCurved) {
                canvas.concat(matrixRotate);
            }
            canvas.clipRect(rectCurrentItem);
            canvas.drawText(data, drawnCenterXcoord, drawCenterYcoord, paint);
            canvas.restore();
        } else {
            canvas.save();
            canvas.clipRect(rectDrawn);
            if (isCurved) {
                canvas.concat(matrixRotate);
            }
            canvas.drawText(data, drawnCenterXcoord, drawCenterYcoord, paint);
            canvas.restore();
        }
    }

    private float computeDegree(int drawnItemCenterYcoord, float ratio) {
        // Correct unit
        int unit = 0;
        if (drawnItemCenterYcoord > drawnCenterYcoord) {
            unit = 1;
        } else if (drawnItemCenterYcoord < drawnCenterYcoord) {
            unit = -1;
        }

        int stdDegree = 90;
        float degree = (-(1 - ratio) * stdDegree * unit);
        if (degree < -1 * stdDegree) {
            degree = -1 * stdDegree;
        }
        if (degree > stdDegree) {
            degree = stdDegree;
        }
        return degree;
    }

    private String obtainItemText(int drawnDataPosition) {
        String data = "";
        final int itemCount = adapter.getItemCount();
        if (isCyclic) {
            if (itemCount != 0) {
                int actualPosition = drawnDataPosition % itemCount;
                actualPosition = actualPosition < 0 ? (actualPosition + itemCount) : actualPosition;
                data = adapter.getItemText(actualPosition, this);
            }
        } else {
            if (isPositionInRange(drawnDataPosition, itemCount)) {
                data = adapter.getItemText(drawnDataPosition, this);
            }
        }
        return data;
    }

    @Override
    public String formatItemText(int position, @NonNull Object object) {
        if (object instanceof TextProvider) {
            return ((TextProvider) object).provideItemText();
        }
        return object.toString();
    }

    private void computeAndSetAtmospheric(int abs) {
        if (hasAtmospheric) {
            int alpha = (int) ((drawnCenterYcoord - abs) * 1.0F /
                    drawnCenterYcoord * 255);
            alpha = alpha < 0 ? 0 : alpha;
            paint.setAlpha(alpha);
        }
    }

    private void drawCurtain(Canvas canvas) {
        // Need to draw curtain or not
        if (hasCurtain) {
            paint.setColor(curtainColor);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(rectCurrentItem, paint);
        }
    }

    private void drawIndicator(Canvas canvas) {
        // Need to draw indicator or not
        if (hasIndicator) {
            paint.setColor(indicatorColor);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(rectIndicatorHead, paint);
            canvas.drawRect(rectIndicatorFoot, paint);
        }
    }

    private boolean isPositionInRange(int position, int itemCount) {
        return position >= 0 && position < itemCount;
    }

    private int computeSpace(float degree) {
        return (int) (Math.sin(Math.toRadians(degree)) * halfWheelHeight);
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
        downPointYcoord = lastPointYcoord = (int) event.getY();
    }

    private void handleActionMove(MotionEvent event) {
        int endPoint = computeDistanceToEndPoint(scroller.getFinalY() % itemHeight);
        if (Math.abs(downPointYcoord - event.getY()) < touchSlop && endPoint > 0) {
            isClick = true;
            return;
        }
        isClick = false;
        if (null != tracker) {
            tracker.addMovement(event);
        }
        if (null != wheelChangedListener) {
            wheelChangedListener.onWheelScrollStateChanged(this, SCROLL_STATE_DRAGGING);
        }

        // Scroll WheelPicker's content
        float move = event.getY() - lastPointYcoord;
        if (Math.abs(move) < 1) {
            return;
        }
        scrollOffsetYcoord += move;
        lastPointYcoord = (int) event.getY();
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
            scroller.fling(0, scrollOffsetYcoord, 0, yVelocity, 0,
                    0, minFlingYcoord, maxFlingYcoord);
            int endPoint = computeDistanceToEndPoint(scroller.getFinalY() % itemHeight);
            scroller.setFinalY(scroller.getFinalY() + endPoint);
        } else {
            int endPoint = computeDistanceToEndPoint(scrollOffsetYcoord % itemHeight);
            scroller.startScroll(0, scrollOffsetYcoord, 0, endPoint);
        }
        // Correct coordinates
        if (!isCyclic) {
            if (scroller.getFinalY() > maxFlingYcoord) {
                scroller.setFinalY(maxFlingYcoord);
            } else if (scroller.getFinalY() < minFlingYcoord) {
                scroller.setFinalY(minFlingYcoord);
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
            if (scrollOffsetYcoord < 0) {
                return -itemHeight - remainder;
            } else {
                return itemHeight - remainder;
            }
        } else {
            return -remainder;
        }
    }

    @Override
    public void run() {
        if (null == adapter) {
            return;
        }
        final int itemCount = adapter.getItemCount();
        if (itemCount == 0) {
            return;
        }
        boolean scrollFinished = scroller.isFinished() && !isForceFinishScroll;
        if (scrollFinished) {
            if (itemHeight == 0) {
                return;
            }
            int position = (-scrollOffsetYcoord / itemHeight + defaultItemPosition) % itemCount;
            position = position < 0 ? position + itemCount : position;
            currentItemPosition = position;
            selectCurrentItem();
            if (null != wheelChangedListener) {
                wheelChangedListener.onWheelSelected(this, position);
                wheelChangedListener.onWheelScrollStateChanged(this, SCROLL_STATE_IDLE);
            }
        }
        boolean scrollNotFinished = scroller.computeScrollOffset();
        if (scrollNotFinished) {
            if (null != wheelChangedListener) {
                wheelChangedListener.onWheelScrollStateChanged(this, SCROLL_STATE_SCROLLING);
            }

            scrollOffsetYcoord = scroller.getCurrY();
            int position = (-scrollOffsetYcoord / itemHeight + defaultItemPosition) % itemCount;
            onItemCurrentScroll(position, adapter.getItem(position));

            postInvalidate();
            handler.postDelayed(this, 16);
        }
    }

    public final void scrollTo(final int position) {
        if (position != currentItemPosition) {
            final int differencesLines = currentItemPosition - position;
            final int newScrollOffsetYcoord = scrollOffsetYcoord + (differencesLines * itemHeight);

            ValueAnimator animator = ValueAnimator.ofInt(scrollOffsetYcoord, newScrollOffsetYcoord);
            animator.setDuration(300);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    scrollOffsetYcoord = (int) animation.getAnimatedValue();
                    invalidate();
                }
            });
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    currentItemPosition = position;
                    selectCurrentItem();
                }
            });
            animator.start();
        }
    }

    protected final void selectCurrentItem() {
        int position = currentItemPosition;
        final T item = adapter.getItem(position);
        if (null != wheelSelectedListener) {
            wheelSelectedListener.onItemSelected(this, position, item);
        }
        onItemSelected(position, item);
    }

    protected void onItemSelected(int position, T item) {
        CqrLog.d("item selected: " + position + ", " + item);
        if (listener != null) {
            listener.onSelected(this, position, item);
        }
    }

    protected void onItemCurrentScroll(int position, T item) {
        CqrLog.d("item current scroll: " + position + ", " + item);
        if (lastScrollPosition != position) {
            if (listener != null) {
                listener.onCurrentScrolled(this, position, item);
                if (lastScrollPosition == adapter.getItemCount() - 1 && position == 0) {
                    onFinishedLoop();
                }
            }
            lastScrollPosition = position;
        }
    }

    protected void onFinishedLoop() {
        CqrLog.d("finished loop");
    }

    protected interface Listener<Item> {
        void onSelected(WheelView wheelView, int position, Item item);

        void onCurrentScrolled(WheelView wheelView, int position, Item item);
    }

}
