package cn.qqtheme.framework.wheelview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import cn.qqtheme.framework.toolkit.CqrDensity;
import cn.qqtheme.framework.wheelview.R;
import cn.qqtheme.framework.wheelview.annotation.ItemAlign;
import cn.qqtheme.framework.wheelview.interfaces.LinkageDataProvider;
import cn.qqtheme.framework.wheelview.interfaces.LinkageTextProvider;
import cn.qqtheme.framework.wheelview.interfaces.OnWheelChangedListener;
import cn.qqtheme.framework.wheelview.interfaces.OnWheelSelectedListener;
import cn.qqtheme.framework.wheelview.interfaces.TextProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 二三级联动滚轮控件
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/6/15 11:55
 * @since 2.0
 */
@SuppressWarnings("unused")
public class LinkageWheelLayout<F extends LinkageTextProvider, S extends LinkageTextProvider,
        T extends TextProvider> extends LinearLayout implements OnWheelChangedListener {
    private static final int DELAY_BEFORE_CHECK_PAST = 100;

    private WheelView<F> firstWheelView;
    private WheelView<S> secondWheelView;
    private WheelView<T> thirdWheelView;

    private int firstIndex;
    private int secondIndex;
    private int thirdIndex;

    private List<WheelView> wheelViews = new ArrayList<>();

    private LinkageDataProvider<F, S, T> dataProvider;

    private AttributeSet attrs;

    public LinkageWheelLayout(Context context) {
        this(context, null);
    }

    public LinkageWheelLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.WheelLinkageStyle);
    }

    public LinkageWheelLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, R.style.WheelLinkage);
    }

    public LinkageWheelLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        this.attrs = attrs;
        setOrientation(VERTICAL);
        inflate(context, R.layout.include_linkage, this);
        firstWheelView = findViewById(R.id.first_wheel_view);
        secondWheelView = findViewById(R.id.second_wheel_view);
        thirdWheelView = findViewById(R.id.third_wheel_view);
        wheelViews.addAll(Arrays.asList(
                firstWheelView, secondWheelView, thirdWheelView
        ));
        initAttrs(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setStyle(@StyleRes int style) {
        if (attrs == null) {
            throw new RuntimeException("Please use " + getClass().getSimpleName() + " in xml");
        }
        initAttrs(getContext(), attrs, R.attr.WheelLinkageStyle, style);
        requestLayout();
        postInvalidate();
    }

    public void setDataProvider(@NonNull LinkageDataProvider<F, S, T> provider) {
        dataProvider = provider;
        changeFirstData();
        changeSecondData();
        if (dataProvider.isOnlyTwoLevel()) {
            thirdWheelView.setVisibility(GONE);
            return;
        }
        thirdWheelView.setVisibility(VISIBLE);
        changeThirdData();
        setWheelListener();
    }

    public void setDefaultIndex(int firstIndex, int secondIndex, int thirdIndex) {
        if (dataProvider == null) {
            throw new RuntimeException("You must set data provider at first");
        }
        this.firstIndex = firstIndex;
        this.secondIndex = secondIndex;
        this.thirdIndex = thirdIndex;
    }

    public final WheelView<F> getFirstWheelView() {
        return firstWheelView;
    }

    public final WheelView<S> getSecondWheelView() {
        return secondWheelView;
    }

    public final WheelView<T> getThirdWheelView() {
        return thirdWheelView;
    }

    private void setWheelListener() {
        firstWheelView.setWheelSelectedListener(new OnWheelSelectedListener<F>() {
            @Override
            public void onItemSelected(WheelView wheelView, int position, F item) {
                firstIndex = position;
                secondIndex = 0;
                thirdIndex = 0;
                changeSecondData();
                changeThirdData();
            }
        });
        secondWheelView.setWheelSelectedListener(new OnWheelSelectedListener<S>() {
            @Override
            public void onItemSelected(WheelView wheelView, int position, S item) {
                secondIndex = position;
                thirdIndex = 0;
                changeThirdData();
            }
        });
        thirdWheelView.setWheelSelectedListener(new OnWheelSelectedListener<T>() {
            @Override
            public void onItemSelected(WheelView wheelView, int position, T item) {
                thirdIndex = position;
            }
        });

        firstWheelView.setWheelChangedListener(this);
        secondWheelView.setWheelChangedListener(this);
        thirdWheelView.setWheelChangedListener(this);
    }

    @Override
    public void onWheelScrolled(WheelView wheelView, int offset) {

    }

    @Override
    public void onWheelSelected(WheelView wheelView, int position) {

    }

    @Override
    public void onWheelScrollStateChanged(WheelView wheelView, int state) {
        // 除当前操作的滚轮外，其他滚轮在有滚轮滚动时设置触摸事件不可用，
        // 防止快速来回切换多个滚轮可能会出现某些项显示为空的问题
        setEnabled(state == WheelView.SCROLL_STATE_IDLE);
        wheelView.setEnabled(true);
    }

    private void changeFirstData() {
        firstWheelView.postDelayed(new Runnable() {
            @Override
            public void run() {
                firstWheelView.setData(dataProvider.provideFirstData());
                firstWheelView.setDefaultItemPosition(firstIndex);
            }
        }, DELAY_BEFORE_CHECK_PAST);
    }

    private void changeSecondData() {
        secondWheelView.postDelayed(new Runnable() {
            @Override
            public void run() {
                secondWheelView.setData(dataProvider.linkageSecondData(firstIndex));
                secondWheelView.setDefaultItemPosition(secondIndex);
            }
        }, DELAY_BEFORE_CHECK_PAST);
    }

    private void changeThirdData() {
        thirdWheelView.postDelayed(new Runnable() {
            @Override
            public void run() {
                thirdWheelView.setData(dataProvider.linkageThirdData(firstIndex, secondIndex));
                thirdWheelView.setDefaultItemPosition(thirdIndex);
            }
        }, DELAY_BEFORE_CHECK_PAST);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for (WheelView wheelView : wheelViews) {
            wheelView.setEnabled(enabled);
        }
    }

    public void setCurtain(boolean hasCurtain) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setCurtain(hasCurtain);
        }
    }

    public void setCurtainColor(@ColorInt int color) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setCurtainColor(color);
        }
    }

    public void setAtmospheric(boolean hasAtmospheric) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setAtmospheric(hasAtmospheric);
        }
    }

    public void setCurved(boolean curved) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setCurved(curved);
        }
    }

    public void setItemSpace(int space) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setItemSpace(space);
        }
    }

    public void setCyclic(boolean cyclic) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setCyclic(cyclic);
        }
    }

    public void setIndicator(boolean hasIndicator) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setIndicator(hasIndicator);
        }
    }

    public void setIndicatorSize(int size) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setIndicatorSize(size);
        }
    }

    public void setIndicatorColor(@ColorInt int color) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setIndicatorColor(color);
        }
    }

    public void setTextSize(int textSize) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setTextSize(textSize);
        }
    }

    public void setSameWidth(boolean hasSameWidth) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setSameWidth(hasSameWidth);
        }
    }

    public void setDefaultItemPosition(int position) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setDefaultItemPosition(position);
        }
    }

    public void setMaxWidthTextPosition(int position) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setMaxWidthTextPosition(position);
        }
    }

    public void setMaxWidthText(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        for (WheelView wheelView : wheelViews) {
            wheelView.setMaxWidthText(text);
        }
    }

    public void setSelectedTextColor(int selectedTextColor) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setSelectedTextColor(selectedTextColor);
        }
    }

    public void setTextColor(int textColor) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setTextColor(textColor);
        }
    }

    public void setVisibleItemCount(int visibleItemCount) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setVisibleItemCount(visibleItemCount);
        }
    }

    public void setItemAlign(@ItemAlign int align) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setItemAlign(align);
        }
    }

    public void setOnlyTwoLevel(boolean onlyTwoLevel) {
        if (onlyTwoLevel) {
            thirdWheelView.setVisibility(GONE);
        } else {
            thirdWheelView.setVisibility(VISIBLE);
        }
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (attrs == null) {
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LinkageWheelLayout,
                defStyleAttr, defStyleRes);

        setTextSize(a.getDimensionPixelSize(R.styleable.LinkageWheelLayout_wheel_itemTextSize,
                CqrDensity.sp2px(context, 20)));
        setVisibleItemCount(a.getInt(R.styleable.LinkageWheelLayout_wheel_itemVisibleCount, 5));
        setDefaultItemPosition(a.getInt(R.styleable.LinkageWheelLayout_wheel_itemDefaultPosition, 0));
        setSameWidth(a.getBoolean(R.styleable.LinkageWheelLayout_wheel_hasSameWidth, false));
        setMaxWidthTextPosition(a.getInt(R.styleable.LinkageWheelLayout_wheel_maxWidthTextPosition, 0));
        setMaxWidthText(a.getString(R.styleable.LinkageWheelLayout_wheel_maxWidthText));
        setSelectedTextColor(a.getColor(R.styleable.LinkageWheelLayout_wheel_itemTextColorSelected, 0xFF000000));
        setTextColor(a.getColor(R.styleable.LinkageWheelLayout_wheel_itemTextColor, 0xFF888888));
        setItemSpace(a.getDimensionPixelSize(R.styleable.LinkageWheelLayout_wheel_itemSpace,
                CqrDensity.dp2px(context, 15)));
        setCyclic(a.getBoolean(R.styleable.LinkageWheelLayout_wheel_cyclic, false));
        setIndicator(a.getBoolean(R.styleable.LinkageWheelLayout_wheel_indicator, false));
        setIndicatorColor(a.getColor(R.styleable.LinkageWheelLayout_wheel_indicatorColor, 0xFFEE3333));
        setIndicatorSize(a.getDimensionPixelSize(R.styleable.LinkageWheelLayout_wheel_indicatorSize,
                CqrDensity.sp2px(context, 1)));
        setCurtain(a.getBoolean(R.styleable.LinkageWheelLayout_wheel_curtain, false));
        setCurtainColor(a.getColor(R.styleable.LinkageWheelLayout_wheel_curtainColor, 0x88FFFFFF));
        setAtmospheric(a.getBoolean(R.styleable.LinkageWheelLayout_wheel_atmospheric, false));
        setCurved(a.getBoolean(R.styleable.LinkageWheelLayout_wheel_curved, false));
        setItemAlign(a.getInt(R.styleable.LinkageWheelLayout_wheel_itemAlign, ItemAlign.CENTER));
        setOnlyTwoLevel(a.getBoolean(R.styleable.LinkageWheelLayout_wheel_onlyTwoLevel, false));

        a.recycle();
    }

}
