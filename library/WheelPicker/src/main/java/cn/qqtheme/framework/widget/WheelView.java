package cn.qqtheme.framework.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.qqtheme.framework.util.ConvertUtils;
import cn.qqtheme.framework.util.LogUtils;

/**
 * 滑轮控件，参见：https://github.com/venshine/WheelView
 * <br />
 * Author:李玉江[QQ:1032694760]
 * DateTime:2017/01/06 18:05
 * Builder:Android Studio
 *
 * @see WheelAdapter
 * @see ItemView
 * @see LineConfig
 * @see OnWheelListener
 * @see WheelDrawable
 * @see HoloWheelDrawable
 * @see ShadowWheelDrawable
 */
public class WheelView extends ListView implements ListView.OnScrollListener, View.OnTouchListener,
        ViewTreeObserver.OnGlobalLayoutListener {
    public static final int SMOOTH_SCROLL_DURATION = 50;//ms

    public static final int TEXT_SIZE = 16;//sp
    public static final float TEXT_ALPHA = 0.8f;
    public static final int TEXT_COLOR_FOCUS = 0XFF0288CE;
    public static final int TEXT_COLOR_NORMAL = 0XFFBBBBBB;

    public static final int ITEM_OFF_SET = 2;
    public static final int ITEM_HEIGHT = 40;//dp
    public static final int ITEM_PADDING_TOP_BOTTOM = 5;//dp
    public static final int ITEM_PADDING_LEFT_RIGHT = 10;//dp
    public static final int ITEM_MARGIN = 5;//dp
    public static final int ITEM_TAG_IMAGE = 100;
    public static final int ITEM_TAG_TEXT = 101;

    public static final int LINE_ALPHA = 220;
    public static final int LINE_COLOR = 0XFF83CDE6;
    public static final float LINE_THICK = 1f;//px

    private static final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
    private static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;

    private int itemHeightPixels = 0; // 每一项高度
    private int currentPosition = -1;    // 记录滚轮当前刻度
    private WheelAdapter adapter = new WheelAdapter();
    private OnWheelListener onWheelListener;

    private int textSize = TEXT_SIZE;
    private int textColorNormal = TEXT_COLOR_NORMAL;
    private int textColorFocus = TEXT_COLOR_FOCUS;
    private boolean isUserScroll = false;//是否用户手动滚动
    private LineConfig lineConfig = null;//分割线配置

    public WheelView(Context context) {
        super(context);
        init();
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setVerticalScrollBarEnabled(false);
        setScrollingCacheEnabled(false);
        setCacheColorHint(Color.TRANSPARENT);
        setFadingEdgeLength(0);
        setOverScrollMode(OVER_SCROLL_NEVER);
        setLayoutParams(new ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        setDividerHeight(0);
        setOnScrollListener(this);
        setOnTouchListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setNestedScrollingEnabled(true);
        }
        if (!isInEditMode()) {
            getViewTreeObserver().addOnGlobalLayoutListener(this);
        }
        super.setAdapter(adapter);
    }

    /**
     * 设置背景
     */
    private void changeBackground() {
        int wheelSize = adapter.getWheelSize();
        if (null == lineConfig) {
            lineConfig = new LineConfig();
        }
        lineConfig.setWidth(getWidth());
        lineConfig.setHeight(itemHeightPixels * wheelSize);
        lineConfig.setWheelSize(wheelSize);
        lineConfig.setItemHeight(itemHeightPixels);
        Drawable drawable;
        WheelDrawable holoWheelDrawable = new HoloWheelDrawable(lineConfig);
        if (lineConfig.isShadowVisible()) {
            WheelDrawable shadowWheelDrawable = new ShadowWheelDrawable(lineConfig);
            if (lineConfig.isVisible()) {
                drawable = new LayerDrawable(new Drawable[]{shadowWheelDrawable, holoWheelDrawable});
            } else {
                drawable = shadowWheelDrawable;
            }
        } else if (lineConfig.isVisible()) {
            drawable = holoWheelDrawable;
        } else {
            drawable = new WheelDrawable(lineConfig);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            super.setBackground(drawable);
        } else {
            //noinspection deprecation
            super.setBackgroundDrawable(drawable);
        }
    }

    private void _setItems(List<String> list) {
        if (null == list || list.size() == 0) {
            throw new IllegalArgumentException("data are empty");
        }
        isUserScroll = false;
        currentPosition = -1;
        adapter.setData(list);
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

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public void setTextColor(@ColorInt int textColorNormal, @ColorInt int textColorFocus) {
        this.textColorNormal = textColorNormal;
        this.textColorFocus = textColorFocus;
    }

    public void setTextColor(@ColorInt int textColor) {
        this.textColorFocus = textColor;
    }

    /**
     * 设置滚轮个数偏移量
     */
    public void setOffset(@IntRange(from = 1, to = 3) int offset) {
        if (offset < 1 || offset > 3) {
            throw new IllegalArgumentException("Offset must between 1 and 3");
        }
        int wheelSize = offset * 2 + 1;
        adapter.setWheelSize(wheelSize);
    }

    /**
     * 设置滚轮是否禁用循环滚动
     */
    public void setCycleDisable(boolean cycleDisable) {
        adapter.setLoop(!cycleDisable);
    }

    public int getSelectedIndex() {
        return getCurrentPosition();
    }

    public void setSelectedIndex(int index) {
        setSelection(index);
    }

    /**
     * 获取当前滚轮位置的数据
     */
    @Override
    public String getSelectedItem() {
        return adapter.getItem(getCurrentPosition());
    }

    public void setSelectedItem(String item) {
        //noinspection deprecation
        setSelection(adapter.getData().indexOf(item));
    }

    /**
     * @deprecated use {@link #setSelectedIndex(int)} instead
     */
    @Override
    @Deprecated
    public void setSelection(int position) {
        final int realPosition = getRealPosition(position);
        //延时一下以保证数据初始化完成，才定位到选中项
        postDelayed(new Runnable() {
            @Override
            public void run() {
                WheelView.super.setSelection(realPosition);
                refreshCurrentPosition();
            }
        }, 500);
    }

    /**
     * 获得滚轮当前真实位置
     */
    private int getRealPosition(int position) {
        int realCount = adapter.getRealCount();
        if (realCount == 0) {
            return 0;
        }
        if (adapter.isLoop()) {
            int d = Integer.MAX_VALUE / 2 / realCount;
            return position + d * realCount - adapter.getWheelSize() / 2;
        }
        return position;
    }

    /**
     * 获取当前滚轮位置
     */
    public int getCurrentPosition() {
        if (currentPosition == -1) {
            return 0;
        }
        return currentPosition;
    }

    /**
     * 设置滚轮数据适配器
     *
     * @deprecated 使用{@link #setItems}代替
     */
    @Deprecated
    @Override
    public void setAdapter(ListAdapter adapter) {
        if (adapter != null && adapter instanceof WheelAdapter) {
            this.adapter = (WheelAdapter) adapter;
            super.setAdapter(this.adapter);
        } else {
            throw new IllegalArgumentException("please invoke setItems");
        }
    }

    private void onSelectedCallback() {
        int index = getSelectedIndex();
        String item = getSelectedItem();
        LogUtils.verbose("isUserScroll=" + isUserScroll + ", index=" + index + ", item=" + item);
        if (null != onWheelListener) {
            onWheelListener.onSelected(isUserScroll, index, item);
        }
    }

    private int obtainSmoothDistance(float scrollDistance) {
        if (Math.abs(scrollDistance) <= 2) {
            return (int) scrollDistance;
        } else if (Math.abs(scrollDistance) < 12) {
            return scrollDistance > 0 ? 2 : -2;
        } else {
            return (int) (scrollDistance / 6);  // 减缓平滑滑动速率
        }
    }

    private void refreshCurrentPosition() {
        if (getChildAt(0) == null || itemHeightPixels == 0) {
            return;
        }
        int firstPosition = getFirstVisiblePosition();
        if (adapter.isLoop() && firstPosition == 0) {
            LogUtils.verbose("is loop and first visible position is 0");
            return;
        }
        int position;
        if (Math.abs(getChildAt(0).getY()) <= itemHeightPixels / 2) {
            position = firstPosition;
        } else {
            position = firstPosition + 1;
        }
        //由这个逆推：int wheelSize = offset * 2 + 1;
        int offset = (adapter.getWheelSize() - 1) / 2;
        int curPosition = position + offset;
        refreshVisibleItems(firstPosition, curPosition, offset);
        if (adapter.isLoop()) {
            position = curPosition % adapter.getRealCount();
        }
        if (position == currentPosition) {
            //LogUtils.verbose("scrolling position: " + position);
            return;
        }
        currentPosition = position;
        //LogUtils.verbose("refresh position as: " + position);
        onSelectedCallback();
    }

    private void refreshVisibleItems(int firstPosition, int curPosition, int offset) {
        for (int i = curPosition - offset; i <= curPosition + offset; i++) {
            View itemView = getChildAt(i - firstPosition);
            if (itemView == null) {
                continue;
            }
            TextView textView = (TextView) itemView.findViewWithTag(ITEM_TAG_TEXT);
            refreshTextView(i, curPosition, itemView, textView);
        }
    }

    private void refreshTextView(int position, int curPosition, View
            itemView, TextView textView) {
        //LogUtils.verbose("position=" + position + ", curPosition=" + curPosition);
        if (curPosition == position) { // 选中
            setTextView(itemView, textView, textColorFocus, textSize, 1.0f);
        } else { // 未选中
            int delta = Math.abs(position - curPosition);
            float alpha = (float) Math.pow(TEXT_ALPHA, delta);
            setTextView(itemView, textView, textColorNormal, textSize, alpha);
        }
    }

    private void setTextView(View itemView, TextView textView, int textColor, float textSize, float textAlpha) {
        textView.setTextColor(textColor);
        textView.setTextSize(textSize);
        itemView.setAlpha(textAlpha);
    }

    @Override
    public void onGlobalLayout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getViewTreeObserver().removeOnGlobalLayoutListener(this);
        } else {
            getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
        int childCount = getChildCount();
        if (childCount > 0 && itemHeightPixels == 0) {
            itemHeightPixels = getChildAt(0).getHeight();
            LogUtils.verbose(this, "itemHeightPixels=" + itemHeightPixels);
            if (itemHeightPixels == 0) {
                return;
            }
            int wheelSize = adapter.getWheelSize();
            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = itemHeightPixels * wheelSize;
            refreshVisibleItems(getFirstVisiblePosition(),
                    getCurrentPosition() + wheelSize / 2, wheelSize / 2);
            changeBackground();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        isUserScroll = true;//触发触摸事件，说明是用户在滚动
        //v.getParent().requestDisallowInterceptTouchEvent(true);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LogUtils.verbose(this, "press down: currentPosition=" + currentPosition);
                break;
            case MotionEvent.ACTION_UP:
                LogUtils.verbose(this, "press up: currentItem=" + getSelectedItem());
                break;
        }
        return false;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState != SCROLL_STATE_IDLE) {
            return;
        }
        View itemView = getChildAt(0);
        if (itemView == null) {
            return;
        }
        float deltaY = itemView.getY();
        // fixed: 17-1-7  Equality tests should not be made with floating point values.
        if ((int) deltaY == 0 || itemHeightPixels == 0) {
            return;
        }
        if (Math.abs(deltaY) < itemHeightPixels / 2) {
            int d = obtainSmoothDistance(deltaY);
            smoothScrollBy(d, SMOOTH_SCROLL_DURATION);
        } else {
            int d = obtainSmoothDistance(itemHeightPixels + deltaY);
            smoothScrollBy(d, SMOOTH_SCROLL_DURATION);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int
            visibleItemCount, int totalItemCount) {
        if (visibleItemCount != 0) {
            refreshCurrentPosition();
        }
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int width = getLayoutParams().width;
        //LogUtils.verbose(this, "onMeasure: width is " + width);
        if (width == WRAP_CONTENT) {
            //宽度自适应
            super.onMeasure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), heightSpec);
        } else {
            //宽度填充屏幕或明确设置了宽度
            super.onMeasure(widthSpec, heightSpec);
        }
    }

    public void setOnWheelListener(OnWheelListener onWheelListener) {
        this.onWheelListener = onWheelListener;
    }

    public void setLineConfig(LineConfig lineConfig) {
        this.lineConfig = lineConfig;
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

    /**
     * 选中项的分割线
     */
    public static class LineConfig {
        private boolean visible = true;
        private boolean shadowVisible = false;
        private int color = LINE_COLOR;
        private int alpha = LINE_ALPHA;
        private float ratio = (float) (1.0 / 6.0);
        private float thick = LINE_THICK;// px
        private int width = 0;
        private int height = 0;
        private int itemHeight = 0;
        private int wheelSize = 0;

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

        protected int getWidth() {
            return width;
        }

        protected void setWidth(int width) {
            this.width = width;
        }

        protected int getHeight() {
            return height;
        }

        protected void setHeight(int height) {
            this.height = height;
        }

        protected int getItemHeight() {
            return itemHeight;
        }

        protected void setItemHeight(int itemHeight) {
            this.itemHeight = itemHeight;
        }

        protected int getWheelSize() {
            return wheelSize;
        }

        protected void setWheelSize(int wheelSize) {
            this.wheelSize = wheelSize;
        }

        @Override
        public String toString() {
            return "visible=" + visible + "color=" + color + ", alpha=" + alpha
                    + ", thick=" + thick + ", width=" + width;
        }

    }

    private static class ItemView extends LinearLayout {
        private ImageView imageView;
        private TextView textView;

        public ItemView(Context context) {
            super(context);
            init(context);
        }

        public ItemView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init(context);
        }

        public ItemView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init(context);
        }

        private void init(Context context) {
            setOrientation(LinearLayout.HORIZONTAL);
            int paddingTopBottom = ConvertUtils.toPx(context, ITEM_PADDING_TOP_BOTTOM);
            int paddingLeftRight = ConvertUtils.toPx(context, ITEM_PADDING_LEFT_RIGHT);
            setPadding(paddingLeftRight, paddingTopBottom, paddingLeftRight, paddingTopBottom);
            setGravity(Gravity.CENTER);
            int height = ConvertUtils.toPx(context, ITEM_HEIGHT);
            // fixed: 17-1-8 #79 安卓4.x兼容问题，java.lang.ClassCastException……onMeasure……
            setLayoutParams(new AbsListView.LayoutParams(MATCH_PARENT, height));

            imageView = new ImageView(getContext());
            imageView.setTag(ITEM_TAG_IMAGE);
            imageView.setVisibility(View.GONE);
            LayoutParams imageParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            imageParams.rightMargin = ConvertUtils.toPx(context, ITEM_MARGIN);
            addView(imageView, imageParams);

            textView = new TextView(getContext());
            textView.setTag(ITEM_TAG_TEXT);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setSingleLine(true);
            textView.setIncludeFontPadding(false);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.BLACK);
            LayoutParams textParams = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
            addView(textView, textParams);
        }

        /**
         * 设置文本
         */
        public void setText(CharSequence text) {
            textView.setText(text);
        }

        /**
         * 设置图片资源
         */
        public void setImage(@DrawableRes int resId) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(resId);
        }

    }

    private static class WheelAdapter extends BaseAdapter {
        private List<String> data = new ArrayList<>();
        private boolean isLoop = false;
        private int wheelSize = 5;

        public final int getRealCount() {
            return data.size();
        }

        public final int getCount() {
            if (isLoop) {
                return Integer.MAX_VALUE;
            }
            return data.size() > 0 ? (data.size() + wheelSize - 1) : 0;
        }

        @Override
        public final long getItemId(int position) {
            if (isLoop) {
                return data.size() > 0 ? position % data.size() : position;
            }
            return position;
        }

        @Override
        public final String getItem(int position) {
            if (isLoop) {
                return data.size() > 0 ? data.get(position % data.size()) : null;
            }
            if (data.size() <= position) {
                position = data.size() - 1;
            }
            return data.get(position);
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEnabled(int position) {
            return false;
        }

        @Override
        public final View getView(int position, View convertView, ViewGroup parent) {
            if (isLoop) {
                position = position % data.size();
            } else {
                if (position < wheelSize / 2) {
                    position = -1;
                } else if (position >= wheelSize / 2 + data.size()) {
                    position = -1;
                } else {
                    position = position - wheelSize / 2;
                }
            }
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                holder.itemView = new WheelView.ItemView(parent.getContext());
                convertView = holder.itemView;
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (!isLoop) {
                holder.itemView.setVisibility(position == -1 ? View.INVISIBLE : View.VISIBLE);
            }
            if (position == -1) {
                position = 0;
            }
            holder.itemView.setText(data.get(position));
            //holder.wheelItem.setImage(...);
            return convertView;
        }

        public final WheelAdapter setLoop(boolean loop) {
            if (loop != isLoop) {
                isLoop = loop;
                super.notifyDataSetChanged();
            }
            return this;
        }

        public final WheelAdapter setWheelSize(int wheelSize) {
            if ((wheelSize & 1) == 0) {
                throw new IllegalArgumentException("wheel size must be an odd number.");
            }
            this.wheelSize = wheelSize;
            super.notifyDataSetChanged();
            return this;
        }

        public final WheelAdapter setData(List<String> list) {
            data.clear();
            if (null != list) {
                data.addAll(list);
            }
            super.notifyDataSetChanged();
            return this;
        }

        public List<String> getData() {
            return data;
        }

        public int getWheelSize() {
            return wheelSize;
        }

        public boolean isLoop() {
            return isLoop;
        }

        /**
         * 数据已改变，重绘可见区域
         */
        @Override
        @Deprecated
        public final void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        /**
         * 数据失效，重绘控件
         */
        @Override
        @Deprecated
        public final void notifyDataSetInvalidated() {
            super.notifyDataSetInvalidated();
        }

        private static class ViewHolder {
            WheelView.ItemView itemView;
        }
    }

    private static class WheelDrawable extends Drawable {
        protected int width;
        protected int height;
        private Paint bgPaint;

        public WheelDrawable(LineConfig config) {
            this.width = config.getWidth();
            this.height = config.getHeight();
            init();
        }

        private void init() {
            bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            bgPaint.setColor(Color.TRANSPARENT);
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.drawRect(0, 0, width, height, bgPaint);
        }

        @Override
        public void setAlpha(int alpha) {
        }

        @Override
        public void setColorFilter(ColorFilter colorFilter) {
        }

        @Override
        public int getOpacity() {
            return PixelFormat.UNKNOWN;
        }

    }

    private static class HoloWheelDrawable extends WheelDrawable {
        private Paint bgPaint, paint;
        private int wheelSize, itemHeight;
        private float ratio;

        public HoloWheelDrawable(LineConfig config) {
            super(config);
            this.wheelSize = config.getWheelSize();
            this.itemHeight = config.getItemHeight();
            ratio = config.getRatio();
            init(config);
        }

        private void init(LineConfig config) {
            bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            bgPaint.setColor(Color.TRANSPARENT);

            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeWidth(config.getThick());
            paint.setColor(config.getColor());
            paint.setAlpha(config.getAlpha());
        }

        @Override
        public void draw(Canvas canvas) {
            // draw background
            canvas.drawRect(0, 0, width, height, bgPaint);

            // draw select border
            if (itemHeight != 0) {
                canvas.drawLine(width * ratio, itemHeight * (wheelSize / 2), width * (1 - ratio),
                        itemHeight * (wheelSize / 2), paint);
                canvas.drawLine(width * ratio, itemHeight * (wheelSize / 2 + 1), width * (1 - ratio),
                        itemHeight * (wheelSize / 2 + 1), paint);
            }
        }

    }

    private static class ShadowWheelDrawable extends WheelDrawable {
        private static final int[] SHADOWS_COLORS =
                {
                        0xFF999999,
                        0x00AAAAAA,
                        0x00AAAAAA
                }; // 阴影色值
        private GradientDrawable topShadow = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                SHADOWS_COLORS); // 顶部阴影
        private GradientDrawable bottomShadow = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                SHADOWS_COLORS); // 底部阴影
        private Paint bgPaint, paint, dividerPaint;
        private int wheelSize, itemHeight;

        public ShadowWheelDrawable(LineConfig config) {
            super(config);
            this.wheelSize = config.getWheelSize();
            this.itemHeight = config.getItemHeight();
            init();
        }

        private void init() {
            bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            bgPaint.setColor(Color.TRANSPARENT);

            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(0XF0CFCFCF);

            dividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            dividerPaint.setColor(0XFFB5B5B5);
            dividerPaint.setStrokeWidth(2);
        }

        @Override
        public void draw(Canvas canvas) {
            // draw background
            canvas.drawRect(0, 0, width, height, bgPaint);

            // draw select border
            if (itemHeight != 0) {
                canvas.drawRect(0, itemHeight * (wheelSize / 2), width, itemHeight
                        * (wheelSize / 2 + 1), paint);
                canvas.drawLine(0, itemHeight * (wheelSize / 2), width, itemHeight
                        * (wheelSize / 2), dividerPaint);
                canvas.drawLine(0, itemHeight * (wheelSize / 2 + 1), width, itemHeight
                        * (wheelSize / 2 + 1), dividerPaint);

                // top, bottom
                topShadow.setBounds(0, 0, width, itemHeight);
                topShadow.draw(canvas);

                bottomShadow.setBounds(0, height - itemHeight, width, height);
                bottomShadow.draw(canvas);
            }
        }

    }

}