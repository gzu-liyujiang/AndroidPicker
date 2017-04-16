package cn.qqtheme.framework.widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

import cn.qqtheme.framework.entity.WheelItem;
import cn.qqtheme.framework.util.ConvertUtils;
import cn.qqtheme.framework.util.LogUtils;
import cn.qqtheme.framework.wheelpicker.R;


/**
 * 滑轮控件，参见：https://github.com/weidongjian/androidWheelView
 * <br />
 * Author:李玉江[QQ:1032694760]
 * DateTime:2015/12/15 09:45 基于ScrollView
 * DateTime:2017/01/07 21:37 基于ListView
 * DateTime:2017/04/16 20:10 基于View
 * Builder:Android Studio
 *
 * @see WheelItem
 * @see DividerConfig
 * @see OnItemSelectListener
 */
public class WheelView extends View {
    public static final long SECTION_DELAY = 200L;
    public static final int TEXT_SIZE = 16;//sp
    public static final int TEXT_COLOR_FOCUS = 0XFF0288CE;
    public static final int TEXT_COLOR_NORMAL = 0XFFBBBBBB;
    public static final int LINE_ALPHA = 220;
    public static final int LINE_COLOR = 0XFF83CDE6;
    public static final float LINE_THICK = 1f;//px
    public static final float LINE_SPACE = 2f;
    public static final int ITEM_OFF_SET = 3;
    private static final int CLICK = 0;
    private static final int FLING = 1;
    private static final int DANGLE = 2;

    private MessageHandler handler;
    private GestureDetector gestureDetector;
    private OnItemSelectListener onItemSelectListener;
    private OnWheelListener onWheelListener;
    private ScheduledExecutorService mExecutor = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> mFuture;
    private Paint paintOuterText;
    private Paint paintCenterText;
    private Paint paintIndicator;
    private List<WheelItem> items = new ArrayList<>();
    private float scaleX = 1.05F;
    private int textSize;
    private int maxTextHeight;
    private int outerTextColor;
    private int centerTextColor;
    private DividerConfig dividerConfig = new DividerConfig();
    private float lineSpacingMultiplier;
    private boolean isLoop;
    private int firstLineY;
    private int secondLineY;
    private int totalScrollY;
    private float inertiaValue = Float.MAX_VALUE;
    private int initPosition = 0;
    private int selectedIndex = 0;
    private int preCurrentIndex;
    private int itemsVisibleCount;
    private String[] drawingStrings;
    private int measuredWidth;
    private int halfCircumference;
    private int radius;
    private int realTotalOffset = Integer.MAX_VALUE;
    private int mOffset = 0;
    private float previousY;
    private long startTime = 0;
    private Rect tempRect = new Rect();
    private int paddingLeft;
    private boolean isUserScroll = false;//是否用户手动滚动

    public WheelView(Context context) {
        super(context);
        init(context, null);
    }

    public WheelView(Context context, AttributeSet attributeset) {
        super(context, attributeset);
        init(context, attributeset);
    }

    public WheelView(Context context, AttributeSet attributeset, int defStyleAttr) {
        super(context, attributeset, defStyleAttr);
        init(context, attributeset);
    }

