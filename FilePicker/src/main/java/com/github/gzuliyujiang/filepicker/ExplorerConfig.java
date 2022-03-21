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

import androidx.annotation.Dimension;
import androidx.core.content.ContextCompat;

import com.github.gzuliyujiang.filepicker.annotation.ExplorerMode;
import com.github.gzuliyujiang.filepicker.annotation.FileSort;
import com.github.gzuliyujiang.filepicker.contract.OnFileClickedListener;
import com.github.gzuliyujiang.filepicker.contract.OnFileLoadedListener;
import com.github.gzuliyujiang.filepicker.contract.OnFilePickedListener;
import com.github.gzuliyujiang.filepicker.contract.OnPathClickedListener;

import java.io.File;
import java.io.Serializable;
import java.util.Arrays;

/**
 * @author liyujiang
 * @since 2022/3/16 19:57
 */
public class ExplorerConfig implements Serializable {
    private final Context context;
    private File rootDir;
    private boolean loadAsync;
    private String[] allowExtensions = null;
    private int explorerMode = ExplorerMode.DIRECTORY;
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
    private OnFileClickedListener onFileClickedListener;
    private OnFilePickedListener onFilePickedListener;

    public ExplorerConfig(Context context) {
        this.context = context;
        homeIcon = ContextCompat.getDrawable(context, R.mipmap.file_picker_home);
        upIcon = ContextCompat.getDrawable(context, R.mipmap.file_picker_up);
        folderIcon = ContextCompat.getDrawable(context, R.mipmap.file_picker_folder);
        fileIcon = ContextCompat.getDrawable(context, R.mipmap.file_picker_file);
    }

    public void setRootDir(File rootDir) {
        this.rootDir = rootDir;
    }

    public File getRootDir() {
        if (rootDir == null) {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                rootDir = Environment.getExternalStorageDirectory();
            } else {
                rootDir = context.getFilesDir();
            }
        }
        return rootDir;
    }

    public void setLoadAsync(boolean loadAsync) {
        this.loadAsync = loadAsync;
    }

    public boolean isLoadAsync() {
        return loadAsync;
    }

    public ExplorerConfig setItemHeight(@Dimension(unit = Dimension.DP) int itemHeight) {
        if (itemHeight < 10 || itemHeight > 100) {
            return this;
        }
        this.itemHeight = itemHeight;
        return this;
    }

    public int getItemHeight() {
        return itemHeight;
    }

    public ExplorerConfig setFileIcon(Drawable fileIcon) {
        if (fileIcon == null) {
            return this;
        }
        this.fileIcon = fileIcon;
        return this;
    }

    public Drawable getFileIcon() {
        return fileIcon;
    }

    public ExplorerConfig setFolderIcon(Drawable folderIcon) {
        if (folderIcon == null) {
            return this;
        }
        this.folderIcon = folderIcon;
        return this;
    }

    public Drawable getFolderIcon() {
        return folderIcon;
    }

    public ExplorerConfig setHomeIcon(Drawable homeIcon) {
        if (homeIcon == null) {
            return this;
        }
        this.homeIcon = homeIcon;
        return this;
    }

    public Drawable getHomeIcon() {
        return homeIcon;
    }

    public ExplorerConfig setUpIcon(Drawable upIcon) {
        if (upIcon == null) {
            return this;
        }
        this.upIcon = upIcon;
        return this;
    }

    public Drawable getUpIcon() {
        return upIcon;
    }

    /**
     * 允许的扩展名
     */
    public ExplorerConfig setAllowExtensions(String[] allowExtensions) {
        if (this.allowExtensions != null && Arrays.equals(this.allowExtensions, allowExtensions)) {
            return this;
        }
        this.allowExtensions = allowExtensions;
        return this;
    }

    public String[] getAllowExtensions() {
        return allowExtensions;
    }

    @ExplorerMode
    public int getExplorerMode() {
        return explorerMode;
    }

    public void setExplorerMode(@ExplorerMode int explorerMode) {
        this.explorerMode = explorerMode;
    }

    /**
     * 是否显示返回主目录
     */
    public ExplorerConfig setShowHomeDir(boolean showHomeDir) {
        if (this.showHomeDir == showHomeDir) {
            return this;
        }
        this.showHomeDir = showHomeDir;
        return this;
    }

    public boolean isShowHomeDir() {
        return showHomeDir;
    }

    /**
     * 是否显示返回上一级
     */
    public ExplorerConfig setShowUpDir(boolean showUpDir) {
        if (this.showUpDir == showUpDir) {
            return this;
        }
        this.showUpDir = showUpDir;
        return this;
    }

    public boolean isShowUpDir() {
        return showUpDir;
    }

    /**
     * 是否显示隐藏的目录（以“.”开头）
     */
    public ExplorerConfig setShowHideDir(boolean showHideDir) {
        if (this.showHideDir == showHideDir) {
            return this;
        }
        this.showHideDir = showHideDir;
        return this;
    }

    public boolean isShowHideDir() {
        return showHideDir;
    }

    public ExplorerConfig setFileSort(@FileSort int fileSort) {
        if (this.fileSort == fileSort) {
            return this;
        }
        this.fileSort = fileSort;
        return this;
    }

    @FileSort
    public int getFileSort() {
        return fileSort;
    }

    public ExplorerConfig setOnFileLoadedListener(OnFileLoadedListener listener) {
        onFileLoadedListener = listener;
        return this;
    }

    public OnFileLoadedListener getOnFileLoadedListener() {
        return onFileLoadedListener;
    }

    public ExplorerConfig setOnPathClickedListener(OnPathClickedListener listener) {
        onPathClickedListener = listener;
        return this;
    }

    public OnPathClickedListener getOnPathClickedListener() {
        return onPathClickedListener;
    }

    public OnFileClickedListener getOnFileClickedListener() {
        return onFileClickedListener;
    }

    public void setOnFileClickedListener(OnFileClickedListener onFileClickedListener) {
        this.onFileClickedListener = onFileClickedListener;
    }

    public OnFilePickedListener getOnFilePickedListener() {
        return onFilePickedListener;
    }

    public void setOnFilePickedListener(OnFilePickedListener onFilePickedListener) {
        this.onFilePickedListener = onFilePickedListener;
    }

}
