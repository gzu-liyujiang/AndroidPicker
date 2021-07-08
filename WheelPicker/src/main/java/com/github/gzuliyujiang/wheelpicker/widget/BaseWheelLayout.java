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
import androidx.annotation.StyleableRes;

import com.github.gzuliyujiang.wheelpicker.R;
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
    private AttributeSet attrs;

    public BaseWheelLayout(Context context) {
        super(context);
        init(context, null);
    }

    public BaseWheelLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, provideStyleableRes(),
                R.attr.WheelStyle, R.style.WheelDefault);
        onAttributeSet(context, a);
        a.recycle();
    }

    public BaseWheelLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, provideStyleableRes(),
                defStyleAttr, R.style.WheelDefault);
        onAttributeSet(context, a);
        a.recycle();
    }

    public BaseWheelLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, provideStyleableRes(),
                defStyleAttr, defStyleRes);
        onAttributeSet(context, a);
        a.recycle();
    }

    private void init(Context context, AttributeSet attrs) {
        this.attrs = attrs;
        setOrientation(VERTICAL);
        inflate(context, provideLayoutRes(), this);
        onInit(context);
        wheelViews.addAll(provideWheelViews());
        for (WheelView wheelView : wheelViews) {
            wheelView.setOnWheelChangedListener(this);
        }
    }

    protected void onInit(@NonNull Context context) {

    }

    protected void onAttributeSet(@NonNull Context context, @NonNull TypedArray typedArray) {

    }

    @LayoutRes
    protected abstract int provideLayoutRes();

    @StyleableRes
    protected abstract int[] provideStyleableRes();

    protected abstract List<WheelView> provideWheelViews();

    public void setStyle(@StyleRes int style) {
        if (attrs == null) {
            throw new RuntimeException("Please use " + getClass().getSimpleName() + " in xml");
        }
        TypedArray a = getContext().obtainStyledAttributes(attrs, provideStyleableRes(), R.attr.WheelStyle, style);
        onAttributeSet(getContext(), a);
        a.recycle();
        requestLayout();
        postInvalidate();
    }

    @Override
    public void onWheelScrolled(WheelView view, int offset) {

    }

    @Override
    public void onWheelScrollStateChanged(WheelView view, int state) {

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

    public void setItemSpace(@Px int space) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setItemSpace(space);
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

    public void setTextSize(@Px int textSize) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setTextSize(textSize);
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

    public void setMaxWidthText(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        for (WheelView wheelView : wheelViews) {
            wheelView.setMaxWidthText(text);
        }
    }

    public void setSelectedTextColor(@ColorInt int color) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setSelectedTextColor(color);
        }
    }

    public void setTextColor(@ColorInt int color) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setTextColor(color);
        }
    }

    public void setVisibleItemCount(int visibleItemCount) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setVisibleItemCount(visibleItemCount);
        }
    }

    public void setTextAlign(int align) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setTextAlign(align);
        }
    }

}
