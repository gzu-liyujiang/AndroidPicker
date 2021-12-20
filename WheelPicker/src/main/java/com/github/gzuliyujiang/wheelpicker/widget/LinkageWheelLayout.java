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

package com.github.gzuliyujiang.wheelpicker.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.gzuliyujiang.wheelpicker.R;
import com.github.gzuliyujiang.wheelpicker.contract.LinkageProvider;
import com.github.gzuliyujiang.wheelpicker.contract.OnLinkageSelectedListener;
import com.github.gzuliyujiang.wheelview.annotation.ScrollState;
import com.github.gzuliyujiang.wheelview.contract.WheelFormatter;
import com.github.gzuliyujiang.wheelview.widget.WheelView;

import java.util.Arrays;
import java.util.List;

/**
 * 二三级联动滚轮控件
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2019/6/15 11:55
 */
@SuppressWarnings("unused")
public class LinkageWheelLayout extends BaseWheelLayout {
    private WheelView firstWheelView, secondWheelView, thirdWheelView;
    private TextView firstLabelView, secondLabelView, thirdLabelView;
    private ProgressBar loadingView;
    private Object firstValue, secondValue, thirdValue;
    private int firstIndex, secondIndex, thirdIndex;
    private LinkageProvider dataProvider;
    private OnLinkageSelectedListener onLinkageSelectedListener;

    public LinkageWheelLayout(Context context) {
        super(context);
    }

    public LinkageWheelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LinkageWheelLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LinkageWheelLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected int provideLayoutRes() {
        return R.layout.wheel_picker_linkage;
    }

    @CallSuper
    @Override
    protected List<WheelView> provideWheelViews() {
        return Arrays.asList(firstWheelView, secondWheelView, thirdWheelView);
    }

    @CallSuper
    @Override
    protected void onInit(@NonNull Context context) {
        firstWheelView = findViewById(R.id.wheel_picker_linkage_first_wheel);
        secondWheelView = findViewById(R.id.wheel_picker_linkage_second_wheel);
        thirdWheelView = findViewById(R.id.wheel_picker_linkage_third_wheel);
        firstLabelView = findViewById(R.id.wheel_picker_linkage_first_label);
        secondLabelView = findViewById(R.id.wheel_picker_linkage_second_label);
        thirdLabelView = findViewById(R.id.wheel_picker_linkage_third_label);
        loadingView = findViewById(R.id.wheel_picker_linkage_loading);
    }

