package cn.qqtheme.framework.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import cn.qqtheme.framework.entity.WheelItem;
import cn.qqtheme.framework.util.ConvertUtils;
import cn.qqtheme.framework.util.LogUtils;

/**
 * 3D滚轮控件，参阅：http://blog.csdn.net/qq_22393017/article/details/59488906
 * <p/>
 * Author:李玉江[QQ:1032694760]
 * DateTime:2015/12/15 09:45 基于ScrollView，参见https://github.com/wangjiegulu/WheelView
 * DateTime:2017/01/07 21:37 基于ListView，参见https://github.com/venshine/WheelView
 * DateTime:2017/04/28 21:10 基于View，参见https://github.com/weidongjian/androidWheelView
 * Builder:Android Studio
 *
 * @see WheelItem
 * @see DividerConfig
 * @see OnItemSelectListener
 */
public class WheelView extends View {
    public static final float LINE_SPACE_MULTIPLIER = 2.0F;
    public static final int TEXT_PADDING = -1;
    public static final int TEXT_SIZE = 16;//单位为sp
    public static final int TEXT_COLOR_FOCUS = 0XFF0288CE;
    public static final int TEXT_COLOR_NORMAL = 0XFFBBBBBB;
    public static final int DIVIDER_COLOR = 0XFF83CDE6;
    public static final int DIVIDER_ALPHA = 220;
    public static final float DIVIDER_THICK = 2f;//单位为px
    public static final int ITEM_OFF_SET = 3;
    private static final float ITEM_PADDING = 13f;//单位为px,480X800的手机边距不能太大
    private static final int ACTION_CLICK = 1;//点击
    private static final int ACTION_FLING = 2;//滑翔
    private static final int ACTION_DRAG = 3;//拖拽
    private static final int VELOCITY_FLING = 5;//修改这个值可以改变滑行速度
    private static final float SCALE_CONTENT = 0.8F;//非中间文字用此控制高度，压扁形成3D错觉

    private MessageHandler handler;
    private GestureDetector gestureDetector;
    private OnItemSelectListener onItemSelectListener;
    private OnWheelListener onWheelListener;
    private boolean onlyShowCenterLabel = true;//附加单位是否仅仅只显示在选中项后面
    private ScheduledFuture<?> mFuture;
    private Paint paintOuterText;//未选项画笔
    private Paint paintCenterText;//选中项画笔
    private Paint paintIndicator;//分割线画笔
    private Paint paintShadow;//阴影画笔
    private List<WheelItem> items = new ArrayList<>();//所有选项
    private String label;//附加单位
    private int maxTextWidth;//最大的文字宽
    private int maxTextHeight;//最大的文字高
    private int textSkewXOffset = 0;//文字倾斜度
    private int textSize = TEXT_SIZE;//文字大小，单位为sp
    private float itemHeight;//每行高度
    private Typeface typeface = Typeface.DEFAULT;//字体样式
    private int textColorOuter = TEXT_COLOR_NORMAL;//未选项文字颜色
    private int textColorCenter = TEXT_COLOR_FOCUS;//选中项文字颜色
    private DividerConfig dividerConfig = new DividerConfig();
    private float lineSpaceMultiplier = LINE_SPACE_MULTIPLIER;//条目间距倍数，可用来设置上下间距
    private int textPadding = TEXT_PADDING;//文字的左右边距,单位为px
    private boolean isLoop = true;//循环滚动
    private float firstLineY;//第一条线Y坐标值
    private float secondLineY;//第二条线Y坐标
    private float totalScrollY = 0;//滚动总高度y值
    private int initPosition = -1;//初始化默认选中项
    private int selectedIndex;//选中项的索引
    private int preCurrentIndex;
    private int visibleItemCount = ITEM_OFF_SET * 2 + 1;//绘制几个条目
    private int measuredHeight;//控件高度
    private int measuredWidth;//控件宽度
    private int radius;//半径
    private int offset = 0;
    private float previousY = 0;
    private long startTime = 0;
    private int widthMeasureSpec;
    private int gravity = Gravity.CENTER;
    private int drawCenterContentStart = 0;//中间选中文字开始绘制位置
    private int drawOutContentStart = 0;//非中间文字开始绘制位置
    private float centerContentOffset;//偏移量
    private boolean useWeight = false;//使用比重还是包裹内容？
    private boolean textSizeAutoFit = true;//条目内容过长时是否自动减少字号来适配

