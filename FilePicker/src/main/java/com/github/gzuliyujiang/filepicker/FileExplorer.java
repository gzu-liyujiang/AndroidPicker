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

package com.github.gzuliyujiang.filepicker;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.github.gzuliyujiang.dialog.DialogLog;
import com.github.gzuliyujiang.filepicker.adapter.FileAdapter;
import com.github.gzuliyujiang.filepicker.adapter.FileEntity;
import com.github.gzuliyujiang.filepicker.adapter.PathAdapter;
import com.github.gzuliyujiang.filepicker.adapter.ViewHolder;
import com.github.gzuliyujiang.filepicker.contract.OnFileLoadedListener;
import com.github.gzuliyujiang.filepicker.contract.OnPathClickedListener;

import java.io.File;
import java.util.Locale;

/**
 * 文件浏览器
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2021/6/10 18:50
 */
@SuppressWarnings("unused")
public class FileExplorer extends FrameLayout implements OnFileLoadedListener, OnPathClickedListener {
    private CharSequence emptyHint;
    private FileAdapter fileAdapter;
    private PathAdapter pathAdapter;
    private ProgressBar loadingView;
    private RecyclerView fileListView;
    private TextView emptyHintView;
    private RecyclerView pathListView;
    private ExplorerConfig explorerConfig;

    public FileExplorer(Context context) {
        super(context);
        init(context);
    }

    public FileExplorer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FileExplorer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FileExplorer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        explorerConfig = new ExplorerConfig(context);
        View contentView = inflate(context, R.layout.file_picker_content, this);
        pathListView = contentView.findViewById(R.id.file_picker_path_list);
        loadingView = contentView.findViewById(R.id.file_picker_loading);
        fileListView = contentView.findViewById(R.id.file_picker_file_list);
        emptyHintView = contentView.findViewById(R.id.file_picker_empty_hint);
        pathAdapter = new PathAdapter(context);
        pathAdapter.setOnPathClickedListener(this);
        pathListView.setAdapter(pathAdapter);
        fileAdapter = new FileAdapter(explorerConfig);
        fileListView.setAdapter(fileAdapter);
        emptyHint = Locale.getDefault().getDisplayLanguage().contains("中文") ? "<空>" : "<Empty>";
        emptyHintView.setText(emptyHint);
    }

    public void load() {
        load(null);
    }

    public void load(@Nullable ExplorerConfig config) {
        if (config != null) {
            explorerConfig = config;
            fileAdapter = new FileAdapter(config);
            fileListView.setAdapter(fileAdapter);
        }
        explorerConfig.setOnFileLoadedListener(this);
        explorerConfig.setOnPathClickedListener(this);
        refreshCurrent(explorerConfig.getRootDir());
    }

    @Override
    public void onFileLoaded(@NonNull File file) {
        loadingView.setVisibility(INVISIBLE);
        fileListView.setVisibility(View.VISIBLE);
        int itemCount = fileAdapter.getItemCount();
        if (explorerConfig.isShowHomeDir()) {
            itemCount--;
        }
        if (explorerConfig.isShowUpDir()) {
            itemCount--;
        }
        if (itemCount < 1) {
            DialogLog.print("no files, or dir is empty");
            emptyHintView.setVisibility(View.VISIBLE);
            emptyHintView.setText(emptyHint);
        } else {
            DialogLog.print("files or dirs count: " + itemCount);
            emptyHintView.setVisibility(View.INVISIBLE);
        }
        pathListView.post(new Runnable() {
            @Override
            public void run() {
                pathListView.scrollToPosition(pathAdapter.getItemCount() - 1);
            }
        });
    }

    @Override
    public void onPathClicked(RecyclerView.Adapter<ViewHolder> adapter, int position, @NonNull String path) {
        DialogLog.print("clicked path name: " + path);
        if (adapter instanceof PathAdapter) {
            refreshCurrent(new File(path));
        } else if (adapter instanceof FileAdapter) {
            FileEntity entity = fileAdapter.getItem(position);
            DialogLog.print("clicked file item: " + entity);
            File file = entity.getFile();
            if (file.isDirectory()) {
                refreshCurrent(file);
                return;
            }
            if (explorerConfig.getOnFileClickedListener() != null) {
                explorerConfig.getOnFileClickedListener().onFileClicked(file);
            }
        }
    }

    public void setEmptyHint(@NonNull CharSequence emptyHint) {
        if (TextUtils.equals(this.emptyHint, emptyHint)) {
            return;
        }
        this.emptyHint = emptyHint;
        emptyHintView.setText(emptyHint);
    }

    public final void refreshCurrent(File current) {
        if (current == null) {
            DialogLog.print("current dir is null");
            return;
        }
        loadingView.setVisibility(VISIBLE);
        fileListView.setVisibility(View.INVISIBLE);
        emptyHintView.setVisibility(View.INVISIBLE);
        long millis = System.currentTimeMillis();
        pathAdapter.updatePath(current);
        fileAdapter.loadData(current);
        long spent = System.currentTimeMillis() - millis;
        DialogLog.print("spent: " + spent + " ms" + ", async=" + explorerConfig.isLoadAsync() + ", thread=" + Thread.currentThread());
    }

    public final FileAdapter getFileAdapter() {
        return fileAdapter;
    }

    public final PathAdapter getPathAdapter() {
        return pathAdapter;
    }

    public ExplorerConfig getExplorerConfig() {
        return explorerConfig;
    }

    public final File getRootDir() {
        return explorerConfig.getRootDir();
    }

    public final File getCurrentFile() {
        return fileAdapter.getCurrentFile();
    }

    public final RecyclerView getPathListView() {
        return pathListView;
    }

    public final RecyclerView getFileListView() {
        return fileListView;
    }

    public final TextView getEmptyHintView() {
        return emptyHintView;
    }

}
