package cn.qqtheme.framework.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
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

import java.io.File;
import java.util.ArrayList;

import cn.qqtheme.framework.drawable.StateColorDrawable;
import cn.qqtheme.framework.entity.FileItem;
import cn.qqtheme.framework.icons.FilePickerIcon;
import cn.qqtheme.framework.util.CompatUtils;
import cn.qqtheme.framework.util.ConvertUtils;
import cn.qqtheme.framework.util.FileUtils;
import cn.qqtheme.framework.util.LogUtils;

/**
 * 文件目录数据适配
 *
 * @author 李玉江[QQ:1032694760]
 * @see cn.qqtheme.framework.picker.FilePicker
 * @since 2014-05-23 18:02
 */
public class FileAdapter extends BaseAdapter {
    public static final String DIR_ROOT = ".";
    public static final String DIR_PARENT = "..";
    private ArrayList<FileItem> data = new ArrayList<FileItem>();
    private String rootPath = null;
    private String currentPath = null;
    private String[] allowExtensions = null;//允许的扩展名
    private boolean onlyListDir = false;//是否仅仅读取目录
    private boolean showHomeDir = false;//是否显示返回主目录
    private boolean showUpDir = true;//是否显示返回上一级
    private boolean showHideDir = true;//是否显示隐藏的目录（以“.”开头）
    private int itemHeight = 40;// dp
    private Drawable homeIcon = null;
    private Drawable upIcon = null;
    private Drawable folderIcon = null;
    private Drawable fileIcon = null;

    public FileAdapter() {
        super();
    }

    public String getCurrentPath() {
        return currentPath;
    }

    public void setFileIcon(Drawable fileIcon) {
        this.fileIcon = fileIcon;
    }

    public void setFolderIcon(Drawable folderIcon) {
        this.folderIcon = folderIcon;
    }

    public void setHomeIcon(Drawable homeIcon) {
        this.homeIcon = homeIcon;
    }

    public void setUpIcon(Drawable upIcon) {
        this.upIcon = upIcon;
    }

    /**
     * 允许的扩展名
     */
    public void setAllowExtensions(String[] allowExtensions) {
        this.allowExtensions = allowExtensions;
    }

    /**
     * 是否仅仅读取目录
     */
    public void setOnlyListDir(boolean onlyListDir) {
        this.onlyListDir = onlyListDir;
    }

    public boolean isOnlyListDir() {
        return onlyListDir;
    }

    /**
     * 是否显示返回主目录
     */
    public void setShowHomeDir(boolean showHomeDir) {
        this.showHomeDir = showHomeDir;
    }

    public boolean isShowHomeDir() {
        return showHomeDir;
    }

    /**
     * 是否显示返回上一级
     */
    public void setShowUpDir(boolean showUpDir) {
        this.showUpDir = showUpDir;
    }

    public boolean isShowUpDir() {
        return showUpDir;
    }

    /**
     * 是否显示隐藏的目录（以“.”开头）
     */
    public void setShowHideDir(boolean showHideDir) {
        this.showHideDir = showHideDir;
    }

    public boolean isShowHideDir() {
        return showHideDir;
    }

    public void setItemHeight(int itemHeight) {
        this.itemHeight = itemHeight;
    }

