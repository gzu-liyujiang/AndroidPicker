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

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.gzuliyujiang.dialog.DialogLog;
import com.github.gzuliyujiang.filepicker.R;
import com.github.gzuliyujiang.filepicker.annotation.FileSort;
import com.github.gzuliyujiang.filepicker.contract.OnFileLoadedListener;
import com.github.gzuliyujiang.filepicker.contract.OnPathClickedListener;
import com.github.gzuliyujiang.filepicker.filter.SimpleFilter;
import com.github.gzuliyujiang.filepicker.sort.SortByExtension;
import com.github.gzuliyujiang.filepicker.sort.SortByName;
import com.github.gzuliyujiang.filepicker.sort.SortBySize;
import com.github.gzuliyujiang.filepicker.sort.SortByTime;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * 文件目录数据适配
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2014/05/23 18:02
 */
@SuppressWarnings("unused")
public class FileAdapter extends RecyclerView.Adapter<ViewHolder> {
    private static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();
    private static final Handler UI_HANDLER = new Handler(Looper.getMainLooper());
    private final Context context;
    public static final String DIR_ROOT = ".";
    public static final String DIR_PARENT = "..";
    private final List<FileEntity> data = new ArrayList<>();
    private File rootDir = null;
    private File currentFile = null;
    private String[] allowExtensions = null;
    private boolean loadAsync = true;
    private final LinkedList<FutureTask<?>> futureTasks = new LinkedList<>();
    private boolean onlyListDir = false;
    private boolean showHomeDir = true;
    private boolean showUpDir = true;
    private boolean showHideDir = true;
    private int fileSort = FileSort.BY_NAME_ASC;
    private int itemHeight = 40;
    private Drawable homeIcon;
    private Drawable upIcon;
    private Drawable folderIcon;
    private Drawable fileIcon;
    private OnFileLoadedListener onFileLoadedListener;
    private OnPathClickedListener onPathClickedListener;

