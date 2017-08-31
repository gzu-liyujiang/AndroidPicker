package cn.qqtheme.framework.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.LinkedList;

import cn.qqtheme.framework.icons.FilePickerIcon;
import cn.qqtheme.framework.util.ConvertUtils;

/**
 * 文件路径数据适配
 * <br />
 * Author:李玉江[QQ:1032694760]
 * DateTime:2017/01/08 01:20
 * Builder:Android Studio
 *
 * @see cn.qqtheme.framework.picker.FilePicker
 */
public class PathAdapter extends BaseAdapter {
    private static final String ROOT_HINT = "ROOT";
    private LinkedList<String> paths = new LinkedList<>();
    private Drawable arrowIcon = null;

    public PathAdapter() {
        super();
    }

    public void setArrowIcon(Drawable arrowIcon) {
        this.arrowIcon = arrowIcon;
    }

    public void updatePath(String path) {
        if (arrowIcon == null) {
            arrowIcon = ConvertUtils.toDrawable(FilePickerIcon.getARROW());
        }
        paths.clear();
        if (!path.equals("/")) {
            String[] tmps = path.substring(path.indexOf("/") + 1).split("/");
            Collections.addAll(paths, tmps);
        }
        paths.addFirst(ROOT_HINT);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return paths.size();
    }

    @Override
    public String getItem(int position) {
        String tmp = "/";
        //忽略根目录
        if (position == 0) {
            return tmp;
        }
        for (int i = 1; i <= position; i++) {
            tmp += paths.get(i) + "/";
        }
        return tmp;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        ViewHolder holder;
        if (convertView == null) {
            int matchParent = ViewGroup.LayoutParams.MATCH_PARENT;
            int wrapContent = ViewGroup.LayoutParams.WRAP_CONTENT;
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setGravity(Gravity.CENTER_VERTICAL);
            // fixed: 17-1-8 #79 安卓4.x兼容问题，java.lang.ClassCastException……onMeasure……
            if (parent instanceof AbsListView) {
                layout.setLayoutParams(new AbsListView.LayoutParams(matchParent, matchParent));
            } else {
                layout.setLayoutParams(new ViewGroup.LayoutParams(matchParent, matchParent));
            }

            TextView textView = new TextView(context);
            LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(wrapContent, matchParent);
            textView.setLayoutParams(tvParams);
            textView.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            int padding = ConvertUtils.toPx(context, 5);
            textView.setPadding(padding, 0, padding, 0);
            layout.addView(textView);

            ImageView imageView = new ImageView(context);
            int width = ConvertUtils.toPx(context, 20);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(width, matchParent));
            layout.addView(imageView);

            convertView = layout;
            holder = new ViewHolder();
            holder.textView = textView;
            holder.imageView = imageView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(paths.get(position));
        holder.imageView.setImageDrawable(arrowIcon);
        return convertView;
    }

    public void recycleData() {
        paths.clear();
        if (arrowIcon instanceof BitmapDrawable) {
            Bitmap homeBitmap = ((BitmapDrawable) arrowIcon).getBitmap();
            if (null != homeBitmap && !homeBitmap.isRecycled()) {
                homeBitmap.recycle();
            }
        }
    }

    private static class ViewHolder {
        TextView textView;
        ImageView imageView;
    }

}
