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

package com.github.gzuliyujiang.filepicker.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.gzuliyujiang.filepicker.R;
import com.github.gzuliyujiang.filepicker.contract.OnPathClickedListener;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;

/**
 * 文件路径数据适配
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2017/01/08 01:20
 */
@SuppressWarnings("unused")
public class PathAdapter extends RecyclerView.Adapter<ViewHolder> {
    private static final String ROOT_HINT = "ROOT";
    private final Context context;
    private final LinkedList<String> paths = new LinkedList<>();
    private Drawable arrowIcon = null;
    private OnPathClickedListener onPathClickedListener;

    public PathAdapter(@NonNull Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int wrapContent = ViewGroup.LayoutParams.WRAP_CONTENT;
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER_VERTICAL);
        layout.setLayoutParams(new ViewGroup.LayoutParams(wrapContent, wrapContent));
        TextView textView = new TextView(context);
        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(wrapContent, wrapContent);
        textView.setLayoutParams(tvParams);
        textView.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        int padding = (int) (5 * context.getResources().getDisplayMetrics().density);
        textView.setPadding(padding, 0, padding, 0);
        layout.addView(textView);
        ImageView imageView = new ImageView(context);
        int width = (int) (20 * context.getResources().getDisplayMetrics().density);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(width, wrapContent));
        layout.addView(imageView);
        ViewHolder viewHolder = new ViewHolder(layout);
        viewHolder.textView = textView;
        viewHolder.imageView = imageView;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.textView.setText(paths.get(position));
        holder.imageView.setImageDrawable(arrowIcon);
        if (onPathClickedListener == null) {
            return;
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPathClickedListener.onPathClicked(position, getPath(position));
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return paths.size();
    }

    public void setArrowIcon(Drawable arrowIcon) {
        this.arrowIcon = arrowIcon;
    }

    public void updatePath(File file) {
        if (arrowIcon == null) {
            arrowIcon = ContextCompat.getDrawable(context, R.mipmap.file_picker_arrow);
        }
        paths.clear();
        String path = file.getAbsolutePath();
        if (!File.separator.equals(path)) {
            Collections.addAll(paths, path.substring(path.indexOf(File.separator) + 1)
                    .split(File.separator));
        }
        paths.addFirst(ROOT_HINT);
        notifyDataSetChanged();
    }

    public String getPath(int position) {
        StringBuilder sb = new StringBuilder(File.separator);
        //忽略根目录
        if (position == 0) {
            return sb.toString();
        }
        for (int i = 1; i <= position; i++) {
            sb.append(paths.get(i)).append(File.separator);
        }
        return sb.toString();
    }

    public final void recycleData() {
        paths.clear();
        if (arrowIcon instanceof BitmapDrawable) {
            Bitmap homeBitmap = ((BitmapDrawable) arrowIcon).getBitmap();
            if (null != homeBitmap && !homeBitmap.isRecycled()) {
                homeBitmap.recycle();
            }
        }
    }

    public void setOnPathClickedListener(OnPathClickedListener listener) {
        onPathClickedListener = listener;
    }

}
