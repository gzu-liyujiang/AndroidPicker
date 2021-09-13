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

package com.github.gzuliyujiang.basepicker;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.annotation.Dimension;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

/**
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2021/6/3 15:23
 */
@SuppressWarnings("unused")
public abstract class ConfirmPicker extends BottomDialog implements View.OnClickListener {
    protected View headerView;
    protected TextView cancelView;
    protected TextView titleView;
    protected TextView okView;
    protected View topLineView;
    protected View bodyView;
    protected View footerView;

    public ConfirmPicker(@NonNull Activity activity) {
        super(activity);
    }

    public ConfirmPicker(@NonNull Activity activity, @StyleRes int themeResId) {
        super(activity, themeResId);
    }

    @NonNull
    @Override
    protected View createContentView(@NonNull Activity activity) {
        LinearLayout rootLayout = new LinearLayout(activity);
        rootLayout.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setGravity(Gravity.CENTER);
        rootLayout.setPadding(0, 0, 0, 0);
        rootLayout.setBackgroundColor(0xFFFFFFFF);
        headerView = createHeaderView(activity);
        if (headerView != null) {
            rootLayout.addView(headerView);
        }
        topLineView = createTopLineView(activity);
        if (topLineView != null) {
            rootLayout.addView(topLineView);
        }
        bodyView = createBodyView(activity);
        rootLayout.addView(bodyView, new LinearLayout.LayoutParams(MATCH_PARENT, 0, 1.0f));
        footerView = createFooterView(activity);
        if (footerView != null) {
            rootLayout.addView(footerView);
        }
        return rootLayout;
    }

    @Nullable
    protected View createHeaderView(@NonNull Activity activity) {
        return View.inflate(activity, R.layout.picker_confirm_header, null);
    }

    @Nullable
    protected View createTopLineView(@NonNull Activity activity) {
        View view = new View(activity);
        view.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, 1));
        view.setBackgroundColor(0xFFCCCCCC);
        return view;
    }

    @NonNull
    protected abstract View createBodyView(@NonNull Activity activity);

    @Nullable
    protected View createFooterView(@NonNull Activity activity) {
        return null;
    }

    @CallSuper
    @Override
    protected void initView(@NonNull View contentView) {
        super.initView(contentView);
        cancelView = contentView.findViewById(R.id.confirm_picker_cancel);
        titleView = contentView.findViewById(R.id.confirm_picker_title);
        okView = contentView.findViewById(R.id.confirm_picker_ok);
    }

    @CallSuper
    @Override
    protected void initData() {
        super.initData();
        if (cancelView != null) {
            cancelView.setOnClickListener(this);
        }
        if (okView != null) {
            okView.setOnClickListener(this);
        }
    }

    @CallSuper
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.confirm_picker_cancel) {
            PickerLog.print("cancel clicked");
            onCancel();
            dismiss();
        } else if (id == R.id.confirm_picker_ok) {
            PickerLog.print("ok clicked");
            onOk();
            dismiss();
        }
    }

    protected abstract void onCancel();

    protected abstract void onOk();

    public final void setBodyWidth(@Dimension(unit = Dimension.DP) @IntRange(from = 50) int bodyWidth) {
        ViewGroup.LayoutParams layoutParams = bodyView.getLayoutParams();
        int width = WRAP_CONTENT;
        if (bodyWidth != WRAP_CONTENT && bodyWidth != MATCH_PARENT) {
            width = (int) (bodyView.getResources().getDisplayMetrics().density * bodyWidth);
        }
        layoutParams.width = width;
        bodyView.setLayoutParams(layoutParams);
    }

    public final void setBodyHeight(@Dimension(unit = Dimension.DP) @IntRange(from = 50) int bodyHeight) {
        ViewGroup.LayoutParams layoutParams = bodyView.getLayoutParams();
        int height = WRAP_CONTENT;
        if (bodyHeight != WRAP_CONTENT && bodyHeight != MATCH_PARENT) {
            height = (int) (bodyView.getResources().getDisplayMetrics().density * bodyHeight);
        }
        layoutParams.height = height;
        bodyView.setLayoutParams(layoutParams);
    }

    public final View getHeaderView() {
        return headerView;
    }

    public final View getTopLineView() {
        return topLineView;
    }

    public final View getBodyView() {
        return bodyView;
    }

    public final View getFooterView() {
        return footerView;
    }

    public final TextView getCancelView() {
        return cancelView;
    }

    public final TextView getTitleView() {
        return titleView;
    }

    public final TextView getOkView() {
        return okView;
    }

}
