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
import android.graphics.Typeface;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import cn.qqtheme.framework.toolkit.CqrDensity;
import cn.qqtheme.framework.wheelview.R;
import cn.qqtheme.framework.wheelview.adapter.WheelAdapter;
import cn.qqtheme.framework.wheelview.interfaces.OnWheelChangedListener;
import cn.qqtheme.framework.wheelview.interfaces.OnWheelSelectedListener;
import cn.qqtheme.framework.wheelview.interfaces.TextProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * 滚轮控件，部分代码参考 https://github.com/florent37/SingleDateAndTimePicker#WheelPicker
 *
 * @param <T> 泛型除了{@link CharSequence}及其子类，需要实现{@link TextProvider}
 *            或者重载{@link Object#toString()}提供显示文本
 * @author liyujiang
 * @date 2019/5/8 11:11
 * @see WheelAdapter
 * @see TextProvider
 * @see OnWheelSelectedListener
 * @see OnWheelChangedListener
 */
@SuppressWarnings({"unused"})
public class WheelView<T> extends View implements Runnable {

    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_SCROLLING = 2;
    public static final int ALIGN_CENTER = 0;
    public static final int ALIGN_LEFT = 1;
    public static final int ALIGN_RIGHT = 2;

    protected T defaultItem;
    protected int lastScrollPosition;
    protected Listener<T> listener;
    protected WheelAdapter<T> adapter = new WheelAdapter<>();

    private final Handler handler = new Handler();
    private Paint paint;
    private Scroller scroller;
    private VelocityTracker tracker;
    private OnWheelSelectedListener onWheelListener;
    private OnWheelChangedListener wheelChangedListener;
    private final Rect rectDrawn = new Rect();
    private final Rect rectIndicatorHead = new Rect();
    private final Rect rectIndicatorFoot = new Rect();
    private final Rect rectCurrentItem = new Rect();
    private final Camera camera = new Camera();
    private final Matrix matrixRotate = new Matrix();
    private final Matrix matrixDepth = new Matrix();
    private String maxWidthText;

    private int mVisibleItemCount, mDrawnItemCount;
    private int mHalfDrawnItemCount;
    private int mTextMaxWidth, mTextMaxHeight;
    private int mItemTextColor, mSelectedItemTextColor;
    private int mItemTextSize;
    private int mIndicatorSize;
    private int mIndicatorColor;
    private int mCurtainColor;
    private int mItemSpace;
    private int mItemAlign;
    private int mItemHeight, mHalfItemHeight;
    private int mHalfWheelHeight;
    private int defaultItemPosition;
    private int currentItemPosition;
    private int minFlingY, maxFlingY;
    private int minimumVelocity = 50, maximumVelocity = 8000;
    private int wheelCenterX, wheelCenterY;
    private int drawnCenterX, drawnCenterY;
    private int scrollOffsetY;
    private int textMaxWidthPosition;
    private int lastPointY;
    private int downPointY;
    private int touchSlop = 8;

    private boolean hasSameWidth;
    private boolean hasIndicator;
    private boolean hasCurtain;
    private boolean hasAtmospheric;
    private boolean isCyclic;
    private boolean isCurved;

    private boolean isClick;
    private boolean isForceFinishScroll;

    public WheelView(Context context) {
        this(context, null);
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WheelView);

        mItemTextSize = a.getDimensionPixelSize(R.styleable.WheelView_wheel_item_text_size, CqrDensity.sp2px(context, 20));
        mVisibleItemCount = a.getInt(R.styleable.WheelView_wheel_visible_item_count, 7);
        defaultItemPosition = a.getInt(R.styleable.WheelView_wheel_default_item_position, 0);
        hasSameWidth = a.getBoolean(R.styleable.WheelView_wheel_same_width, false);
        textMaxWidthPosition = a.getInt(R.styleable.WheelView_wheel_maximum_width_text_position, -1);
        maxWidthText = a.getString(R.styleable.WheelView_wheel_maximum_width_text);
        mSelectedItemTextColor = a.getColor(R.styleable.WheelView_wheel_item_text_color_selected, -1);
        mItemTextColor = a.getColor(R.styleable.WheelView_wheel_item_text_color, 0xFF888888);
        mItemSpace = a.getDimensionPixelSize(R.styleable.WheelView_wheel_item_space, CqrDensity.dp2px(context, 15));
        isCyclic = a.getBoolean(R.styleable.WheelView_wheel_cyclic, false);
        hasIndicator = a.getBoolean(R.styleable.WheelView_wheel_indicator, false);
        mIndicatorColor = a.getColor(R.styleable.WheelView_wheel_indicator_color, 0xFFEE3333);
        mIndicatorSize = a.getDimensionPixelSize(R.styleable.WheelView_wheel_indicator_size, CqrDensity.sp2px(context, 1));
        hasCurtain = a.getBoolean(R.styleable.WheelView_wheel_curtain, false);
        mCurtainColor = a.getColor(R.styleable.WheelView_wheel_curtain_color, 0x88FFFFFF);
        hasAtmospheric = a.getBoolean(R.styleable.WheelView_wheel_atmospheric, false);
        isCurved = a.getBoolean(R.styleable.WheelView_wheel_curved, false);
        mItemAlign = a.getInt(R.styleable.WheelView_wheel_item_align, ALIGN_CENTER);
        a.recycle();

        updateVisibleItemCount();

        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
        paint.setTextSize(mItemTextSize);

        scroller = new Scroller(getContext());

        ViewConfiguration conf = ViewConfiguration.get(getContext());
        minimumVelocity = conf.getScaledMinimumFlingVelocity();
        maximumVelocity = conf.getScaledMaximumFlingVelocity();
        touchSlop = conf.getScaledTouchSlop();

        init();
        defaultItem = assignDefault();
        adapter.setData(generateData());
        currentItemPosition = adapter.getItemPosition(defaultItem);
        defaultItemPosition = currentItemPosition;
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
        if (mVisibleItemCount < 2) {
            throw new ArithmeticException("Wheel's visible item count can not be less than 2!");
        }

        if (mVisibleItemCount % 2 == 0) {
            mVisibleItemCount += 1;
        }
        mDrawnItemCount = mVisibleItemCount + 2;
        mHalfDrawnItemCount = mDrawnItemCount / 2;
    }

    private void computeTextSize() {
        mTextMaxWidth = mTextMaxHeight = 0;
        if (hasSameWidth) {
            mTextMaxWidth = (int) paint.measureText(adapter.getItemText(0));
        } else if (isPosInRang(textMaxWidthPosition)) {
            mTextMaxWidth = (int) paint.measureText(adapter.getItemText(textMaxWidthPosition));
        } else if (!TextUtils.isEmpty(maxWidthText)) {
            mTextMaxWidth = (int) paint.measureText(maxWidthText);
        } else {
            final int itemCount = adapter.getItemCount();
            for (int i = 0; i < itemCount; ++i) {
                String text = adapter.getItemText(i);
                int width = (int) paint.measureText(text);
                mTextMaxWidth = Math.max(mTextMaxWidth, width);
            }
        }
        final Paint.FontMetrics metrics = paint.getFontMetrics();
        mTextMaxHeight = (int) (metrics.bottom - metrics.top);
    }

    private void updateItemTextAlign() {
        switch (mItemAlign) {
            case ALIGN_LEFT:
                paint.setTextAlign(Paint.Align.LEFT);
                break;
            case ALIGN_RIGHT:
                paint.setTextAlign(Paint.Align.RIGHT);
                break;
            default:
                paint.setTextAlign(Paint.Align.CENTER);
                break;
        }
    }

    public void setListener(Listener<T> listener) {
        this.listener = listener;
    }

    public int getCurrentItemPosition() {
        return currentItemPosition;
    }

    public T getCurrentItem() {
        return adapter.getItem(currentItemPosition);
    }

    public int getVisibleItemCount() {
        return mVisibleItemCount;
    }

    public void setVisibleItemCount(int count) {
        mVisibleItemCount = count;
        updateVisibleItemCount();
        requestLayout();
    }

    public boolean isCyclic() {
        return isCyclic;
    }

    public void setCyclic(boolean isCyclic) {
        this.isCyclic = isCyclic;
        computeFlingLimitY();
        invalidate();
    }

    public void setOnWheelListener(OnWheelSelectedListener listener) {
        onWheelListener = listener;
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
        scrollOffsetY = 0;
        computeFlingLimitY();
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

    public void setWheelChangedListener(OnWheelChangedListener listener) {
        wheelChangedListener = listener;
    }

    public String getMaximumWidthText() {
        return maxWidthText;
    }

    public void setMaximumWidthText(String text) {
        if (null == text) {
            throw new NullPointerException("Maximum width text can not be null!");
        }
        maxWidthText = text;
        computeTextSize();
        requestLayout();
        postInvalidate();
    }

    public int getMaximumWidthTextPosition() {
        return textMaxWidthPosition;
    }

    public void setMaximumWidthTextPosition(int position) {
        if (!isPosInRang(position)) {
            throw new ArrayIndexOutOfBoundsException("Maximum width text Position must in [0, " +
                    adapter.getItemCount() + "), but current is " + position);
        }
        textMaxWidthPosition = position;
        computeTextSize();
        requestLayout();
        postInvalidate();
    }

    public int getSelectedItemTextColor() {
        return mSelectedItemTextColor;
    }

    public void setSelectedItemTextColor(int color) {
        mSelectedItemTextColor = color;
        computeCurrentItemRect();
        postInvalidate();
    }

    public int getItemTextColor() {
        return mItemTextColor;
    }

    public void setItemTextColor(int color) {
        mItemTextColor = color;
        postInvalidate();
    }

    public int getItemTextSize() {
        return mItemTextSize;
    }

    public void setItemTextSize(int size) {
        if (mItemTextSize != size) {
            mItemTextSize = size;
            paint.setTextSize(mItemTextSize);
            computeTextSize();
            requestLayout();
            postInvalidate();
        }
    }

    public int getItemSpace() {
        return mItemSpace;
    }

    public void setItemSpace(int space) {
        mItemSpace = space;
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
        return mIndicatorSize;
    }

    public void setIndicatorSize(int size) {
        mIndicatorSize = size;
        computeIndicatorRect();
        postInvalidate();
    }

    public int getIndicatorColor() {
        return mIndicatorColor;
    }

    public void setIndicatorColor(int color) {
        mIndicatorColor = color;
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

    public int getCurtainColor() {
        return mCurtainColor;
    }

    public void setCurtainColor(int color) {
        mCurtainColor = color;
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

    public int getItemAlign() {
        return mItemAlign;
    }

    public void setItemAlign(int align) {
        mItemAlign = align;
        updateItemTextAlign();
        computeDrawnCenter();
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
        if (defaultItemPosition > adapter.getItemCount() - 1
                || currentItemPosition > adapter.getItemCount() - 1) {
            defaultItemPosition = currentItemPosition = adapter.getItemCount() - 1;
        } else {
            defaultItemPosition = currentItemPosition;
        }
        scrollOffsetY = 0;
        computeTextSize();
        computeFlingLimitY();
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
        int resultWidth = mTextMaxWidth;
        int resultHeight = mTextMaxHeight * mVisibleItemCount + mItemSpace * (mVisibleItemCount - 1);

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
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        // Set content region
        rectDrawn.set(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(),
                getHeight() - getPaddingBottom());

        // Get the center coordinates of content region
        wheelCenterX = rectDrawn.centerX();
        wheelCenterY = rectDrawn.centerY();

        // Correct item drawn center
        computeDrawnCenter();

        mHalfWheelHeight = rectDrawn.height() / 2;

        mItemHeight = rectDrawn.height() / mVisibleItemCount;
        mHalfItemHeight = mItemHeight / 2;

        // Initialize fling max Y-coordinates
        computeFlingLimitY();

        // Correct region of indicator
        computeIndicatorRect();

        // Correct region of current select item
        computeCurrentItemRect();
    }

    private void computeDrawnCenter() {
        switch (mItemAlign) {
            case ALIGN_LEFT:
                drawnCenterX = rectDrawn.left;
                break;
            case ALIGN_RIGHT:
                drawnCenterX = rectDrawn.right;
                break;
            default:
                drawnCenterX = wheelCenterX;
                break;
        }
        drawnCenterY = (int) (wheelCenterY - ((paint.ascent() + paint.descent()) / 2));
    }

    private void computeFlingLimitY() {
        int currentItemOffset = defaultItemPosition * mItemHeight;
        minFlingY = isCyclic ? Integer.MIN_VALUE
                : -mItemHeight * (adapter.getItemCount() - 1) + currentItemOffset;
        maxFlingY = isCyclic ? Integer.MAX_VALUE : currentItemOffset;
    }

    private void computeIndicatorRect() {
        if (!hasIndicator) {
            return;
        }
        int halfIndicatorSize = mIndicatorSize / 2;
        int indicatorHeadCenterY = wheelCenterY + mHalfItemHeight;
        int indicatorFootCenterY = wheelCenterY - mHalfItemHeight;
        rectIndicatorHead.set(rectDrawn.left, indicatorHeadCenterY - halfIndicatorSize, rectDrawn.right,
                indicatorHeadCenterY + halfIndicatorSize);
        rectIndicatorFoot.set(rectDrawn.left, indicatorFootCenterY - halfIndicatorSize, rectDrawn.right,
                indicatorFootCenterY + halfIndicatorSize);
    }

    private void computeCurrentItemRect() {
        if (!hasCurtain && mSelectedItemTextColor == -1) {
            return;
        }
        rectCurrentItem.set(rectDrawn.left, wheelCenterY - mHalfItemHeight, rectDrawn.right,
                wheelCenterY + mHalfItemHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (null != wheelChangedListener) {
            wheelChangedListener.onWheelScrolled(scrollOffsetY);
        }
        if (mItemHeight - mHalfDrawnItemCount <= 0) {
            return;
        }
        int drawnDataStartPos = -scrollOffsetY / mItemHeight - mHalfDrawnItemCount;
        for (int drawnDataPos = drawnDataStartPos + defaultItemPosition,
             drawnOffsetPos = -mHalfDrawnItemCount;
             drawnDataPos < drawnDataStartPos + defaultItemPosition + mDrawnItemCount;
             drawnDataPos++, drawnOffsetPos++) {
            String data = "";
            if (isCyclic) {
                final int itemCount = adapter.getItemCount();
                if (itemCount != 0) {
                    int actualPos = drawnDataPos % itemCount;
                    actualPos = actualPos < 0 ? (actualPos + itemCount) : actualPos;
                    data = adapter.getItemText(actualPos);
                }
            } else {
                if (isPosInRang(drawnDataPos)) {
                    data = adapter.getItemText(drawnDataPos);
                }
            }
            paint.setColor(mItemTextColor);
            paint.setStyle(Paint.Style.FILL);
            int mDrawnItemCenterY = drawnCenterY + (drawnOffsetPos * mItemHeight) +
                    scrollOffsetY % mItemHeight;

            int distanceToCenter = 0;
            int abs = Math.abs(drawnCenterY - mDrawnItemCenterY);
            if (isCurved) {
                // Correct ratio of item's drawn center to wheel center
                float ratio = (drawnCenterY - abs -
                        rectDrawn.top) * 1.0F / (drawnCenterY - rectDrawn.top);

                // Correct unit
                int unit = 0;
                if (mDrawnItemCenterY > drawnCenterY) {
                    unit = 1;
                } else if (mDrawnItemCenterY < drawnCenterY) {
                    unit = -1;
                }

                float degree = (-(1 - ratio) * 90 * unit);
                if (degree < -90) {
                    degree = -90;
                }
                if (degree > 90) {
                    degree = 90;
                }
                distanceToCenter = computeSpace((int) degree);

                int transX = wheelCenterX;
                switch (mItemAlign) {
                    case ALIGN_LEFT:
                        transX = rectDrawn.left;
                        break;
                    case ALIGN_RIGHT:
                        transX = rectDrawn.right;
                        break;
                    default:
                        break;
                }
                int transY = wheelCenterY - distanceToCenter;

                camera.save();
                camera.rotateX(degree);
                camera.getMatrix(matrixRotate);
                camera.restore();
                matrixRotate.preTranslate(-transX, -transY);
                matrixRotate.postTranslate(transX, transY);

                camera.save();
                camera.translate(0, 0, computeDepth((int) degree));
                camera.getMatrix(matrixDepth);
                camera.restore();
                matrixDepth.preTranslate(-transX, -transY);
                matrixDepth.postTranslate(transX, transY);

                matrixRotate.postConcat(matrixDepth);
            }
            if (hasAtmospheric) {
                int alpha = (int) ((drawnCenterY - abs) * 1.0F / drawnCenterY * 255);
                alpha = alpha < 0 ? 0 : alpha;
                paint.setAlpha(alpha);
            }
            // Correct item's drawn centerY base on curved state
            int drawnCenterY = isCurved ? this.drawnCenterY - distanceToCenter : mDrawnItemCenterY;

            // Judges need to draw different color for current item or not
            if (mSelectedItemTextColor != -1) {
                canvas.save();
                if (isCurved) {
                    canvas.concat(matrixRotate);
                }
                canvas.clipRect(rectCurrentItem, android.graphics.Region.Op.DIFFERENCE);
                canvas.drawText(data, drawnCenterX, drawnCenterY, paint);
                canvas.restore();

                paint.setColor(mSelectedItemTextColor);
                canvas.save();
                if (isCurved) {
                    canvas.concat(matrixRotate);
                }
                canvas.clipRect(rectCurrentItem);
                canvas.drawText(data, drawnCenterX, drawnCenterY, paint);
                canvas.restore();
            } else {
                canvas.save();
                canvas.clipRect(rectDrawn);
                if (isCurved) {
                    canvas.concat(matrixRotate);
                }
                canvas.drawText(data, drawnCenterX, drawnCenterY, paint);
                canvas.restore();
            }
        }
        // Need to draw curtain or not
        if (hasCurtain) {
            paint.setColor(mCurtainColor);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(rectCurrentItem, paint);
        }
        // Need to draw indicator or not
        if (hasIndicator) {
            paint.setColor(mIndicatorColor);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(rectIndicatorHead, paint);
            canvas.drawRect(rectIndicatorFoot, paint);
        }
    }

    private boolean isPosInRang(int position) {
        return position >= 0 && position < adapter.getItemCount();
    }

    private int computeSpace(int degree) {
        return (int) (Math.sin(Math.toRadians(degree)) * mHalfWheelHeight);
    }

    private int computeDepth(int degree) {
        return (int) (mHalfWheelHeight - Math.cos(Math.toRadians(degree)) * mHalfWheelHeight);
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
        if (null == tracker) {
            tracker = VelocityTracker.obtain();
        } else {
            tracker.clear();
        }
        tracker.addMovement(event);
        if (!scroller.isFinished()) {
            scroller.abortAnimation();
            isForceFinishScroll = true;
        }
        downPointY = lastPointY = (int) event.getY();
    }

    private void handleActionMove(MotionEvent event) {
        if (Math.abs(downPointY - event.getY()) < touchSlop
                && computeDistanceToEndPoint(scroller.getFinalY() % mItemHeight) > 0) {
            isClick = true;
            return;
        }
        isClick = false;
        tracker.addMovement(event);
        if (null != wheelChangedListener) {
            wheelChangedListener.onWheelScrollStateChanged(SCROLL_STATE_DRAGGING);
        }

        // Scroll WheelPicker's content
        float move = event.getY() - lastPointY;
        if (Math.abs(move) < 1) {
            return;
        }
        scrollOffsetY += move;
        lastPointY = (int) event.getY();
        invalidate();
    }

    private void handleActionUp(MotionEvent event) {
        if (null != getParent()) {
            getParent().requestDisallowInterceptTouchEvent(false);
        }
        if (isClick) {
            return;
        }
        tracker.addMovement(event);
        tracker.computeCurrentVelocity(1000, maximumVelocity);

        // Judges the WheelPicker is scroll or fling base on current velocity
        isForceFinishScroll = false;
        int velocity = (int) tracker.getYVelocity();
        if (Math.abs(velocity) > minimumVelocity) {
            scroller.fling(0, scrollOffsetY, 0, velocity, 0, 0, minFlingY, maxFlingY);
            scroller.setFinalY(
                    scroller.getFinalY() + computeDistanceToEndPoint(scroller.getFinalY() % mItemHeight));
        } else {
            scroller.startScroll(0, scrollOffsetY, 0,
                    computeDistanceToEndPoint(scrollOffsetY % mItemHeight));
        }
        // Correct coordinates
        if (!isCyclic) {
            if (scroller.getFinalY() > maxFlingY) {
                scroller.setFinalY(maxFlingY);
            } else if (scroller.getFinalY() < minFlingY) {
                scroller.setFinalY(minFlingY);
            }
        }
        handler.post(this);
        if (null != tracker) {
            tracker.recycle();
            tracker = null;
        }
    }

    private void handleActionCancel(MotionEvent event) {
        if (null != getParent()) {
            getParent().requestDisallowInterceptTouchEvent(false);
        }
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
        if (Math.abs(remainder) > mHalfItemHeight) {
            if (scrollOffsetY < 0) {
                return -mItemHeight - remainder;
            } else {
                return mItemHeight - remainder;
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
        if (scroller.isFinished() && !isForceFinishScroll) {
            if (mItemHeight == 0) {
                return;
            }
            int position = (-scrollOffsetY / mItemHeight + defaultItemPosition) % itemCount;
            position = position < 0 ? position + itemCount : position;
            currentItemPosition = position;
            onItemSelected();
            if (null != wheelChangedListener) {
                wheelChangedListener.onWheelSelected(position);
                wheelChangedListener.onWheelScrollStateChanged(SCROLL_STATE_IDLE);
            }
        }
        if (scroller.computeScrollOffset()) {
            if (null != wheelChangedListener) {
                wheelChangedListener.onWheelScrollStateChanged(SCROLL_STATE_SCROLLING);
            }

            scrollOffsetY = scroller.getCurrY();

            int position = (-scrollOffsetY / mItemHeight + defaultItemPosition) % itemCount;
            if (onWheelListener != null) {
                onWheelListener.onCurrentItemOfScroll(this, position);
            }
            onItemCurrentScroll(position, adapter.getItem(position));

            postInvalidate();
            handler.postDelayed(this, 16);
        }
    }

    public void scrollTo(final int itemPosition) {
        if (itemPosition != currentItemPosition) {
            final int differencesLines = currentItemPosition - itemPosition;
            final int newScrollOffsetY = scrollOffsetY + (differencesLines * mItemHeight);

            ValueAnimator va = ValueAnimator.ofInt(scrollOffsetY, newScrollOffsetY);
            va.setDuration(300);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    scrollOffsetY = (int) animation.getAnimatedValue();
                    invalidate();
                }
            });
            va.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    currentItemPosition = itemPosition;
                    onItemSelected();
                }
            });
            va.start();
        }
    }

    protected final void onItemSelected() {
        int position = currentItemPosition;
        final T item = this.adapter.getItem(position);
        if (null != onWheelListener) {
            onWheelListener.onItemSelected(this, position);
        }
        onItemSelected(position, item);
    }

    protected void onItemSelected(int position, T item) {
        if (listener != null) {
            listener.onSelected(this, position, item);
        }
    }

    protected void onItemCurrentScroll(int position, T item) {
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

    }

    protected interface Listener<Item> {
        void onSelected(WheelView<Item> wheelView, int position, Item item);

        void onCurrentScrolled(WheelView<Item> wheelView, int position, Item item);
    }

}
