package cn.qqtheme.framework.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import cn.qqtheme.framework.util.ConvertUtils;
import cn.qqtheme.framework.util.LogUtils;

/**
 * 基于原版(https://github.com/wangjiegulu/WheelView)作了一下修改：
 * 去掉回弹阴影
 * 修正以便支持联动效果
 * 可设置颜色
 * 设置文字大小
 * 分隔线是否可见
 * 初始设置选中选项
 * 伪循环滚动
 *
 * @author 李玉江[QQ:1023694760]
 * @since 2015/12/17
 */
public class WheelView extends ScrollView {
    public static final int TEXT_SIZE = 20;
    public static final int TEXT_PADDING_TOP_BOTTOM = 8;
    public static final int TEXT_PADDING_LEFT_RIGHT = 15;
    public static final int TEXT_COLOR_FOCUS = 0XFF0288CE;
    public static final int TEXT_COLOR_NORMAL = 0XFFBBBBBB;
    public static final int LINE_ALPHA = 150;
    public static final int LINE_COLOR = 0XFF83CDE6;
    public static final float LINE_THICK = 1f;
    public static final int OFF_SET = 2;
    private static final int DELAY = 50;//自动滚动延迟毫秒数
    private static final int DELTA = 100;//触发伪循环的慢滚或速滑距离
    private static final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
    private static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;

    private Context context;
    private LinearLayout views;//容器
    private LinkedList<String> items = new LinkedList<String>();
    private int offset = OFF_SET; // 偏移量（需要在最前面和最后面补全）

    private int displayItemCount; // 每页显示的数量

    private int selectedIndex = OFF_SET;//索引值含补全的占位符的索引
    private int initialY;

    private Runnable scrollerTask = new ScrollerTask();
    private int itemHeight = 0;
    private OnWheelListener onWheelListener;

    private int textSize = TEXT_SIZE;
    private int textColorNormal = TEXT_COLOR_NORMAL;
    private int textColorFocus = TEXT_COLOR_FOCUS;
    private boolean isUserScroll = false;//是否用户手动滚动
    private boolean cycleDisable = false;//是否禁用伪循环

    private LineConfig lineConfig;

    public WheelView(Context context) {
        super(context);
        init(context);
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WheelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.context = context;

        // 2015/12/15 去掉ScrollView的阴影
        setFadingEdgeLength(0);
        setOverScrollMode(OVER_SCROLL_NEVER);
        setVerticalScrollBarEnabled(false);

        views = new LinearLayout(context);
        views.setOrientation(LinearLayout.VERTICAL);
        addView(views);
    }

    private void startScrollerTask() {
        initialY = getScrollY();
        postDelayed(scrollerTask, DELAY);
    }

    private void initData() {
        long startTime = System.currentTimeMillis();

        displayItemCount = offset * 2 + 1;

        // 2015/12/15 添加此句才可以支持联动效果
        views.removeAllViews();

        //对象复用，可参考这个基于ListView的实现方式：https://github.com/venshine/WheelView
        for (String item : items) {
            views.addView(createView(item));
        }

        // 2016/1/15 焦点文字颜色高亮位置，逆推“int position = y / itemHeight + offset”
        refreshItemView(itemHeight * (selectedIndex - offset));

        long millis = System.currentTimeMillis() - startTime;
        LogUtils.verbose(this, "init data spent " + millis + "ms");
    }

