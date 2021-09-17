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

package com.github.gzuliyujiang.calendarpicker.core;

import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2021/9/17 14:36
 */
public class WeekAdapter extends BaseAdapter {
    private static final String[] DATA = new String[]{
            "日", "一", "二", "三", "四", "五", "六"
    };
    private ColorScheme colorScheme = new ColorScheme();

    public void setColorScheme(ColorScheme colorScheme) {
        if (colorScheme == null) {
            colorScheme = new ColorScheme();
        }
        this.colorScheme = colorScheme;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return DATA.length;
    }

    @Override
    public Object getItem(int position) {
        return DATA[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = new TextView(parent.getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(15);
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        int padding = (int) (parent.getResources().getDisplayMetrics().density * 10);
        textView.setPadding(0, padding, 0, padding);
        textView.setText(DATA[position]);
        textView.setBackgroundColor(colorScheme.weekBackgroundColor());
        textView.setTextColor(colorScheme.weekTextColor());
        return textView;
    }

}