    public WheelView(Context context) {
        this(context, null);
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //屏幕密度：0.75、1.0、1.5、2.0、3.0，根据密度不同进行适配
        float density = getResources().getDisplayMetrics().density;
        if (density < 1) {
            centerContentOffset = 2.4F;
        } else if (1 <= density && density < 2) {
            centerContentOffset = 3.6F;
        } else if (1 <= density && density < 2) {
            centerContentOffset = 4.5F;
        } else if (2 <= density && density < 3) {
            centerContentOffset = 6.0F;
        } else if (density >= 3) {
            centerContentOffset = density * 2.5F;
        }
        judgeLineSpace();
        initView(context);
    }

    /**
     * 设置显示的选项个数，必须是奇数
     */
    public final void setVisibleItemCount(int count) {
        if (count % 2 == 0) {
            throw new IllegalArgumentException("must be odd");
        }
        if (count != visibleItemCount) {
            visibleItemCount = count;
        }
    }

    /**
     * 设置是否禁用循环滚动
     */
    public final void setCycleDisable(boolean cycleDisable) {
        isLoop = !cycleDisable;
    }

    /**
     * 设置滚轮个数偏移量
     */
    public final void setOffset(@IntRange(from = 1, to = 5) int offset) {
        if (offset < 1 || offset > 5) {
            throw new IllegalArgumentException("must between 1 and 5");
        }
        int count = offset * 2 + 1;
        if (offset % 2 == 0) {
            count += offset;
        } else {
            count += offset - 1;
        }
        setVisibleItemCount(count);
    }

    public final int getSelectedIndex() {
        return selectedIndex;
    }

    public final void setSelectedIndex(int index) {
        if (items == null || items.isEmpty()) {
            return;
        }
        int size = items.size();
        if (index == 0 || (index > 0 && index < size && index != selectedIndex)) {
            initPosition = index;
            totalScrollY = 0;//回归顶部，不然重设索引的话位置会偏移，就会显示出不对位置的数据
            offset = 0;
            invalidate();
        }
    }