    public void loadData(String path) {
        if (path == null) {
            LogUtils.warn("current directory is null");
            return;
        }
        if (homeIcon == null) {
            homeIcon = ConvertUtils.toDrawable(FilePickerIcon.getHOME());
        }
        if (upIcon == null) {
            upIcon = ConvertUtils.toDrawable(FilePickerIcon.getUPDIR());
        }
        if (folderIcon == null) {
            folderIcon = ConvertUtils.toDrawable(FilePickerIcon.getFOLDER());
        }
        if (fileIcon == null) {
            fileIcon = ConvertUtils.toDrawable(FilePickerIcon.getFILE());
        }
        ArrayList<FileItem> datas = new ArrayList<FileItem>();
        if (rootPath == null) {
            rootPath = path;
        }
        LogUtils.verbose("current directory path: " + path);
        currentPath = path;
        if (showHomeDir) {
            //添加“返回主目录”
            FileItem fileRoot = new FileItem();
            fileRoot.setDirectory(true);
            fileRoot.setIcon(homeIcon);
            fileRoot.setName(DIR_ROOT);
            fileRoot.setSize(0);
            fileRoot.setPath(rootPath);
            datas.add(fileRoot);
        }
        if (showUpDir && !path.equals("/")) {
            //添加“返回上一级目录”
            FileItem fileParent = new FileItem();
            fileParent.setDirectory(true);
            fileParent.setIcon(upIcon);
            fileParent.setName(DIR_PARENT);
            fileParent.setSize(0);
            fileParent.setPath(new File(path).getParent());
            datas.add(fileParent);
        }
        File[] files;
        if (allowExtensions == null) {
            if (onlyListDir) {
                files = FileUtils.listDirs(currentPath);
            } else {
                files = FileUtils.listDirsAndFiles(currentPath);
            }
        } else {
            if (onlyListDir) {
                files = FileUtils.listDirs(currentPath, allowExtensions);
            } else {
                files = FileUtils.listDirsAndFiles(currentPath, allowExtensions);
            }
        }
        if (files != null) {
            for (File file : files) {
                if (!showHideDir && file.getName().startsWith(".")) {
                    continue;
                }
                FileItem fileItem = new FileItem();
                boolean isDirectory = file.isDirectory();
                fileItem.setDirectory(isDirectory);
                if (isDirectory) {
                    fileItem.setIcon(folderIcon);
                    fileItem.setSize(0);
                } else {
                    fileItem.setIcon(fileIcon);
                    fileItem.setSize(file.length());
                }
                fileItem.setName(file.getName());
                fileItem.setPath(file.getAbsolutePath());
                datas.add(fileItem);
            }
        }
        data.clear();
        data.addAll(datas);
        notifyDataSetChanged();
    }

    public void recycleData() {
        data.clear();
        if (homeIcon instanceof BitmapDrawable) {
            Bitmap homeBitmap = ((BitmapDrawable) homeIcon).getBitmap();
            if (null != homeBitmap && !homeBitmap.isRecycled()) {
                homeBitmap.recycle();
            }
        }
        if (upIcon instanceof BitmapDrawable) {
            Bitmap upBitmap = ((BitmapDrawable) upIcon).getBitmap();
            if (null != upBitmap && !upBitmap.isRecycled()) {
                upBitmap.recycle();
            }
        }
        if (folderIcon instanceof BitmapDrawable) {
            Bitmap folderBitmap = ((BitmapDrawable) folderIcon).getBitmap();
            if (null != folderBitmap && !folderBitmap.isRecycled()) {
                folderBitmap.recycle();
            }
        }
        if (fileIcon instanceof BitmapDrawable) {
            Bitmap fileBitmap = ((BitmapDrawable) fileIcon).getBitmap();
            if (null != fileBitmap && !fileBitmap.isRecycled()) {
                fileBitmap.recycle();
            }
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public FileItem getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        ViewHolder holder;
        if (convertView == null) {
            LinearLayout layout = new LinearLayout(context);
            CompatUtils.setBackground(layout, new StateColorDrawable(Color.WHITE, Color.LTGRAY));
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setGravity(Gravity.CENTER_VERTICAL);
            int height = ConvertUtils.toPx(context, itemHeight);
            int matchParent = ViewGroup.LayoutParams.MATCH_PARENT;
            // fixed: 17-1-8 #79 安卓4.x兼容问题，java.lang.ClassCastException……onMeasure……
            if (parent instanceof AbsListView) {
                layout.setLayoutParams(new AbsListView.LayoutParams(matchParent, height));
            } else {
                layout.setLayoutParams(new ViewGroup.LayoutParams(matchParent, height));
            }
            int padding = ConvertUtils.toPx(context, 5);
            layout.setPadding(padding, padding, padding, padding);

            ImageView imageView = new ImageView(context);
            int wh = ConvertUtils.toPx(context, 30);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(wh, wh));
            imageView.setImageResource(android.R.drawable.ic_menu_report_image);
            layout.addView(imageView);

            TextView textView = new TextView(context);
            LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(matchParent, matchParent);
            tvParams.leftMargin = ConvertUtils.toPx(context, 10);
            textView.setLayoutParams(tvParams);
            textView.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            textView.setSingleLine();
            layout.addView(textView);

            convertView = layout;
            holder = new ViewHolder();
            holder.imageView = imageView;
            holder.textView = textView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        FileItem item = data.get(position);
        holder.imageView.setImageDrawable(item.getIcon());
        holder.textView.setText(item.getName());
        return convertView;
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView textView;
    }

}
