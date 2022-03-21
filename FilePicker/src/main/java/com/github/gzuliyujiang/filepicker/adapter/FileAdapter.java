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
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.gzuliyujiang.dialog.DialogLog;
import com.github.gzuliyujiang.filepicker.ExplorerConfig;
import com.github.gzuliyujiang.filepicker.annotation.ExplorerMode;
import com.github.gzuliyujiang.filepicker.annotation.FileSort;
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
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
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
    public static final String DIR_ROOT = ".";
    public static final String DIR_PARENT = "..";
    private final List<FileEntity> data = new ArrayList<>();
    private File currentFile = null;
    private final ConcurrentLinkedQueue<FutureTask<?>> futureTasks = new ConcurrentLinkedQueue<>();
    private ExplorerConfig explorerConfig;

    public FileAdapter(ExplorerConfig explorerConfig) {
        this.explorerConfig = explorerConfig;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER_VERTICAL);
        int height = (int) (explorerConfig.getItemHeight() * context.getResources().getDisplayMetrics().density);
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
        if (explorerConfig.getOnPathClickedListener() == null) {
            return;
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                explorerConfig.getOnPathClickedListener().onPathClicked(FileAdapter.this, adapterPosition, item.getFile().getAbsolutePath());
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

    public void setExplorerConfig(ExplorerConfig explorerConfig) {
        this.explorerConfig = explorerConfig;
        loadData(currentFile);
    }

    public ExplorerConfig getExplorerConfig() {
        return explorerConfig;
    }

    public final void recycleData() {
        data.clear();
        if (explorerConfig.getHomeIcon() instanceof BitmapDrawable) {
            Bitmap homeBitmap = ((BitmapDrawable) explorerConfig.getHomeIcon()).getBitmap();
            if (null != homeBitmap && !homeBitmap.isRecycled()) {
                homeBitmap.recycle();
            }
        }
        if (explorerConfig.getUpIcon() instanceof BitmapDrawable) {
            Bitmap upBitmap = ((BitmapDrawable) explorerConfig.getUpIcon()).getBitmap();
            if (null != upBitmap && !upBitmap.isRecycled()) {
                upBitmap.recycle();
            }
        }
        if (explorerConfig.getFolderIcon() instanceof BitmapDrawable) {
            Bitmap folderBitmap = ((BitmapDrawable) explorerConfig.getFolderIcon()).getBitmap();
            if (null != folderBitmap && !folderBitmap.isRecycled()) {
                folderBitmap.recycle();
            }
        }
        if (explorerConfig.getFileIcon() instanceof BitmapDrawable) {
            Bitmap fileBitmap = ((BitmapDrawable) explorerConfig.getFileIcon()).getBitmap();
            if (null != fileBitmap && !fileBitmap.isRecycled()) {
                fileBitmap.recycle();
            }
        }
    }

    public File getCurrentFile() {
        return currentFile;
    }

    public FileEntity getItem(int position) {
        return data.get(position);
    }

    public void loadData(final File dir) {
        if (!explorerConfig.isLoadAsync()) {
            reallyRefresh(loadDataSync(dir));
            return;
        }
        FutureTask<?> lastTask = futureTasks.peek();
        if (lastTask != null && !lastTask.isDone()) {
            lastTask.cancel(true);
        }
        FutureTask<?> newTask = new FutureTask<>(new Callable<Void>() {
            @Override
            public Void call() {
                final List<FileEntity> temp = loadDataSync(dir);
                FutureTask<?> task = futureTasks.poll();
                if (task != null && task.isCancelled()) {
                    DialogLog.print("data load is canceled: " + currentFile);
                    return null;
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
        futureTasks.add(newTask);
        THREAD_POOL.execute(newTask);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void reallyRefresh(List<FileEntity> temp) {
        data.clear();
        data.addAll(temp);
        notifyDataSetChanged();
        if (explorerConfig.getOnFileLoadedListener() != null) {
            explorerConfig.getOnFileLoadedListener().onFileLoaded(currentFile);
        }
        DialogLog.print("notify changed when data loaded: " + currentFile);
    }

    private List<FileEntity> loadDataSync(File dir) {
        if (dir == null) {
            DialogLog.print("current directory is null");
            dir = explorerConfig.getRootDir();
        }
        long millis = System.currentTimeMillis();
        List<FileEntity> entities = new ArrayList<>();
        DialogLog.print("will load directory: " + dir);
        currentFile = dir;
        if (explorerConfig.isShowHomeDir()) {
            //添加“返回主目录”
            FileEntity root = new FileEntity();
            root.setIcon(explorerConfig.getHomeIcon());
            root.setName(DIR_ROOT);
            root.setFile(explorerConfig.getRootDir());
            entities.add(root);
        }
        if (explorerConfig.isShowUpDir() && !File.separator.equals(dir.getAbsolutePath())) {
            //添加“返回上一级目录”
            FileEntity parent = new FileEntity();
            parent.setIcon(explorerConfig.getUpIcon());
            parent.setName(DIR_PARENT);
            parent.setFile(dir.getParentFile());
            entities.add(parent);
        }
        SimpleFilter filter = new SimpleFilter(explorerConfig.getExplorerMode() == ExplorerMode.DIRECTORY, explorerConfig.getAllowExtensions());
        List<File> files = listFiles(currentFile, filter);
        sortFiles(files, explorerConfig.getFileSort());
        for (File file : files) {
            if (!explorerConfig.isShowHideDir() && file.getName().startsWith(".")) {
                continue;
            }
            FileEntity FileEntity = new FileEntity();
            if (file.isDirectory()) {
                FileEntity.setIcon(explorerConfig.getFolderIcon());
            } else {
                FileEntity.setIcon(explorerConfig.getFileIcon());
            }
            FileEntity.setName(file.getName());
            FileEntity.setFile(file);
            entities.add(FileEntity);
        }
        long spent = System.currentTimeMillis() - millis;
        DialogLog.print("spent: " + spent + " ms" + ", async=" + explorerConfig.isLoadAsync() + ", thread=" + Thread.currentThread());
        return entities;
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
