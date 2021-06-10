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
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.gzuliyujiang.basepicker.PickerLog;
import com.github.gzuliyujiang.filepicker.adapter.FileAdapter;
import com.github.gzuliyujiang.filepicker.adapter.FileEntity;
import com.github.gzuliyujiang.filepicker.adapter.PathAdapter;
import com.github.gzuliyujiang.filepicker.annotation.ExplorerMode;
import com.github.gzuliyujiang.filepicker.contract.OnFileClickedListener;
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
public class FileExplorer extends FrameLayout {
    private int explorerMode = ExplorerMode.FILE;
    private File initDir;
    private CharSequence emptyHint;
    private FileAdapter fileAdapter;
    private PathAdapter pathAdapter;
    private RecyclerView fileListView;
    private TextView emptyHintView;
    private View bottomLineView;
    private RecyclerView pathListView;
    private OnFileClickedListener onFileClickedListener;

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

    public FileExplorer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View contentView = inflate(context, R.layout.file_picker_content, this);
        fileListView = contentView.findViewById(R.id.file_picker_file_list);
        fileAdapter = new FileAdapter(context);
        fileListView.setAdapter(fileAdapter);
        emptyHint = Locale.getDefault().getDisplayLanguage().contains("中文") ? "<空>" : "<Empty>";
        emptyHintView = contentView.findViewById(R.id.file_picker_empty_hint);
        emptyHintView.setText(emptyHint);
        bottomLineView = contentView.findViewById(R.id.file_picker_bottom_line);
        pathAdapter = new PathAdapter(context);
        pathListView = contentView.findViewById(R.id.file_picker_path_list);
        pathListView.setAdapter(pathAdapter);
        fileAdapter.setOnlyListDir(false);
        fileAdapter.setShowHideDir(true);
        fileAdapter.setShowHomeDir(true);
        fileAdapter.setShowUpDir(true);
        initDir = getDefaultDir();
        fileAdapter.loadData(initDir);
        fileAdapter.setOnPathClickedListener(new OnPathClickedListener() {
            @Override
            public void onPathClicked(int position, @NonNull String path) {
                FileEntity entity = fileAdapter.getItem(position);
                PickerLog.print("clicked file item: " + entity);
                File file = entity.getFile();
                if (file.isDirectory()) {
                    refreshCurrent(file);
                    return;
                }
                if (onFileClickedListener != null) {
                    onFileClickedListener.onFileClicked(file);
                }
            }
        });
        pathAdapter.setOnPathClickedListener(new OnPathClickedListener() {
            @Override
            public void onPathClicked(int position, @NonNull String path) {
                PickerLog.print("clicked path name: " + path);
                refreshCurrent(new File(path));
            }
        });
    }

    public final File getDefaultDir() {
        if (initDir != null) {
            return initDir;
        }
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return getContext().getExternalFilesDir(null);
        } else {
            return getContext().getFilesDir();
        }
    }

    public void setInitDir(@ExplorerMode int explorerMode, File initDir) {
        if (this.explorerMode != explorerMode) {
            this.explorerMode = explorerMode;
            fileAdapter.setOnlyListDir(explorerMode == ExplorerMode.DIRECTORY);
        }
        if (initDir == null) {
            initDir = getDefaultDir();
        }
        this.initDir = initDir;
        refreshCurrent(initDir);
    }

    @ExplorerMode
    public final int getExplorerMode() {
        return explorerMode;
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
            return;
        }
        pathAdapter.updatePath(current);
        fileAdapter.loadData(current);
        int itemCount = fileAdapter.getItemCount();
        if (fileAdapter.isShowHomeDir()) {
            itemCount--;
        }
        if (fileAdapter.isShowUpDir()) {
            itemCount--;
        }
        if (itemCount < 1) {
            PickerLog.print("no files, or dir is empty");
            emptyHintView.setVisibility(View.VISIBLE);
            emptyHintView.setText(emptyHint);
        } else {
            PickerLog.print("files or dirs count: " + itemCount);
            emptyHintView.setVisibility(View.GONE);
        }
    }

    public void setOnFileClickedListener(OnFileClickedListener listener) {
        this.onFileClickedListener = listener;
    }

    public void setAllowExtensions(String[] allowExtensions) {
        fileAdapter.setAllowExtensions(allowExtensions);
    }

    public void setShowUpDir(boolean showUpDir) {
        fileAdapter.setShowUpDir(showUpDir);
    }

    public void setShowHomeDir(boolean showHomeDir) {
        fileAdapter.setShowHomeDir(showHomeDir);
    }

    public void setShowHideDir(boolean showHideDir) {
        fileAdapter.setShowHideDir(showHideDir);
    }

    public void setFileIcon(Drawable fileIcon) {
        fileAdapter.setFileIcon(fileIcon);
    }

    public void setFolderIcon(Drawable folderIcon) {
        fileAdapter.setFolderIcon(folderIcon);
    }

    public void setHomeIcon(Drawable homeIcon) {
        fileAdapter.setHomeIcon(homeIcon);
    }

    public void setUpIcon(Drawable upIcon) {
        fileAdapter.setUpIcon(upIcon);
    }

    public void setArrowIcon(Drawable arrowIcon) {
        pathAdapter.setArrowIcon(arrowIcon);
    }

    public void setItemHeight(int itemHeight) {
        fileAdapter.setItemHeight(itemHeight);
    }

    public final FileAdapter getFileAdapter() {
        return fileAdapter;
    }

    public final PathAdapter getPathAdapter() {
        return pathAdapter;
    }

    public final File getCurrentFile() {
        return fileAdapter.getCurrentFile();
    }

    public final RecyclerView getFileListView() {
        return fileListView;
    }

    public final TextView getEmptyHintView() {
        return emptyHintView;
    }

    public final View getBottomLineView() {
        return bottomLineView;
    }

    public final RecyclerView getPathListView() {
        return pathListView;
    }

}