    @CallSuper
    @Override
    protected void onAttributeSet(@NonNull Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LinkageWheelLayout);
        setFirstVisible(typedArray.getBoolean(R.styleable.LinkageWheelLayout_wheel_firstVisible, true));
        setThirdVisible(typedArray.getBoolean(R.styleable.LinkageWheelLayout_wheel_thirdVisible, true));
        String firstLabel = typedArray.getString(R.styleable.LinkageWheelLayout_wheel_firstLabel);
        String secondLabel = typedArray.getString(R.styleable.LinkageWheelLayout_wheel_secondLabel);
        String thirdLabel = typedArray.getString(R.styleable.LinkageWheelLayout_wheel_thirdLabel);
        typedArray.recycle();
        setLabel(firstLabel, secondLabel, thirdLabel);
    }

    @CallSuper
    @Override
    public void onWheelSelected(WheelView view, int position) {
        int id = view.getId();
        if (id == R.id.wheel_picker_linkage_first_wheel) {
            firstIndex = position;
            secondIndex = 0;
            thirdIndex = 0;
            changeSecondData();
            changeThirdData();
            selectedCallback();
            return;
        }
        if (id == R.id.wheel_picker_linkage_second_wheel) {
            secondIndex = position;
            thirdIndex = 0;
            changeThirdData();
            selectedCallback();
            return;
        }
        if (id == R.id.wheel_picker_linkage_third_wheel) {
            thirdIndex = position;
            selectedCallback();
        }
    }

    @CallSuper
    @Override
    public void onWheelScrollStateChanged(WheelView view, @ScrollState int state) {
        int id = view.getId();
        if (id == R.id.wheel_picker_linkage_first_wheel) {
            secondWheelView.setEnabled(state == ScrollState.IDLE);
            thirdWheelView.setEnabled(state == ScrollState.IDLE);
            return;
        }
        if (id == R.id.wheel_picker_linkage_second_wheel) {
            firstWheelView.setEnabled(state == ScrollState.IDLE);
            thirdWheelView.setEnabled(state == ScrollState.IDLE);
            return;
        }
        if (id == R.id.wheel_picker_linkage_third_wheel) {
            firstWheelView.setEnabled(state == ScrollState.IDLE);
            secondWheelView.setEnabled(state == ScrollState.IDLE);
        }
    }

    public void setData(@NonNull LinkageProvider provider) {
        setFirstVisible(provider.firstLevelVisible());
        setThirdVisible(provider.thirdLevelVisible());
        if (firstValue != null) {
            firstIndex = provider.findFirstIndex(firstValue);
        }
        if (secondValue != null) {
            secondIndex = provider.findSecondIndex(firstIndex, secondValue);
        }
        if (thirdValue != null) {
            thirdIndex = provider.findThirdIndex(firstIndex, secondIndex, thirdValue);
        }
        dataProvider = provider;
        changeFirstData();
        changeSecondData();
        changeThirdData();
    }

    public void setDefaultValue(Object first, Object second, Object third) {
        if (dataProvider != null) {
            firstIndex = dataProvider.findFirstIndex(first);
            secondIndex = dataProvider.findSecondIndex(firstIndex, second);
            thirdIndex = dataProvider.findThirdIndex(firstIndex, secondIndex, third);
            changeFirstData();
            changeSecondData();
            changeThirdData();
        } else {
            this.firstValue = first;
            this.secondValue = second;
            this.thirdValue = third;
        }
    }

    public void setFormatter(WheelFormatter first, WheelFormatter second, WheelFormatter third) {
        firstWheelView.setFormatter(first);
        secondWheelView.setFormatter(second);
        thirdWheelView.setFormatter(third);
    }

    public void setLabel(CharSequence first, CharSequence second, CharSequence third) {
        firstLabelView.setText(first);
        secondLabelView.setText(second);
        thirdLabelView.setText(third);
    }

    public void showLoading() {
        loadingView.setVisibility(VISIBLE);
    }

    public void hideLoading() {
        loadingView.setVisibility(GONE);
    }

    public void setOnLinkageSelectedListener(OnLinkageSelectedListener onLinkageSelectedListener) {
        this.onLinkageSelectedListener = onLinkageSelectedListener;
    }

    public void setFirstVisible(boolean visible) {
        if (visible) {
            firstWheelView.setVisibility(VISIBLE);
            firstLabelView.setVisibility(VISIBLE);
        } else {
            firstWheelView.setVisibility(GONE);
            firstLabelView.setVisibility(GONE);
        }
    }

    public void setThirdVisible(boolean visible) {
        if (visible) {
            thirdWheelView.setVisibility(VISIBLE);
            thirdLabelView.setVisibility(VISIBLE);
        } else {
            thirdWheelView.setVisibility(GONE);
            thirdLabelView.setVisibility(GONE);
        }
    }

    private void selectedCallback() {
        if (onLinkageSelectedListener == null) {
            return;
        }
        thirdWheelView.post(new Runnable() {
            @Override
            public void run() {
                Object first = firstWheelView.getCurrentItem();
                Object second = secondWheelView.getCurrentItem();
                Object third = thirdWheelView.getCurrentItem();
                onLinkageSelectedListener.onLinkageSelected(first, second, third);
            }
        });
    }

    private void changeFirstData() {
        firstWheelView.setData(dataProvider.provideFirstData());
        firstWheelView.setDefaultPosition(firstIndex);
    }

    private void changeSecondData() {
        secondWheelView.setData(dataProvider.linkageSecondData(firstIndex));
        secondWheelView.setDefaultPosition(secondIndex);
    }

    private void changeThirdData() {
        if (!dataProvider.thirdLevelVisible()) {
            return;
        }
        thirdWheelView.setData(dataProvider.linkageThirdData(firstIndex, secondIndex));
        thirdWheelView.setDefaultPosition(thirdIndex);
    }

    public final WheelView getFirstWheelView() {
        return firstWheelView;
    }

    public final WheelView getSecondWheelView() {
        return secondWheelView;
    }

    public final WheelView getThirdWheelView() {
        return thirdWheelView;
    }

    public final TextView getFirstLabelView() {
        return firstLabelView;
    }

    public final TextView getSecondLabelView() {
        return secondLabelView;
    }

    public final TextView getThirdLabelView() {
        return thirdLabelView;
    }

    public final ProgressBar getLoadingView() {
        return loadingView;
    }

}