    public final void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }

    /**
     * @deprecated use {@link #setOnItemSelectListener(OnItemSelectListener)} instead
     */
    @Deprecated
    public final void setOnWheelListener(OnWheelListener listener) {
        onWheelListener = listener;
    }


    public final void setItems(List<?> items) {
        this.items.clear();
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

    /**
     * 附加在右边的单位字符串
     */
    public final void setLabel(String label, boolean onlyShowCenterLabel) {
        this.label = label;
        this.onlyShowCenterLabel = onlyShowCenterLabel;
    }

    public final void setLabel(String label) {
        setLabel(label, true);
    }

    public final void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public void setTextColor(@ColorInt int colorNormal, @ColorInt int colorFocus) {
        this.textColorOuter = colorNormal;
        this.textColorCenter = colorFocus;
        paintOuterText.setColor(colorNormal);
        paintCenterText.setColor(colorFocus);
    }

    public void setTextColor(@ColorInt int color) {
        this.textColorOuter = color;
        this.textColorCenter = color;
        paintOuterText.setColor(color);
        paintCenterText.setColor(color);
    }

    public final void setTypeface(Typeface font) {
        typeface = font;
        paintOuterText.setTypeface(typeface);
        paintCenterText.setTypeface(typeface);
    }

    public final void setTextSize(float size) {
        if (size > 0.0F) {
            textSize = (int) (getContext().getResources().getDisplayMetrics().density * size);
            paintOuterText.setTextSize(textSize);
            paintCenterText.setTextSize(textSize);
        }
    }

    public void setTextSkewXOffset(int textSkewXOffset) {
        this.textSkewXOffset = textSkewXOffset;
        if (textSkewXOffset != 0) {
            paintCenterText.setTextScaleX(1.0F);
        }
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
        paintIndicator.setColor(config.color);
        paintIndicator.setStrokeWidth(config.thick);
        paintIndicator.setAlpha(config.alpha);
        paintShadow.setColor(config.shadowColor);
        paintShadow.setAlpha(config.shadowAlpha);
    }

    public final void setLineSpaceMultiplier(@FloatRange(from = 2, to = 4) float multiplier) {
        lineSpaceMultiplier = multiplier;
        judgeLineSpace();
    }

    /**
     * Use {@link #setTextPadding(int)} instead
     */
    @Deprecated
    public void setPadding(int padding) {
        setTextPadding(padding);
    }

    public void setTextPadding(int textPadding) {
        this.textPadding = ConvertUtils.toPx(getContext(), textPadding);
    }

    public void setUseWeight(boolean useWeight) {
        this.useWeight = useWeight;
    }

    public void setTextSizeAutoFit(boolean textSizeAutoFit) {
        this.textSizeAutoFit = textSizeAutoFit;
    }

    /**
     * 判断间距是否在有效范围内
     */
    private void judgeLineSpace() {
        if (lineSpaceMultiplier < 1.5f) {
            lineSpaceMultiplier = 1.5f;
        } else if (lineSpaceMultiplier > 4.0f) {
            lineSpaceMultiplier = 4.0f;
        }
    }

    private void initView(Context context) {
        handler = new MessageHandler(this);
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public final boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                scrollBy(velocityY);
                return true;
            }
        });
        gestureDetector.setIsLongpressEnabled(false);
        initPaints();
        initDataForIDE();
    }

    private void initPaints() {
        paintOuterText = new Paint();
        paintOuterText.setAntiAlias(true);
        paintOuterText.setColor(textColorOuter);
        paintOuterText.setTypeface(typeface);
        paintOuterText.setTextSize(textSize);
        paintCenterText = new Paint();
        paintCenterText.setAntiAlias(true);
        paintCenterText.setColor(textColorCenter);
        paintCenterText.setTextScaleX(1.0F);
        paintCenterText.setTypeface(typeface);
        paintCenterText.setTextSize(textSize);
        paintIndicator = new Paint();
        paintIndicator.setAntiAlias(true);
        paintIndicator.setColor(dividerConfig.color);
        paintIndicator.setStrokeWidth(dividerConfig.thick);
        paintIndicator.setAlpha(dividerConfig.alpha);
        paintShadow = new Paint();
        paintShadow.setAntiAlias(true);
        paintShadow.setColor(dividerConfig.shadowColor);
        paintShadow.setAlpha(dividerConfig.shadowAlpha);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    private void initDataForIDE() {
        if (isInEditMode()) {
            setItems(new String[]{"李玉江", "男", "贵州", "穿青人"});
        }
    }

    /**
     * 重新测量
     */
    private void remeasure() {
        if (items == null) {
            return;
        }
        measureTextWidthHeight();
        //半圆的周长
        int halfCircumference = (int) (itemHeight * (visibleItemCount - 1));
        //整个圆的周长除以PI得到直径，这个直径用作控件的总高度
        measuredHeight = (int) ((halfCircumference * 2) / Math.PI);
        //求出半径
        radius = (int) (halfCircumference / Math.PI);
        ViewGroup.LayoutParams params = getLayoutParams();
        //控件宽度
        if (useWeight) {
            measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        } else if (params != null && params.width > 0) {
            measuredWidth = params.width;
        } else {
            measuredWidth = maxTextWidth;
            if (textPadding < 0) {
                textPadding = ConvertUtils.toPx(getContext(), ITEM_PADDING);
            }
            measuredWidth += textPadding * 2;
            if (!TextUtils.isEmpty(label)) {
                measuredWidth += obtainTextWidth(paintCenterText, label);
            }
        }
        LogUtils.debug("measuredWidth=" + measuredWidth + ",measuredHeight=" + measuredHeight);
        //计算两条横线 和 选中项画笔的基线Y位置
        firstLineY = (measuredHeight - itemHeight) / 2.0F;
        secondLineY = (measuredHeight + itemHeight) / 2.0F;
        //初始化显示的item的position
        if (initPosition == -1) {
            if (isLoop) {
                initPosition = (items.size() + 1) / 2;
            } else {
                initPosition = 0;
            }
        }
        preCurrentIndex = initPosition;
    }

    /**
     * 计算最大length的Text的宽高度
     */
    private void measureTextWidthHeight() {
        Rect rect = new Rect();
        for (int i = 0; i < items.size(); i++) {
            String s1 = obtainContentText(items.get(i));
            paintCenterText.getTextBounds(s1, 0, s1.length(), rect);
            int textWidth = rect.width();
            if (textWidth > maxTextWidth) {
                maxTextWidth = textWidth;
            }
            paintCenterText.getTextBounds("测试", 0, 2, rect);
            maxTextHeight = rect.height() + 2;
        }
        itemHeight = lineSpaceMultiplier * maxTextHeight;
    }

    /**
     * 平滑滚动的实现
     */
    private void smoothScroll(int actionType) {
        cancelFuture();
        if (actionType == ACTION_FLING || actionType == ACTION_DRAG) {
            offset = (int) ((totalScrollY % itemHeight + itemHeight) % itemHeight);
            if ((float) offset > itemHeight / 2.0F) {//如果超过Item高度的一半，滚动到下一个Item去
                offset = (int) (itemHeight - (float) offset);
            } else {
                offset = -offset;
            }
        }
        //停止的时候，位置有偏移，不是全部都能正确停止到中间位置的，这里把文字位置挪回中间去
        SmoothScrollTimerTask command = new SmoothScrollTimerTask(this, offset);
        mFuture = Executors.newSingleThreadScheduledExecutor()
                .scheduleWithFixedDelay(command, 0, 10, TimeUnit.MILLISECONDS);
    }

    /**
     * 滚动惯性的实现
     */
    private void scrollBy(float velocityY) {
        cancelFuture();
        InertiaTimerTask command = new InertiaTimerTask(this, velocityY);
        mFuture = Executors.newSingleThreadScheduledExecutor()
                .scheduleWithFixedDelay(command, 0, VELOCITY_FLING, TimeUnit.MILLISECONDS);
    }

    private void cancelFuture() {
        if (mFuture != null && !mFuture.isCancelled()) {
            mFuture.cancel(true);
            mFuture = null;
        }
    }

    private void itemSelectedCallback() {
        if (onItemSelectListener == null && onWheelListener == null) {
            return;
        }
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (onItemSelectListener != null) {
                    onItemSelectListener.onSelected(selectedIndex);
                }
                if (onWheelListener != null) {
                    onWheelListener.onSelected(true, selectedIndex, items.get(selectedIndex).getName());
                }
            }
        }, 200L);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (items == null || items.size() == 0) {
            return;
        }
        //可见项的数组
        @SuppressLint("DrawAllocation")
        String[] visibleItemStrings = new String[visibleItemCount];
        //滚动的Y值高度除去每行的高度，得到滚动了多少个项，即change数
        int change = (int) (totalScrollY / itemHeight);
        //滚动中实际的预选中的item(即经过了中间位置的item) ＝ 滑动前的位置 ＋ 滑动相对位置
        preCurrentIndex = initPosition + change % items.size();
        if (!isLoop) {//不循环的情况
            if (preCurrentIndex < 0) {
                preCurrentIndex = 0;
            }
            if (preCurrentIndex > items.size() - 1) {
                preCurrentIndex = items.size() - 1;
            }
        } else {//循环
            if (preCurrentIndex < 0) {//举个例子：如果总数是5，preCurrentIndex ＝ －1，那么preCurrentIndex按循环来说，其实是0的上面，也就是4的位置
                preCurrentIndex = items.size() + preCurrentIndex;
            }
            if (preCurrentIndex > items.size() - 1) {//同理上面,自己脑补一下
                preCurrentIndex = preCurrentIndex - items.size();
            }
        }
        //跟滚动流畅度有关，总滑动距离与每个item高度取余，即并不是一格格的滚动，每个item不一定滚到对应Rect里的，这个item对应格子的偏移值
        float itemHeightOffset = (totalScrollY % itemHeight);
        // 设置数组中每个元素的值
        int counter = 0;
        while (counter < visibleItemCount) {
            int index = preCurrentIndex - (visibleItemCount / 2 - counter);//索引值，即当前在控件中间的item看作数据源的中间，计算出相对源数据源的index值
            //判断是否循环，如果是循环数据源也使用相对循环的position获取对应的item值，如果不是循环则超出数据源范围使用""空白字符串填充，在界面上形成空白无数据的item项
            if (isLoop) {
                index = getLoopMappingIndex(index);
                visibleItemStrings[counter] = items.get(index).getName();
            } else if (index < 0) {
                visibleItemStrings[counter] = "";
            } else if (index > items.size() - 1) {
                visibleItemStrings[counter] = "";
            } else {
                visibleItemStrings[counter] = items.get(index).getName();
            }
            counter++;
        }
        //绘制中间两条横线
        if (dividerConfig.visible) {
            float ratio = dividerConfig.ratio;
            canvas.drawLine(measuredWidth * ratio, firstLineY, measuredWidth * (1 - ratio), firstLineY, paintIndicator);
            canvas.drawLine(measuredWidth * ratio, secondLineY, measuredWidth * (1 - ratio), secondLineY, paintIndicator);
        }
        if (dividerConfig.shadowVisible) {
            paintShadow.setColor(dividerConfig.shadowColor);
            paintShadow.setAlpha(dividerConfig.shadowAlpha);
            canvas.drawRect(0.0F, firstLineY, measuredWidth, secondLineY, paintShadow);
        }
        counter = 0;
        while (counter < visibleItemCount) {
            canvas.save();
            // 弧长 L = itemHeight * counter - itemHeightOffset
            // 求弧度 α = L / r  (弧长/半径) [0,π]
            double radian = ((itemHeight * counter - itemHeightOffset)) / radius;
            // 弧度转换成角度(把半圆以Y轴为轴心向右转90度，使其处于第一象限及第四象限
            // angle [-90°,90°]
            float angle = (float) (90D - (radian / Math.PI) * 180D);//item第一项,从90度开始，逐渐递减到 -90度
            // 计算取值可能有细微偏差，保证负90°到90°以外的不绘制
            if (angle >= 90F || angle <= -90F) {
                canvas.restore();
            } else {
                //获取内容文字
                String contentText;
                //如果是label每项都显示的模式，并且item内容不为空、label也不为空
                String tempStr = obtainContentText(visibleItemStrings[counter]);
                if (!onlyShowCenterLabel && !TextUtils.isEmpty(label) && !TextUtils.isEmpty(tempStr)) {
                    contentText = tempStr + label;
                } else {
                    contentText = tempStr;
                }
                if (textSizeAutoFit) {
                    remeasureTextSize(contentText);
                    gravity = Gravity.CENTER;
                } else {
                    gravity = Gravity.START;
                }
                //计算开始绘制的位置
                measuredCenterContentStart(contentText);
                measuredOutContentStart(contentText);
                float translateY = (float) (radius - Math.cos(radian) * radius - (Math.sin(radian) * maxTextHeight) / 2D);
                canvas.translate(0.0F, translateY);
                if (translateY <= firstLineY && maxTextHeight + translateY >= firstLineY) {
                    // 条目经过第一条线
                    canvas.save();
                    canvas.clipRect(0, 0, measuredWidth, firstLineY - translateY);
                    canvas.scale(1.0F, (float) Math.sin(radian) * SCALE_CONTENT);
                    canvas.drawText(contentText, drawOutContentStart, maxTextHeight, paintOuterText);
                    canvas.restore();
                    canvas.save();
                    canvas.clipRect(0, firstLineY - translateY, measuredWidth, (int) (itemHeight));
                    canvas.scale(1.0F, (float) Math.sin(radian) * 1.0F);
                    canvas.drawText(contentText, drawCenterContentStart, maxTextHeight - centerContentOffset, paintCenterText);
                    canvas.restore();
                } else if (translateY <= secondLineY && maxTextHeight + translateY >= secondLineY) {
                    // 条目经过第二条线
                    canvas.save();
                    canvas.clipRect(0, 0, measuredWidth, secondLineY - translateY);
                    canvas.scale(1.0F, (float) Math.sin(radian) * 1.0F);
                    canvas.drawText(contentText, drawCenterContentStart, maxTextHeight - centerContentOffset, paintCenterText);
                    canvas.restore();
                    canvas.save();
                    canvas.clipRect(0, secondLineY - translateY, measuredWidth, (int) (itemHeight));
                    canvas.scale(1.0F, (float) Math.sin(radian) * SCALE_CONTENT);
                    canvas.drawText(contentText, drawOutContentStart, maxTextHeight, paintOuterText);
                    canvas.restore();
                } else if (translateY >= firstLineY && maxTextHeight + translateY <= secondLineY) {
                    // 中间条目
                    canvas.clipRect(0, 0, measuredWidth, maxTextHeight);
                    //让文字居中
                    float y = maxTextHeight - centerContentOffset;//因为圆弧角换算的向下取值，导致角度稍微有点偏差，加上画笔的基线会偏上，因此需要偏移量修正一下
                    int i = 0;
                    for (WheelItem item : items) {
                        if (item.getName().equals(tempStr)) {
                            selectedIndex = i;
                            break;
                        }
                        i++;
                    }
                    if (onlyShowCenterLabel && !TextUtils.isEmpty(label)) {
                        contentText += label;
                    }
                    canvas.drawText(contentText, drawCenterContentStart, y, paintCenterText);
                } else {
                    // 其他条目
                    canvas.save();
                    canvas.clipRect(0, 0, measuredWidth, itemHeight);
                    canvas.scale(1.0F, (float) Math.sin(radian) * SCALE_CONTENT);
                    // 根据当前角度计算出偏差系数，用以在绘制时控制文字的 水平移动 透明度 倾斜程度
                    float offsetCoefficient = (float) Math.pow(Math.abs(angle) / 90f, 2.2);
                    if (textSkewXOffset != 0) {
                        //控制文字倾斜度
                        paintOuterText.setTextSkewX((textSkewXOffset > 0 ? 1 : -1) * (angle > 0 ? -1 : 1) * 0.5F * offsetCoefficient);
                        // 控制透明度
                        paintOuterText.setAlpha((int) ((1 - offsetCoefficient) * 255));
                    }
                    // 控制文字水平偏移距离
                    canvas.drawText(contentText, drawOutContentStart + textSkewXOffset * offsetCoefficient, maxTextHeight, paintOuterText);
                    canvas.restore();
                }
                canvas.restore();
                paintCenterText.setTextSize(textSize);
            }
            counter++;
        }
    }

    /**
     * 根据文字的长度 重新设置文字的大小 让其能完全显示
     */
    private void remeasureTextSize(String contentText) {
        Rect rect = new Rect();
        paintCenterText.getTextBounds(contentText, 0, contentText.length(), rect);
        int width = rect.width();
        int size = textSize;
        while (width > measuredWidth) {
            size--;
            //设置2条横线中间的文字大小
            paintCenterText.setTextSize(size);
            paintCenterText.getTextBounds(contentText, 0, contentText.length(), rect);
            width = rect.width();
        }
        //设置2条横线外面的文字大小
        paintOuterText.setTextSize(size);
    }


    /**
     * 递归计算出对应的索引
     */
    private int getLoopMappingIndex(int index) {
        if (index < 0) {
            index = index + items.size();
            index = getLoopMappingIndex(index);
        } else if (index > items.size() - 1) {
            index = index - items.size();
            index = getLoopMappingIndex(index);
        }
        return index;
    }

    /**
     * 根据传进来的对象来获取需要显示的值
     *
     * @param item 数据源的项
     * @return 对应显示的字符串
     */
    private String obtainContentText(Object item) {
        if (item == null) {
            return "";
        } else if (item instanceof WheelItem) {
            return ((WheelItem) item).getName();
        } else if (item instanceof Integer) {
            //如果为整形则最少保留两位数.
            return String.format(Locale.getDefault(), "%02d", (int) item);
        }
        return item.toString();
    }

    private void measuredCenterContentStart(String content) {
        Rect rect = new Rect();
        paintCenterText.getTextBounds(content, 0, content.length(), rect);
        switch (gravity) {
            case Gravity.CENTER://显示内容居中
                drawCenterContentStart = (int) ((measuredWidth - rect.width()) * 0.5);
                break;
            case Gravity.LEFT:
                drawCenterContentStart = ConvertUtils.toPx(getContext(), 8);
                break;
            case Gravity.RIGHT://添加偏移量
                drawCenterContentStart = measuredWidth - rect.width() - (int) centerContentOffset;
                break;
        }
    }

    private void measuredOutContentStart(String content) {
        Rect rect = new Rect();
        paintOuterText.getTextBounds(content, 0, content.length(), rect);
        switch (gravity) {
            case Gravity.CENTER:
                drawOutContentStart = (int) ((measuredWidth - rect.width()) * 0.5);
                break;
            case Gravity.LEFT:
                drawOutContentStart = ConvertUtils.toPx(getContext(), 8);
                break;
            case Gravity.RIGHT:
                drawOutContentStart = measuredWidth - rect.width() - (int) centerContentOffset;
                break;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.widthMeasureSpec = widthMeasureSpec;
        remeasure();
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean eventConsumed = gestureDetector.onTouchEvent(event);
        ViewParent parent = getParent();
        switch (event.getAction()) {
            //按下
            case MotionEvent.ACTION_DOWN:
                startTime = System.currentTimeMillis();
                cancelFuture();
                previousY = event.getRawY();
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(true);
                }
                break;
            //滑动中
            case MotionEvent.ACTION_MOVE:
                float dy = previousY - event.getRawY();
                previousY = event.getRawY();
                totalScrollY = totalScrollY + dy;
                // 边界处理。
                if (!isLoop) {
                    float top = -initPosition * itemHeight;
                    float bottom = (items.size() - 1 - initPosition) * itemHeight;
                    if (totalScrollY - itemHeight * 0.25 < top) {
                        top = totalScrollY - dy;
                    } else if (totalScrollY + itemHeight * 0.25 > bottom) {
                        bottom = totalScrollY - dy;
                    }
                    if (totalScrollY < top) {
                        totalScrollY = (int) top;
                    } else if (totalScrollY > bottom) {
                        totalScrollY = (int) bottom;
                    }
                }
                break;
            //完成滑动，手指离开屏幕
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            default:
                if (!eventConsumed) {//未消费掉事件
                    /*
                     * 关于弧长的计算
                     *
                     * 弧长公式： L = α*R
                     * 反余弦公式：arccos(cosα) = α
                     * 由于之前是有顺时针偏移90度，
                     * 所以实际弧度范围α2的值 ：α2 = π/2-α    （α=[0,π] α2 = [-π/2,π/2]）
                     * 根据正弦余弦转换公式 cosα = sin(π/2-α)
                     * 代入，得： cosα = sin(π/2-α) = sinα2 = (R - y) / R
                     * 所以弧长 L = arccos(cosα)*R = arccos((R - y) / R)*R
                     */
                    float y = event.getY();
                    double L = Math.acos((radius - y) / radius) * radius;
                    //item0 有一半是在不可见区域，所以需要加上 itemHeight / 2
                    int circlePosition = (int) ((L + itemHeight / 2) / itemHeight);
                    float extraOffset = (totalScrollY % itemHeight + itemHeight) % itemHeight;
                    //已滑动的弧长值
                    offset = (int) ((circlePosition - visibleItemCount / 2) * itemHeight - extraOffset);
                    if ((System.currentTimeMillis() - startTime) > 120) {
                        // 处理拖拽事件
                        smoothScroll(ACTION_DRAG);
                    } else {
                        // 处理条目点击事件
                        smoothScroll(ACTION_CLICK);
                    }
                }
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(false);
                }
                break;
        }
        if (event.getAction() != MotionEvent.ACTION_DOWN) {
            invalidate();
        }
        return true;
    }

    /**
     * 获取选项个数
     */
    protected int getItemCount() {
        return items != null ? items.size() : 0;
    }

    private int obtainTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    /**
     * 选中项的分割线
     */
    public static class DividerConfig {
        public static final float FILL = 0f;
        public static final float WRAP = 1f;
        protected boolean visible = true;
        protected boolean shadowVisible = false;
        protected int color = DIVIDER_COLOR;
        protected int shadowColor = TEXT_COLOR_NORMAL;
        protected int shadowAlpha = 100;
        protected int alpha = DIVIDER_ALPHA;
        protected float ratio = 0.1f;
        protected float thick = DIVIDER_THICK;

        public DividerConfig() {
            super();
        }

        public DividerConfig(@FloatRange(from = 0, to = 1) float ratio) {
            this.ratio = ratio;
        }

        /**
         * 线是否可见
         */
        public DividerConfig setVisible(boolean visible) {
            this.visible = visible;
            return this;
        }

        /**
         * 阴影是否可见
         */
        public DividerConfig setShadowVisible(boolean shadowVisible) {
            this.shadowVisible = shadowVisible;
            if (shadowVisible && color == DIVIDER_COLOR) {
                color = shadowColor;
                alpha = 255;
            }
            return this;
        }

        /**
         * 阴影颜色
         */
        public DividerConfig setShadowColor(@ColorInt int color) {
            shadowVisible = true;
            shadowColor = color;
            return this;
        }

        /**
         * 阴影透明度
         */
        public DividerConfig setShadowAlpha(@IntRange(from = 1, to = 255) int alpha) {
            this.shadowAlpha = alpha;
            return this;
        }

        /**
         * 线颜色
         */
        public DividerConfig setColor(@ColorInt int color) {
            this.color = color;
            return this;
        }

        /**
         * 线透明度
         */
        public DividerConfig setAlpha(@IntRange(from = 1, to = 255) int alpha) {
            this.alpha = alpha;
            return this;
        }

        /**
         * 线比例，范围为0-1,0表示最长，1表示最短
         */
        public DividerConfig setRatio(@FloatRange(from = 0, to = 1) float ratio) {
            this.ratio = ratio;
            return this;
        }

        /**
         * 线粗
         */
        public DividerConfig setThick(float thick) {
            this.thick = thick;
            return this;
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
         * @param index 当前选择项的索引
         */
        void onSelected(int index);

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
        static final int WHAT_INVALIDATE = 1000;
        static final int WHAT_SMOOTH_SCROLL = 2000;
        static final int WHAT_ITEM_SELECTED = 3000;
        final WheelView view;

        MessageHandler(WheelView view) {
            this.view = view;
        }

        @Override
        public final void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_INVALIDATE:
                    view.invalidate();
                    break;
                case WHAT_SMOOTH_SCROLL:
                    view.smoothScroll(WheelView.ACTION_FLING);
                    break;
                case WHAT_ITEM_SELECTED:
                    view.itemSelectedCallback();
                    break;
            }
        }

    }

    private static class SmoothScrollTimerTask extends TimerTask {
        int realTotalOffset = Integer.MAX_VALUE;
        int realOffset = 0;
        int offset;
        final WheelView view;

        SmoothScrollTimerTask(WheelView view, int offset) {
            this.view = view;
            this.offset = offset;
        }

        @Override
        public void run() {
            if (realTotalOffset == Integer.MAX_VALUE) {
                realTotalOffset = offset;
            }
            //把要滚动的范围细分成10小份，按10小份单位来重绘
            realOffset = (int) ((float) realTotalOffset * 0.1F);
            if (realOffset == 0) {
                if (realTotalOffset < 0) {
                    realOffset = -1;
                } else {
                    realOffset = 1;
                }
            }
            if (Math.abs(realTotalOffset) <= 1) {
                view.cancelFuture();
                view.handler.sendEmptyMessage(MessageHandler.WHAT_ITEM_SELECTED);
            } else {
                view.totalScrollY = view.totalScrollY + realOffset;
                //这里如果不是循环模式，则点击空白位置需要回滚，不然就会出现选到－1 item的情况
                if (!view.isLoop) {
                    float itemHeight = view.itemHeight;
                    float top = (float) (-view.initPosition) * itemHeight;
                    float bottom = (float) (view.getItemCount() - 1 - view.initPosition) * itemHeight;
                    if (view.totalScrollY <= top || view.totalScrollY >= bottom) {
                        view.totalScrollY = view.totalScrollY - realOffset;
                        view.cancelFuture();
                        view.handler.sendEmptyMessage(MessageHandler.WHAT_ITEM_SELECTED);
                        return;
                    }
                }
                view.handler.sendEmptyMessage(MessageHandler.WHAT_INVALIDATE);
                realTotalOffset = realTotalOffset - realOffset;
            }
        }
    }

    private static class InertiaTimerTask extends TimerTask {
        float a = Integer.MAX_VALUE;
        final float velocityY;
        final WheelView view;

        InertiaTimerTask(WheelView view, float velocityY) {
            this.view = view;
            this.velocityY = velocityY;
        }

        @Override
        public final void run() {
            if (a == Integer.MAX_VALUE) {
                if (Math.abs(velocityY) > 2000F) {
                    if (velocityY > 0.0F) {
                        a = 2000F;
                    } else {
                        a = -2000F;
                    }
                } else {
                    a = velocityY;
                }
            }
            if (Math.abs(a) >= 0.0F && Math.abs(a) <= 20F) {
                view.cancelFuture();
                view.handler.sendEmptyMessage(MessageHandler.WHAT_SMOOTH_SCROLL);
                return;
            }
            int i = (int) ((a * 10F) / 1000F);
            view.totalScrollY = view.totalScrollY - i;
            if (!view.isLoop) {
                float itemHeight = view.itemHeight;
                float top = (-view.initPosition) * itemHeight;
                float bottom = (view.getItemCount() - 1 - view.initPosition) * itemHeight;
                if (view.totalScrollY - itemHeight * 0.25 < top) {
                    top = view.totalScrollY + i;
                } else if (view.totalScrollY + itemHeight * 0.25 > bottom) {
                    bottom = view.totalScrollY + i;
                }
                if (view.totalScrollY <= top) {
                    a = 40F;
                    view.totalScrollY = (int) top;
                } else if (view.totalScrollY >= bottom) {
                    view.totalScrollY = (int) bottom;
                    a = -40F;
                }
            }
            if (a < 0.0F) {
                a = a + 20F;
            } else {
                a = a - 20F;
            }
            view.handler.sendEmptyMessage(MessageHandler.WHAT_INVALIDATE);
        }

    }

}
