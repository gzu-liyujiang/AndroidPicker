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
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import androidx.annotation.StyleRes;

import com.github.gzuliyujiang.wheelpicker.R;
import com.github.gzuliyujiang.wheelview.annotation.CurtainCorner;
import com.github.gzuliyujiang.wheelview.annotation.ItemTextAlign;
import com.github.gzuliyujiang.wheelview.annotation.ScrollState;
import com.github.gzuliyujiang.wheelview.contract.OnWheelChangedListener;
import com.github.gzuliyujiang.wheelview.widget.WheelView;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象的滚轮控件
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2021/6/5 16:18
 */
@SuppressWarnings("unused")
public abstract class BaseWheelLayout extends LinearLayout implements OnWheelChangedListener {
    private final List<WheelView> wheelViews = new ArrayList<>();

    public BaseWheelLayout(Context context) {
        super(context);
        init(context, null, R.attr.WheelStyle, R.style.WheelDefault);
    }

    public BaseWheelLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, R.attr.WheelStyle, R.style.WheelDefault);
    }

    public BaseWheelLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, R.style.WheelDefault);
    }

    public BaseWheelLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        setOrientation(VERTICAL);
        inflate(context, provideLayoutRes(), this);
        onInit(context);
        wheelViews.clear();
        wheelViews.addAll(provideWheelViews());
        initAttrs(context, attrs, defStyleAttr, defStyleRes);
        for (WheelView wheelView : wheelViews) {
            wheelView.setOnWheelChangedListener(this);
        }
    }

    protected void onInit(@NonNull Context context) {

    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        float density = context.getResources().getDisplayMetrics().density;
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseWheelLayout, defStyleAttr, defStyleRes);
        setVisibleItemCount(typedArray.getInt(R.styleable.BaseWheelLayout_wheel_visibleItemCount, 5));
        setSameWidthEnabled(typedArray.getBoolean(R.styleable.BaseWheelLayout_wheel_sameWidthEnabled, false));
        setMaxWidthText(typedArray.getString(R.styleable.BaseWheelLayout_wheel_maxWidthText));
        setTextColor(typedArray.getColor(R.styleable.BaseWheelLayout_wheel_itemTextColor, 0xFF888888));
        setSelectedTextColor(typedArray.getColor(R.styleable.BaseWheelLayout_wheel_itemTextColorSelected, 0xFF000000));
        setTextSize(typedArray.getDimension(R.styleable.BaseWheelLayout_wheel_itemTextSize, 15 * scaledDensity));
        setSelectedTextSize(typedArray.getDimension(R.styleable.BaseWheelLayout_wheel_itemTextSizeSelected, 15 * scaledDensity));
        setSelectedTextBold(typedArray.getBoolean(R.styleable.BaseWheelLayout_wheel_itemTextBoldSelected, false));
        setTextAlign(typedArray.getInt(R.styleable.BaseWheelLayout_wheel_itemTextAlign, ItemTextAlign.CENTER));
        setItemSpace(typedArray.getDimensionPixelSize(R.styleable.BaseWheelLayout_wheel_itemSpace, (int) (20 * density)));
        setCyclicEnabled(typedArray.getBoolean(R.styleable.BaseWheelLayout_wheel_cyclicEnabled, false));
        setIndicatorEnabled(typedArray.getBoolean(R.styleable.BaseWheelLayout_wheel_indicatorEnabled, false));
        setIndicatorColor(typedArray.getColor(R.styleable.BaseWheelLayout_wheel_indicatorColor, 0xFFC9C9C9));
        setIndicatorSize(typedArray.getDimension(R.styleable.BaseWheelLayout_wheel_indicatorSize, 1 * density));
        setCurvedIndicatorSpace(typedArray.getDimensionPixelSize(R.styleable.BaseWheelLayout_wheel_curvedIndicatorSpace, (int) (1 * density)));
        setCurtainEnabled(typedArray.getBoolean(R.styleable.BaseWheelLayout_wheel_curtainEnabled, false));
        setCurtainColor(typedArray.getColor(R.styleable.BaseWheelLayout_wheel_curtainColor, 0x88FFFFFF));
        setCurtainCorner(typedArray.getInt(R.styleable.BaseWheelLayout_wheel_curtainCorner, CurtainCorner.NONE));
        setCurtainRadius(typedArray.getDimension(R.styleable.BaseWheelLayout_wheel_curtainRadius, 0));
        setAtmosphericEnabled(typedArray.getBoolean(R.styleable.BaseWheelLayout_wheel_atmosphericEnabled, false));
        setCurvedEnabled(typedArray.getBoolean(R.styleable.BaseWheelLayout_wheel_curvedEnabled, false));
        setCurvedMaxAngle(typedArray.getInteger(R.styleable.BaseWheelLayout_wheel_curvedMaxAngle, 90));
        typedArray.recycle();
        onAttributeSet(context, attrs);
    }

    protected void onAttributeSet(@NonNull Context context, @Nullable AttributeSet attrs) {

    }

    @LayoutRes
    protected abstract int provideLayoutRes();

    protected abstract List<WheelView> provideWheelViews();

    public void setStyle(@StyleRes int style) {
        initAttrs(getContext(), null, R.attr.WheelStyle, style);
        requestLayout();
        invalidate();
    }

    @Override
    public void onWheelScrolled(WheelView view, int offset) {

    }

    @Override
    public void onWheelScrollStateChanged(WheelView view, @ScrollState int state) {

    }

    @Override
    public void onWheelLoopFinished(WheelView view) {

    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for (WheelView wheelView : wheelViews) {
            wheelView.setEnabled(enabled);
        }
    }

    public void setVisibleItemCount(int visibleItemCount) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setVisibleItemCount(visibleItemCount);
        }
    }

    public void setItemSpace(@Px int space) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setItemSpace(space);
        }
    }

    public void setSameWidthEnabled(boolean sameWidthEnabled) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setSameWidthEnabled(sameWidthEnabled);
        }
    }

    public void setDefaultItemPosition(int position) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setDefaultPosition(position);
        }
    }

    public void setCurtainEnabled(boolean hasCurtain) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setCurtainEnabled(hasCurtain);
        }
    }

    public void setCurtainColor(@ColorInt int color) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setCurtainColor(color);
        }
    }

    public void setCurtainCorner(@CurtainCorner int corner) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setCurtainCorner(corner);
        }
    }

    public void setCurtainRadius(@Px float radius) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setCurtainRadius(radius);
        }
    }

    public void setAtmosphericEnabled(boolean hasAtmospheric) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setAtmosphericEnabled(hasAtmospheric);
        }
    }

    public void setCurvedEnabled(boolean curved) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setCurvedEnabled(curved);
        }
    }

    public void setCurvedMaxAngle(int curvedMaxAngle) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setCurvedMaxAngle(curvedMaxAngle);
        }
    }

    public void setCurvedIndicatorSpace(@Px int space) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setCurvedIndicatorSpace(space);
        }
    }

    public void setCyclicEnabled(boolean cyclic) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setCyclicEnabled(cyclic);
        }
    }

    public void setIndicatorEnabled(boolean hasIndicator) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setIndicatorEnabled(hasIndicator);
        }
    }

    public void setIndicatorSize(@Px float size) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setIndicatorSize(size);
        }
    }

    public void setIndicatorColor(@ColorInt int color) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setIndicatorColor(color);
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

    public void setTextSize(@Px float textSize) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setTextSize(textSize);
        }
    }

    public void setSelectedTextSize(@Px float textSize) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setSelectedTextSize(textSize);
        }
    }

    public void setTextColor(@ColorInt int color) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setTextColor(color);
        }
    }

    public void setSelectedTextColor(@ColorInt int color) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setSelectedTextColor(color);
        }
    }

    public void setSelectedTextBold(boolean bold) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setSelectedTextBold(bold);
        }
    }

    public void setTextAlign(@ItemTextAlign int align) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setTextAlign(align);
        }
    }

}
