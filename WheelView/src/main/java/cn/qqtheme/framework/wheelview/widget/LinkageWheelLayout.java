package cn.qqtheme.framework.wheelview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import cn.qqtheme.framework.toolkit.CqrDensity;
import cn.qqtheme.framework.wheelview.R;
import cn.qqtheme.framework.wheelview.interfaces.LinkageDataProvider;
import cn.qqtheme.framework.wheelview.interfaces.LinkageTextProvider;
import cn.qqtheme.framework.wheelview.interfaces.TextProvider;
import cn.qqtheme.framework.wheelview.interfaces.impl.AbstractWheelSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 二三级联动滚轮控件
 *
 * @author liyujiang
 * @date 2019/6/15 11:55
 */
@SuppressWarnings("unused")
public class LinkageWheelLayout<F extends LinkageTextProvider, S extends LinkageTextProvider,
        T extends TextProvider> extends LinearLayout {
    private static final int DELAY_BEFORE_CHECK_PAST = 100;

    private WheelView<F> firstWheelView;
    private WheelView<S> secondWheelView;
    private WheelView<T> thirdWheelView;

    private int firstIndex;
    private int secondIndex;
    private int thirdIndex;

    private List<WheelView> wheelViews = new ArrayList<>();

    private LinkageDataProvider<F, S, T> dataProvider;

    public LinkageWheelLayout(Context context) {
        this(context, null);
    }

    public LinkageWheelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        inflate(context, R.layout.include_linkage, this);
        firstWheelView = findViewById(R.id.first_wheel_view);
        secondWheelView = findViewById(R.id.second_wheel_view);
        thirdWheelView = findViewById(R.id.third_wheel_view);
        wheelViews.addAll(Arrays.asList(
                firstWheelView, secondWheelView, thirdWheelView
        ));
        initAttrs(context, attrs);
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
        firstWheelView.setOnWheelListener(new AbstractWheelSelectedListener() {
            @Override
            public void onItemSelected(WheelView wheelView, int position) {
                firstIndex = position;
                secondIndex = 0;
                thirdIndex = 0;
                changeSecondData();
                changeThirdData();
            }
        });
        secondWheelView.setOnWheelListener(new AbstractWheelSelectedListener() {
            @Override
            public void onItemSelected(WheelView wheelView, int position) {
                secondIndex = position;
                thirdIndex = 0;
                changeThirdData();
            }
        });
        thirdWheelView.setOnWheelListener(new AbstractWheelSelectedListener() {
            @Override
            public void onItemSelected(WheelView wheelView, int position) {
                thirdIndex = position;
            }
        });
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

    public void setCurved(boolean curved) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setCurved(curved);
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

    public void setTextSize(int textSize) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setItemTextSize(textSize);
        }
    }

    public void setSelectedTextColor(int selectedTextColor) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setSelectedItemTextColor(selectedTextColor);
        }
    }

    public void setTextColor(int textColor) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setItemTextColor(textColor);
        }
    }

    public void setVisibleItemCount(int visibleItemCount) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setVisibleItemCount(visibleItemCount);
        }
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LinkageWheelLayout);

        setTextColor(a.getColor(R.styleable.LinkageWheelLayout_address_wheel_textColor, 0xFF999999));
        setSelectedTextColor(a.getColor(R.styleable.LinkageWheelLayout_address_wheel_textColorSelected, 0xFF000000));
        setTextSize(a.getDimensionPixelSize(R.styleable.LinkageWheelLayout_address_wheel_textSize, CqrDensity.sp2px(context, 19)));
        setCurved(a.getBoolean(R.styleable.LinkageWheelLayout_address_wheel_curved, false));
        setCyclic(a.getBoolean(R.styleable.LinkageWheelLayout_address_wheel_cyclic, false));
        setIndicator(a.getBoolean(R.styleable.LinkageWheelLayout_address_wheel_indicator, true));
        setVisibleItemCount(a.getInt(R.styleable.LinkageWheelLayout_address_wheel_visibleItemCount, 7));

        a.recycle();
    }

}