    private void init(Context context, AttributeSet attributeset) {
        handler = new MessageHandler(this);
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public final boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                scrollBy(velocityY);
                return true;
            }
        });
        gestureDetector.setIsLongpressEnabled(false);

        TypedArray typedArray = context.obtainStyledAttributes(attributeset, R.styleable.androidWheelView);
        textSize = typedArray.getInteger(R.styleable.androidWheelView_awv_textsize, TEXT_SIZE);
        textSize = (int) (Resources.getSystem().getDisplayMetrics().density * textSize);
        lineSpacingMultiplier = typedArray.getFloat(R.styleable.androidWheelView_awv_lineSpace, LINE_SPACE);
        centerTextColor = typedArray.getInteger(R.styleable.androidWheelView_awv_centerTextColor, TEXT_COLOR_FOCUS);
        outerTextColor = typedArray.getInteger(R.styleable.androidWheelView_awv_outerTextColor, TEXT_COLOR_NORMAL);
        int dividerColor = typedArray.getInteger(R.styleable.androidWheelView_awv_dividerTextColor, LINE_COLOR);
        dividerConfig.setColor(dividerColor);
        itemsVisibleCount = typedArray.getInteger(R.styleable.androidWheelView_awv_itemsVisibleCount, 7);
        if (itemsVisibleCount % 2 == 0) {
            itemsVisibleCount = ITEM_OFF_SET * 2 + 1;
        }
        isLoop = typedArray.getBoolean(R.styleable.androidWheelView_awv_isLoop, true);
        typedArray.recycle();

        drawingStrings = new String[itemsVisibleCount];
        totalScrollY = 0;
        initPosition = -1;

        initPaints();
        initDataForIDE();
    }

    private void initPaints() {
        paintOuterText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintOuterText.setColor(outerTextColor);
        paintOuterText.setTypeface(Typeface.DEFAULT);
        paintOuterText.setTextSize(textSize);

        paintCenterText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCenterText.setColor(centerTextColor);
        paintCenterText.setTextScaleX(scaleX);
        paintCenterText.setTypeface(Typeface.DEFAULT);
        paintCenterText.setTextSize(textSize);

        paintIndicator = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintIndicator.setColor(dividerConfig.getColor());
        paintIndicator.setStrokeWidth(dividerConfig.getThick());
        paintIndicator.setAlpha(dividerConfig.getAlpha());
    }

    private void initDataForIDE() {
        if (isInEditMode()) {
            setItems(new String[]{"李玉江", "男", "贵州", "穿青人"});
        }
    }

    private void remeasure() {
        if (items == null) {
            return;
        }
        measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        if (measuredWidth == 0 || measuredHeight == 0) {
            return;
        }

        paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        measuredWidth = measuredWidth - paddingRight;
        paintCenterText.getTextBounds("测试", 0, 2, tempRect);
        maxTextHeight = tempRect.height();
        halfCircumference = (int) (measuredHeight * Math.PI / 2);
        maxTextHeight = (int) (halfCircumference / (lineSpacingMultiplier * (itemsVisibleCount - 1)));
        radius = measuredHeight / 2;
        firstLineY = (int) ((measuredHeight - lineSpacingMultiplier * maxTextHeight) / 2.0F);
        secondLineY = (int) ((measuredHeight + lineSpacingMultiplier * maxTextHeight) / 2.0F);
        if (initPosition == -1) {
            if (isLoop) {
                initPosition = (items.size() + 1) / 2;
            } else {
                initPosition = 0;
            }
        }
        preCurrentIndex = initPosition;
    }

    private void smoothScroll(int action) {
        cancelFuture();
        if (action == FLING || action == DANGLE) {
            float itemHeight = lineSpacingMultiplier * maxTextHeight;
            mOffset = (int) ((totalScrollY % itemHeight + itemHeight) % itemHeight);
            if ((float) mOffset > itemHeight / 2.0F) {
                mOffset = (int) (itemHeight - (float) mOffset);
            } else {
                mOffset = -mOffset;
            }
        }
        mFuture = mExecutor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                if (realTotalOffset == Integer.MAX_VALUE) {
                    realTotalOffset = mOffset;
                }
                int realOffset = (int) ((float) realTotalOffset * 0.1F);
                if (realOffset == 0) {
                    if (realTotalOffset < 0) {
                        realOffset = -1;
                    } else {
                        realOffset = 1;
                    }
                }
                if (Math.abs(realTotalOffset) <= 0) {
                    cancelFuture();
                    handler.sendEmptyMessage(MessageHandler.WHAT_ITEM_SELECTED);
                } else {
                    totalScrollY = totalScrollY + realOffset;
                    handler.sendEmptyMessage(MessageHandler.WHAT_INVALIDATE_LOOP_VIEW);
                    realTotalOffset = realTotalOffset - realOffset;
                }
            }
        }, 0, 10, TimeUnit.MILLISECONDS);
    }

    private void scrollBy(final float velocityY) {
        cancelFuture();
        Runnable command = new Runnable() {
            @Override
            public void run() {
                if (inertiaValue == Integer.MAX_VALUE) {
                    if (Math.abs(velocityY) > 2000F) {
                        if (velocityY > 0.0F) {
                            inertiaValue = 2000F;
                        } else {
                            inertiaValue = -2000F;
                        }
                    } else {
                        inertiaValue = velocityY;
                    }
                }
                if (Math.abs(inertiaValue) >= 0.0F && Math.abs(inertiaValue) <= 20F) {
                    cancelFuture();
                    handler.sendEmptyMessage(MessageHandler.WHAT_SMOOTH_SCROLL);
                    return;
                }
                int i = (int) ((inertiaValue * 10F) / 1000F);
                totalScrollY = totalScrollY - i;
                if (!isLoop) {
                    float itemHeight = lineSpacingMultiplier * maxTextHeight;
                    if (totalScrollY <= (int) ((float) (-initPosition) * itemHeight)) {
                        inertiaValue = 40F;
                        totalScrollY = (int) ((float) (-initPosition) * itemHeight);
                    } else if (totalScrollY >= (int) ((float) (items.size() - 1 - initPosition) * itemHeight)) {
                        totalScrollY = (int) ((float) (items.size() - 1 - initPosition) * itemHeight);
                        inertiaValue = -40F;
                    }
                }
                if (inertiaValue < 0.0F) {
                    inertiaValue = inertiaValue + 20F;
                } else {
                    inertiaValue = inertiaValue - 20F;
                }
                handler.sendEmptyMessage(MessageHandler.WHAT_INVALIDATE_LOOP_VIEW);
            }
        };
        // change this number, can change fling speed
        int velocityFling = 10;
        mFuture = mExecutor.scheduleWithFixedDelay(command, 0, velocityFling, TimeUnit.MILLISECONDS);
    }

    private void onSelectedCallback() {
        if (onItemSelectListener == null && onWheelListener == null) {
            return;
        }
        postDelayed(new Runnable() {
            @Override
            public void run() {
                WheelItem selectedItem = items.get(selectedIndex);
                LogUtils.verbose("isUserScroll=" + isUserScroll + ", selectedItem=" + selectedItem);
                if (onItemSelectListener != null) {
                    onItemSelectListener.onSelected(isUserScroll, selectedIndex, selectedItem);
                }
                if (onWheelListener != null) {
                    onWheelListener.onSelected(isUserScroll, selectedIndex, selectedItem.getName());
                }
            }
        }, SECTION_DELAY);
    }

    private void cancelFuture() {
        if (mFuture != null && !mFuture.isCancelled()) {
            mFuture.cancel(true);
            mFuture = null;
        }
    }

    private int getTextX(String a, Paint paint, Rect rect) {
        paint.getTextBounds(a, 0, a.length(), rect);
        int textWidth = rect.width();
        textWidth *= scaleX;
        return (measuredWidth - paddingLeft - textWidth) / 2 + paddingLeft;
    }

    /**
     * 设置滚轮个数偏移量
     */
    public void setOffset(@IntRange(from = 1, to = 3) int offset) {
        if (offset < 1 || offset > 3) {
            throw new IllegalArgumentException("must between 1 and 3");
        }
        int visibleNumber = offset * 2 + 1;
        if (visibleNumber % 2 == 0) {
            throw new IllegalArgumentException("must be odd");
        }
        if (visibleNumber != itemsVisibleCount) {
            itemsVisibleCount = visibleNumber;
            drawingStrings = new String[itemsVisibleCount];
        }
    }

    public void setLineSpacingMultiplier(float lineSpacingMultiplier) {
        if (lineSpacingMultiplier > 1.0f) {
            this.lineSpacingMultiplier = lineSpacingMultiplier;
        }
    }

    public void setTextColor(@ColorInt int colorNormal, @ColorInt int colorFocus) {
        this.outerTextColor = colorNormal;
        this.centerTextColor = colorFocus;
        paintOuterText.setColor(outerTextColor);
        paintCenterText.setColor(centerTextColor);
    }

    public void setTextColor(@ColorInt int color) {
        this.outerTextColor = color;
        this.centerTextColor = color;
        paintOuterText.setColor(outerTextColor);
        paintCenterText.setColor(centerTextColor);
    }

    public void setDividerColor(@ColorInt int color) {
        dividerConfig.setColor(color);
        paintIndicator.setColor(color);
    }

    /**
     * @deprecated use {{@link #setDividerConfig(DividerConfig)} instead
     */
    @Deprecated
    public void setLineConfig(DividerConfig config) {
        setDividerConfig(config);
    }

    public void setDividerConfig(DividerConfig config) {
        if (null == config) {
            dividerConfig.setVisible(false);
            dividerConfig.setShadowVisible(false);
            return;
        }
        this.dividerConfig = config;
        paintIndicator.setColor(config.getColor());
        paintIndicator.setStrokeWidth(config.getThick());
        paintIndicator.setAlpha(config.getAlpha());
    }

    public final void setTextSize(@IntRange(from = 0, to = 100) int size) {
        textSize = ConvertUtils.toSp(getContext(), size);
        paintOuterText.setTextSize(textSize);
        paintCenterText.setTextSize(textSize);
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    /**
     * 设置滚轮是否禁用循环滚动
     */
    public void setCycleDisable(boolean cycleDisable) {
        isLoop = !cycleDisable;
    }

    public final void setItems(List<?> items) {
        for (Object item : items) {
            if (item instanceof WheelItem) {
                this.items.add((WheelItem) item);
            } else if (item instanceof CharSequence || item instanceof Number) {
                this.items.add(new StringItem(item.toString()));
            } else {
                throw new IllegalArgumentException("please implements " + WheelItem.class.getName());
            }
        }
        remeasure();
        invalidate();
    }

    public final void setItems(List<?> items, int index) {
        setItems(items);
        setSelectedIndex(index);
    }

    public final void setItems(String[] list) {
        setItems(Arrays.asList(list));
    }

    public final void setItems(List<String> list, String item) {
        int index = list.indexOf(item);
        if (index == -1) {
            index = 0;
        }
        setItems(list, index);
    }

    public final void setItems(String[] list, int index) {
        setItems(Arrays.asList(list), index);
    }

    public final void setItems(String[] items, String item) {
        setItems(Arrays.asList(items), item);
    }

    public final void setSelectedIndex(int index) {
        if (items == null || items.isEmpty()) {
            return;
        }
        int size = items.size();
        if (index >= 0 && index < size && index != selectedIndex) {
            initPosition = index;
            totalScrollY = 0;
            mOffset = 0;
            invalidate();
        }
    }

    public final int getSelectedIndex() {
        return selectedIndex;
    }

    public final void setOnItemSelectListener(OnItemSelectListener listener) {
        onItemSelectListener = listener;
    }

    /**
     * @deprecated use {@link #setOnItemSelectListener(OnItemSelectListener)} instead
     */
    @Deprecated
    public final void setOnWheelListener(OnWheelListener listener) {
        onWheelListener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        isUserScroll = false;
        if (items == null) {
            return;
        }

        float itemHeight = maxTextHeight * lineSpacingMultiplier;
        int change = (int) (totalScrollY / itemHeight);
        preCurrentIndex = initPosition + change % items.size();

        if (!isLoop) {
            if (preCurrentIndex < 0) {
                preCurrentIndex = 0;
            }
            if (preCurrentIndex > items.size() - 1) {
                preCurrentIndex = items.size() - 1;
            }
        } else {
            if (preCurrentIndex < 0) {
                preCurrentIndex = items.size() + preCurrentIndex;
            }
            if (preCurrentIndex > items.size() - 1) {
                preCurrentIndex = preCurrentIndex - items.size();
            }
        }

        int visibleIndex = 0;
        while (visibleIndex < itemsVisibleCount) {
            int index = preCurrentIndex - (itemsVisibleCount / 2 - visibleIndex);
            if (isLoop) {
                while (index < 0) {
                    index = index + items.size();
                }
                while (index > items.size() - 1) {
                    index = index - items.size();
                }
                drawingStrings[visibleIndex] = items.get(index).getName();
            } else if (index < 0) {
                drawingStrings[visibleIndex] = "";
            } else if (index > items.size() - 1) {
                drawingStrings[visibleIndex] = "";
            } else {
                drawingStrings[visibleIndex] = items.get(index).getName();
            }
            visibleIndex++;
        }
        float ratio = dividerConfig.getRatio();
        canvas.drawLine(measuredWidth * ratio, firstLineY, measuredWidth * (1 - ratio), firstLineY, paintIndicator);
        canvas.drawLine(measuredWidth * ratio, secondLineY, measuredWidth * (1 - ratio), secondLineY, paintIndicator);
        //canvas.drawLine(paddingLeft, firstLineY, measuredWidth, firstLineY, paintIndicator);
        //canvas.drawLine(paddingLeft, secondLineY, measuredWidth, secondLineY, paintIndicator);

        int surplus = (int) (totalScrollY % itemHeight);
        int visiblePosition = 0;
        while (visiblePosition < itemsVisibleCount) {
            canvas.save();
            double radian = ((itemHeight * visiblePosition - surplus) * Math.PI) / halfCircumference;
            if (radian >= Math.PI || radian <= 0) {
                canvas.restore();
            } else {
                int translateY = (int) (radius - Math.cos(radian) * radius - (Math.sin(radian) * maxTextHeight) / 2D);
                canvas.translate(0.0F, translateY);
                canvas.scale(1.0F, (float) Math.sin(radian));
                String drawingString = drawingStrings[visiblePosition];
                if (translateY <= firstLineY && maxTextHeight + translateY >= firstLineY) {
                    // first divider
                    canvas.save();
                    canvas.clipRect(0, 0, measuredWidth, firstLineY - translateY);
                    canvas.drawText(drawingString, getTextX(drawingString, paintOuterText, tempRect),
                            maxTextHeight, paintOuterText);
                    canvas.restore();
                    canvas.save();
                    canvas.clipRect(0, firstLineY - translateY, measuredWidth, (int) (itemHeight));
                    canvas.drawText(drawingString, getTextX(drawingString, paintCenterText, tempRect),
                            maxTextHeight, paintCenterText);
                    canvas.restore();
                } else if (translateY <= secondLineY && maxTextHeight + translateY >= secondLineY) {
                    // second divider
                    canvas.save();
                    canvas.clipRect(0, 0, measuredWidth, secondLineY - translateY);
                    canvas.drawText(drawingString, getTextX(drawingString, paintCenterText, tempRect),
                            maxTextHeight, paintCenterText);
                    canvas.restore();
                    canvas.save();
                    canvas.clipRect(0, secondLineY - translateY, measuredWidth, (int) (itemHeight));
                    canvas.drawText(drawingString, getTextX(drawingString, paintOuterText, tempRect),
                            maxTextHeight, paintOuterText);
                    canvas.restore();
                } else if (translateY >= firstLineY && maxTextHeight + translateY <= secondLineY) {
                    // center item
                    canvas.clipRect(0, 0, measuredWidth, (int) (itemHeight));
                    canvas.drawText(drawingString, getTextX(drawingString, paintCenterText, tempRect),
                            maxTextHeight, paintCenterText);
                    int i = 0;
                    for (WheelItem item : items) {
                        if (item.getName().equals(drawingString)) {
                            selectedIndex = i;
                            break;
                        }
                        i++;
                    }
                } else {
                    // other item
                    canvas.clipRect(0, 0, measuredWidth, (int) (itemHeight));
                    canvas.drawText(drawingString, getTextX(drawingString, paintOuterText, tempRect),
                            maxTextHeight, paintOuterText);
                }
                canvas.restore();
            }
            visiblePosition++;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        remeasure();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        isUserScroll = true;//触发触摸事件，说明是用户在滚动
        boolean eventConsumed = gestureDetector.onTouchEvent(event);
        float itemHeight = lineSpacingMultiplier * maxTextHeight;
        ViewParent parent = getParent();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTime = System.currentTimeMillis();
                cancelFuture();
                previousY = event.getRawY();
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = previousY - event.getRawY();
                previousY = event.getRawY();
                totalScrollY = (int) (totalScrollY + dy);
                if (!isLoop) {
                    float top = -initPosition * itemHeight;
                    float bottom = (items.size() - 1 - initPosition) * itemHeight;
                    if (totalScrollY < top) {
                        totalScrollY = (int) top;
                    } else if (totalScrollY > bottom) {
                        totalScrollY = (int) bottom;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            default:
                if (!eventConsumed) {
                    float y = event.getY();
                    double l = Math.acos((radius - y) / radius) * radius;
                    int circlePosition = (int) ((l + itemHeight / 2) / itemHeight);
                    float extraOffset = (totalScrollY % itemHeight + itemHeight) % itemHeight;
                    mOffset = (int) ((circlePosition - itemsVisibleCount / 2) * itemHeight - extraOffset);
                    if ((System.currentTimeMillis() - startTime) > 120) {
                        smoothScroll(DANGLE);
                    } else {
                        smoothScroll(CLICK);
                    }
                }
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(false);
                }
                break;
        }
        invalidate();
        return true;
    }

    /**
     * 选中项的分割线
     */
    public static class DividerConfig {
        private boolean visible = true;
        private boolean shadowVisible = false;
        private int color = LINE_COLOR;
        private int alpha = LINE_ALPHA;
        private float ratio = (float) (1.0 / 6.0);
        private float thick = LINE_THICK;// px

        public DividerConfig() {
            super();
        }

        public DividerConfig(@FloatRange(from = 0, to = 1) float ratio) {
            this.ratio = ratio;
        }

        /**
         * 线是否可见
         */
        public void setVisible(boolean visible) {
            this.visible = visible;
        }

        public boolean isVisible() {
            return visible;
        }

        /**
         * 阴影是否可见
         */
        public void setShadowVisible(boolean shadowVisible) {
            this.shadowVisible = shadowVisible;
        }

        public boolean isShadowVisible() {
            return shadowVisible;
        }

        @ColorInt
        public int getColor() {
            return color;
        }

        /**
         * 线颜色
         */
        public void setColor(@ColorInt int color) {
            this.color = color;
        }

        @IntRange(from = 1, to = 255)
        public int getAlpha() {
            return alpha;
        }

        /**
         * 线透明度
         */
        public void setAlpha(@IntRange(from = 1, to = 255) int alpha) {
            this.alpha = alpha;
        }

        @FloatRange(from = 0, to = 1)
        public float getRatio() {
            return ratio;
        }

        /**
         * 线比例，范围为0-1,0表示最长，1表示最短
         */
        public void setRatio(@FloatRange(from = 0, to = 1) float ratio) {
            this.ratio = ratio;
        }

        public float getThick() {
            return thick;
        }

        /**
         * 线粗
         */
        public void setThick(float thick) {
            this.thick = thick;
        }

        @Override
        public String toString() {
            return "visible=" + visible + ",color=" + color + ",alpha=" + alpha + ",thick=" + thick;
        }

    }

    /**
     * @deprecated 使用{@link #DividerConfig}代替
     */
    @Deprecated
    public static class LineConfig extends DividerConfig {
    }

    /**
     * 用于兼容旧版本的纯字符串条目
     */
    private static class StringItem implements WheelItem {
        private String name;

        private StringItem(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

    }

    public interface OnItemSelectListener {
        /**
         * 滑动选择回调
         *
         * @param isUserScroll 是否用户手动滚动，用于联动效果判断是否自动重置选中项
         * @param index        当前选择项的索引
         * @param item         当前选择项的值
         */
        void onSelected(boolean isUserScroll, int index, WheelItem item);

    }

    /**
     * 兼容旧版本API
     *
     * @deprecated use {@link OnItemSelectListener} instead
     */
    @Deprecated
    public interface OnWheelListener {

        void onSelected(boolean isUserScroll, int index, String item);

    }

    /**
     * @deprecated use {@link OnItemSelectListener} instead
     */
    @Deprecated
    public interface OnWheelViewListener extends OnWheelListener {
    }

    private static class MessageHandler extends Handler {
        private static final int WHAT_INVALIDATE_LOOP_VIEW = 1000;
        private static final int WHAT_SMOOTH_SCROLL = 2000;
        private static final int WHAT_ITEM_SELECTED = 3000;
        private WheelView wheelView;

        MessageHandler(WheelView wheelView) {
            this.wheelView = wheelView;
        }

        @Override
        public final void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_INVALIDATE_LOOP_VIEW:
                    wheelView.invalidate();
                    break;
                case WHAT_SMOOTH_SCROLL:
                    wheelView.smoothScroll(FLING);
                    break;
                case WHAT_ITEM_SELECTED:
                    wheelView.onSelectedCallback();
                    break;
            }
        }

    }

}