    public FileAdapter(@NonNull Context context) {
        this.context = context;
        homeIcon = ContextCompat.getDrawable(context, R.mipmap.file_picker_home);
        upIcon = ContextCompat.getDrawable(context, R.mipmap.file_picker_up);
        folderIcon = ContextCompat.getDrawable(context, R.mipmap.file_picker_folder);
        fileIcon = ContextCompat.getDrawable(context, R.mipmap.file_picker_file);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER_VERTICAL);
        int height = (int) (itemHeight * context.getResources().getDisplayMetrics().density);
        int matchParent = ViewGroup.LayoutParams.MATCH_PARENT;
        layout.setLayoutParams(new ViewGroup.LayoutParams(matchParent, height));
        int padding = (int) (5 * context.getResources().getDisplayMetrics().density);
        layout.setPadding(padding, padding, padding, padding);
        ImageView imageView = new ImageView(context);
        int size = (int) (20 * context.getResources().getDisplayMetrics().density);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(size, size));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageResource(android.R.drawable.ic_menu_report_image);
        layout.addView(imageView);
        TextView textView = new TextView(context);
        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(matchParent, matchParent);
        tvParams.leftMargin = (int) (10 * context.getResources().getDisplayMetrics().density);
        textView.setLayoutParams(tvParams);
        textView.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        textView.setSingleLine();
        layout.addView(textView);
        ViewHolder viewHolder = new ViewHolder(layout);
        viewHolder.textView = textView;
        viewHolder.imageView = imageView;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final int adapterPosition = holder.getAdapterPosition();
        final FileEntity item = getItem(adapterPosition);
        holder.imageView.setImageDrawable(item.getIcon());
        holder.textView.setText(item.getName());
        if (onPathClickedListener == null) {
            return;
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPathClickedListener.onPathClicked(FileAdapter.this, adapterPosition, item.getFile().getAbsolutePath());
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setItemHeight(@Dimension(unit = Dimension.DP) int itemHeight) {
        this.itemHeight = itemHeight;
    }

    public void setFileIcon(Drawable fileIcon) {
        if (fileIcon == null) {
            return;
        }
        this.fileIcon = fileIcon;
    }

    public void setFolderIcon(Drawable folderIcon) {
        if (folderIcon == null) {
            return;
        }
        this.folderIcon = folderIcon;
    }

    public void setHomeIcon(Drawable homeIcon) {
        if (homeIcon == null) {
            return;
        }
        this.homeIcon = homeIcon;
    }

    public void setUpIcon(Drawable upIcon) {
        if (upIcon == null) {
            return;
        }
        this.upIcon = upIcon;
    }

    /**
     * 允许的扩展名
     */
    public void setAllowExtensions(String[] allowExtensions) {
        if (this.allowExtensions != null && Arrays.equals(this.allowExtensions, allowExtensions)) {
            return;
        }
        this.allowExtensions = allowExtensions;
    }

    /**
     * 是否异步加载文件夹或文件
     */
    public void setLoadAsync(boolean loadAsync) {
        if (this.loadAsync == loadAsync) {
            return;
        }
        this.loadAsync = loadAsync;
    }

    public boolean isLoadAsync() {
        return loadAsync;
    }

    /**
     * 是否仅仅读取目录
     */
    public void setOnlyListDir(boolean onlyListDir) {
        if (this.onlyListDir == onlyListDir) {
            return;
        }
        this.onlyListDir = onlyListDir;
    }

    public boolean isOnlyListDir() {
        return onlyListDir;
    }

    /**
     * 是否显示返回主目录
     */
    public void setShowHomeDir(boolean showHomeDir) {
        if (this.showHomeDir == showHomeDir) {
            return;
        }
        this.showHomeDir = showHomeDir;
    }

    public boolean isShowHomeDir() {
        return showHomeDir;
    }

    /**
     * 是否显示返回上一级
     */
    public void setShowUpDir(boolean showUpDir) {
        if (this.showUpDir == showUpDir) {
            return;
        }
        this.showUpDir = showUpDir;
    }

    public boolean isShowUpDir() {
        return showUpDir;
    }

    /**
     * 是否显示隐藏的目录（以“.”开头）
     */
    public void setShowHideDir(boolean showHideDir) {
        if (this.showHideDir == showHideDir) {
            return;
        }
        this.showHideDir = showHideDir;
    }

    public boolean isShowHideDir() {
        return showHideDir;
    }

    @FileSort
    public int getFileSort() {
        return fileSort;
    }

    public void setFileSort(@FileSort int fileSort) {
        if (this.fileSort == fileSort) {
            return;
        }
        this.fileSort = fileSort;
    }

    public File getRootDir() {
        return rootDir;
    }

    public File getCurrentFile() {
        return currentFile;
    }

    public void refreshData() {
        loadData(currentFile);
    }

    public void loadData(final File dir) {
        if (!loadAsync) {
            reallyRefresh(loadDataSync(dir));
            return;
        }
        if (!futureTasks.isEmpty()) {
            FutureTask<?> futureTask = futureTasks.getFirst();
            if (futureTask != null && !futureTask.isDone()) {
                futureTask.cancel(true);
            }
        }
        FutureTask<?> futureTask = new FutureTask<>(new Callable<Void>() {
            @Override
            public Void call() {
                final List<FileEntity> temp = loadDataSync(dir);
                if (!futureTasks.isEmpty()) {
                    FutureTask<?> futureTask = futureTasks.removeFirst();
                    if (futureTask != null && futureTask.isCancelled()) {
                        DialogLog.print("data load is canceled: " + currentFile);
                        return null;
                    }
                }
                UI_HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        reallyRefresh(temp);
                    }
                });
                return null;
            }
        });
        futureTasks.addLast(futureTask);
        THREAD_POOL.execute(futureTask);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void reallyRefresh(List<FileEntity> temp) {
        data.clear();
        data.addAll(temp);
        notifyDataSetChanged();
        if (onFileLoadedListener != null) {
            onFileLoadedListener.onFileLoaded(currentFile);
        }
        DialogLog.print("notify changed when data loaded: " + currentFile);
    }

    private List<FileEntity> loadDataSync(File dir) {
        if (dir == null) {
            DialogLog.print("current directory is null");
            return new ArrayList<>();
        }
        long millis = System.currentTimeMillis();
        List<FileEntity> entities = new ArrayList<>();
        if (rootDir == null) {
            rootDir = dir;
        }
        DialogLog.print("current directory path: " + dir);
        currentFile = dir;
        if (showHomeDir) {
            //添加“返回主目录”
            FileEntity root = new FileEntity();
            root.setIcon(homeIcon);
            root.setName(DIR_ROOT);
            root.setFile(rootDir);
            entities.add(root);
        }
        if (showUpDir && !File.separator.equals(dir.getAbsolutePath())) {
            //添加“返回上一级目录”
            FileEntity parent = new FileEntity();
            parent.setIcon(upIcon);
            parent.setName(DIR_PARENT);
            parent.setFile(dir.getParentFile());
            entities.add(parent);
        }
        List<File> files = listFiles(currentFile, new SimpleFilter(onlyListDir, allowExtensions));
        sortFiles(files, fileSort);
        for (File file : files) {
            if (!showHideDir && file.getName().startsWith(".")) {
                continue;
            }
            FileEntity FileEntity = new FileEntity();
            if (file.isDirectory()) {
                FileEntity.setIcon(folderIcon);
            } else {
                FileEntity.setIcon(fileIcon);
            }
            FileEntity.setName(file.getName());
            FileEntity.setFile(file);
            entities.add(FileEntity);
        }
        long spent = System.currentTimeMillis() - millis;
        DialogLog.print("spent: " + spent + " ms" + ", async=" + loadAsync + ", thread=" + Thread.currentThread());
        return entities;
    }

    public final void recycleData() {
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

    public void setOnFileLoadedListener(OnFileLoadedListener listener) {
        onFileLoadedListener = listener;
    }

    public void setOnPathClickedListener(OnPathClickedListener listener) {
        onPathClickedListener = listener;
    }

    public FileEntity getItem(int position) {
        return data.get(position);
    }

    /**
     * 列出指定目录下的所有子目录
     */
    private List<File> listFiles(File startDir, FileFilter fileFilter) {
        DialogLog.print(String.format("list dir %s", startDir));
        if (!startDir.isDirectory()) {
            return new ArrayList<>();
        }
        File[] dirs = startDir.listFiles(fileFilter);
        if (dirs == null) {
            return new ArrayList<>();
        }
        return Arrays.asList(dirs);
    }

    private void sortFiles(List<File> files, @FileSort int sort) {
        switch (sort) {
            case FileSort.BY_NAME_ASC:
                Collections.sort(files, new SortByName());
                break;
            case FileSort.BY_NAME_DESC:
                Collections.sort(files, new SortByName());
                Collections.reverse(files);
                break;
            case FileSort.BY_TIME_ASC:
                Collections.sort(files, new SortByTime());
                break;
            case FileSort.BY_TIME_DESC:
                Collections.sort(files, new SortByTime());
                Collections.reverse(files);
                break;
            case FileSort.BY_SIZE_ASC:
                Collections.sort(files, new SortBySize());
                break;
            case FileSort.BY_SIZE_DESC:
                Collections.sort(files, new SortBySize());
                Collections.reverse(files);
                break;
            case FileSort.BY_EXTENSION_ASC:
                Collections.sort(files, new SortByExtension());
                break;
            case FileSort.BY_EXTENSION_DESC:
                Collections.sort(files, new SortByExtension());
                Collections.reverse(files);
                break;
        }
    }

}