    private TextView createView(String item) {
        TextView tv = new TextView(context);
        tv.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        tv.setSingleLine(true);
        tv.setEllipsize(TextUtils.TruncateAt.END);
        tv.setText(item);
        tv.setTextSize(textSize);
        tv.setGravity(Gravity.CENTER);
        int paddingTopBottom = ConvertUtils.toPx(context, TEXT_PADDING_TOP_BOTTOM);
        int paddingLeftRight = ConvertUtils.toPx(context, TEXT_PADDING_LEFT_RIGHT);
        tv.setPadding(paddingLeftRight, paddingTopBottom, paddingLeftRight, paddingTopBottom);
        if (0 == itemHeight) {
            int wSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            int hSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
            tv.measure(wSpec, hSpec);
            itemHeight = tv.getMeasuredHeight();
            LogUtils.verbose(this, "itemHeight: " + itemHeight);
            views.setLayoutParams(new LayoutParams(MATCH_PARENT, itemHeight * displayItemCount));
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) getLayoutParams();
            setLayoutParams(new LinearLayout.LayoutParams(lp.width, itemHeight * displayItemCount));
        }
        return tv;
    }


    private void refreshItemView(int y) {
        int position = y / itemHeight + offset;
        int remainder = y % itemHeight;
        int divided = y / itemHeight;

        if (remainder == 0) {
            position = divided + offset;
        } else {
            if (remainder > itemHeight / 2) {
                position = divided + offset + 1;
            }
        }
        LogUtils.verbose("current scroll position : " + position);

        int childSize = views.getChildCount();
        for (int i = 0; i < childSize; i++) {
            TextView itemView = (TextView) views.getChildAt(i);
            if (null == itemView) {
                return;
            }
            // 2015/12/15 可设置颜色
            if (position == i) {
                itemView.setTextColor(textColorFocus);
            } else {
                itemView.setTextColor(textColorNormal);
            }
        }
    }

    /**
     * 选中回调
     */
    private void onSelectedCallback() {
        if (null != onWheelListener) {
            // 2015/12/25 真实的index应该忽略偏移量
            int realIndex = selectedIndex - offset;
            LogUtils.verbose("isUserScroll=" + isUserScroll + ",selectedIndex=" + selectedIndex + ",realIndex=" + realIndex);
            onWheelListener.onSelected(isUserScroll, realIndex, items.get(this.selectedIndex));
        }
    }

    @Deprecated
    @Override
    public final void setBackgroundColor(int color) {
        throwUnsupportedException();
    }

    @Deprecated
    @Override
    public final void setBackgroundResource(int resid) {
        throwUnsupportedException();
    }

    @Deprecated
    @Override
    public final void setBackground(Drawable background) {
        throwUnsupportedException();
    }

    @Deprecated
    @SuppressWarnings("deprecation")
    @Override
    public final void setBackgroundDrawable(Drawable background) {
        throwUnsupportedException();
    }

    private void throwUnsupportedException() {
        throw new UnsupportedOperationException("don't set background");
    }

    private void changeBackgroundLineDrawable(boolean isSizeChanged) {
        LogUtils.verbose(this, "isSizeChanged=" + isSizeChanged + ", config is " + lineConfig);
        if (!lineConfig.isVisible()) {
            return;
        }
        //noinspection deprecation
        super.setBackgroundDrawable(new LineDrawable(lineConfig));
    }

    public void setLineConfig(@Nullable LineConfig config) {
        if (null == config) {
            LogUtils.verbose(this, "line config is null, will hide line");
            if (null != lineConfig) {
                lineConfig.setVisible(false);
            }
            return;
        }
        if (null == lineConfig) {
            config.setWidth(getResources().getDisplayMetrics().widthPixels);
            int[] area = new int[2];
            area[0] = itemHeight * offset;
            area[1] = itemHeight * (offset + 1);
            config.setArea(area);
        }
        lineConfig = config;
        changeBackgroundLineDrawable(false);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        LogUtils.verbose(this, "horizontal scroll origin: " + l + ", vertical scroll origin: " + t);
        refreshItemView(t);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        LogUtils.verbose(this, "onSizeChanged viewWidth=" + w);
        if (null == lineConfig) {
            lineConfig = new LineConfig();
            lineConfig.setColor(LINE_COLOR);
            lineConfig.setWidth(w);
            lineConfig.setThick(ConvertUtils.toPx(context, LINE_THICK));
        }
        lineConfig.setWidth(w);
        int[] area = new int[2];
        area[0] = itemHeight * offset;
        area[1] = itemHeight * (offset + 1);
        lineConfig.setArea(area);
        changeBackgroundLineDrawable(true);
    }

    @Override
    public void fling(int velocityY) {
        super.fling(velocityY / 3);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        isUserScroll = true;//触发触摸事件，说明是用户在滚动
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LogUtils.verbose(this, "press down, cycleDisable=" + cycleDisable);
                break;
            case MotionEvent.ACTION_UP:
                LogUtils.verbose(this, "press up, items=" + items.size() + ", offset=" + offset);
                startScrollerTask();
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void _setItems(List<String> list) {
        items.clear();
        items.addAll(list);
        // 前面和后面补全
        for (int i = 0; i < offset; i++) {
            items.addFirst("");
            items.addLast("");
        }
        initData();
    }

    public void setItems(List<String> list) {
        _setItems(list);
        // 2015/12/25 初始化时设置默认选中项
        setSelectedIndex(0);
    }

    public void setItems(String[] list) {
        setItems(Arrays.asList(list));
    }

    public void setItems(List<String> list, int index) {
        _setItems(list);
        setSelectedIndex(index);
    }

    public void setItems(List<String> list, String item) {
        _setItems(list);
        setSelectedItem(item);
    }

    public void setItems(String[] list, int index) {
        setItems(Arrays.asList(list), index);
    }

    public void setItems(String[] list, String item) {
        setItems(Arrays.asList(list), item);
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getTextColor() {
        return textColorFocus;
    }

    public void setTextColor(@ColorInt int textColorNormal, @ColorInt int textColorFocus) {
        this.textColorNormal = textColorNormal;
        this.textColorFocus = textColorFocus;
    }

    public void setTextColor(@ColorInt int textColor) {
        this.textColorFocus = textColor;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(@IntRange(from = 1, to = 3) int offset) {
        if (offset < 1 || offset > 3) {
            throw new IllegalArgumentException("Offset must between 1 and 3");
        }
        this.offset = offset;
    }

    public void setCycleDisable(boolean cycleDisable) {
        this.cycleDisable = cycleDisable;
    }

    /**
     * 从0开始计数，所有项包括偏移量
     */
    private void setSelectedIndex(@IntRange(from = 0) final int index) {
        isUserScroll = false;
        post(new Runnable() {
            @Override
            public void run() {
                //滚动到选中项的位置，smoothScrollTo滚动视觉效果有延迟
                //smoothScrollTo(0, index * itemHeight);
                scrollTo(0, index * itemHeight);
                //选中这一项的值
                selectedIndex = index + offset;
                onSelectedCallback();
                //默认选中第一项时颜色需要高亮
                refreshItemView(itemHeight * index);
            }
        });
    }

    public void setSelectedItem(String item) {
        if (TextUtils.isEmpty(item)) {
            return;
        }
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).equals(item)) {
                //调用_setItems(List)时额外添加了offset个占位符到items里，需要忽略占位符所占的位
                setSelectedIndex(i - offset);
                break;
            }
        }
    }

    public String getSelectedItem() {
        return items.get(selectedIndex);
    }

    public int getSelectedIndex() {
        return selectedIndex - offset;
    }

    /**
     * @deprecated use {@link #setOnWheelListener(OnWheelListener)} instead
     */
    @Deprecated
    public void setOnWheelViewListener(OnWheelViewListener onWheelListener) {
        setOnWheelListener(onWheelListener);
    }

    public void setOnWheelListener(OnWheelListener onWheelListener) {
        this.onWheelListener = onWheelListener;
    }

    public interface OnWheelListener {
        /**
         * 滑动选择回调
         *
         * @param isUserScroll 是否用户手动滚动，用于联动效果判断是否自动重置选中项
         * @param index        当前选择项的索引
         * @param item         当前选择项的值
         */
        void onSelected(boolean isUserScroll, int index, String item);
    }

    /**
     * @deprecated use {@link OnWheelListener} instead
     */
    @Deprecated
    public interface OnWheelViewListener extends OnWheelListener {
    }

    private class ScrollerTask implements Runnable {

        @Override
        public void run() {
            // 2015/12/17 java.lang.ArithmeticException: divide by zero
            if (itemHeight == 0) {
                LogUtils.debug(this, "itemHeight is zero");
                return;
            }
            int newY = getScrollY();
            if (initialY - newY != 0) {
                startScrollerTask();
                return;
            }
            // stopped
            final int remainder = initialY % itemHeight;
            final int divided = initialY / itemHeight;
            LogUtils.verbose(this, "initialY: " + initialY + ", remainder: " + remainder + ", divided: " + divided);
            if (remainder == 0) {
                selectedIndex = divided + offset;
                onSelectedCallback();
                return;
            }
            if (remainder > itemHeight / 2) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        smoothScrollTo(0, initialY - remainder + itemHeight);
                        selectedIndex = divided + offset + 1;
                        onSelectedCallback();
                    }
                });
            } else {
                post(new Runnable() {
                    @Override
                    public void run() {
                        smoothScrollTo(0, initialY - remainder);
                        selectedIndex = divided + offset;
                        onSelectedCallback();
                    }
                });
            }
        }

    }

    /**
     * 选中项的分割线
     */
    public static class LineConfig {
        private boolean visible = true;
        private int color = LINE_COLOR;
        private int alpha = LINE_ALPHA;
        private float ratio = (float) (1.0 / 6.0);
        private float thick = -1;// dp
        private int width = 0;
        private int[] area = null;

        public LineConfig() {
            super();
        }

        public LineConfig(@FloatRange(from = 0, to = 1) float ratio) {
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
            if (thick == -1) {
                thick = ConvertUtils.toPx(LINE_THICK);
            }
            return thick;
        }

        /**
         * 线粗
         */
        public void setThick(float thick) {
            this.thick = thick;
        }

        protected int getWidth() {
            return width;
        }

        protected void setWidth(int width) {
            this.width = width;
        }

        @Size(2)
        protected int[] getArea() {
            return area;
        }

        protected void setArea(@Size(2) int[] area) {
            this.area = area;
        }

        @Override
        public String toString() {
            return "visible=" + visible + "color=" + color + ", alpha=" + alpha
                    + ", thick=" + thick + ", width=" + width;
        }

    }

    private static class LineDrawable extends Drawable {
        private Paint paint;
        private LineConfig config;

        LineDrawable(LineConfig cfg) {
            this.config = cfg;
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(config.getColor());
            paint.setAlpha(config.getAlpha());
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(config.getThick());
        }

        @Override
        public void draw(@NonNull Canvas canvas) {
            int[] area = config.getArea();
            int width = config.getWidth();
            float ratio = config.getRatio();
            canvas.drawLine(width * ratio, area[0], width * (1 - ratio), area[0], paint);
            canvas.drawLine(width * ratio, area[1], width * (1 - ratio), area[1], paint);
        }

        @Override
        public void setAlpha(int alpha) {

        }

        @Override
        public void setColorFilter(ColorFilter cf) {

        }

        @Override
        public int getOpacity() {
            return PixelFormat.UNKNOWN;
        }

    }

}
