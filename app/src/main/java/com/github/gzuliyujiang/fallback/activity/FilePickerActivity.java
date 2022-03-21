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

package com.github.gzuliyujiang.fallback.activity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.gzuliyujiang.fallback.R;
import com.github.gzuliyujiang.filepicker.ExplorerConfig;
import com.github.gzuliyujiang.filepicker.FileExplorer;
import com.github.gzuliyujiang.filepicker.FilePicker;
import com.github.gzuliyujiang.filepicker.annotation.ExplorerMode;
import com.github.gzuliyujiang.filepicker.contract.OnFilePickedListener;

import java.io.File;

/**
 * 文件/目录选择器
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2021/6/10 19:11
 */
public class FilePickerActivity extends BackAbleActivity implements OnFilePickedListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker_file);
        ExplorerConfig config = new ExplorerConfig(this);
        config.setRootDir(Environment.getExternalStorageDirectory());
        config.setLoadAsync(true);
        config.setExplorerMode(ExplorerMode.FILE);
        config.setShowHomeDir(false);
        config.setShowUpDir(false);
        config.setShowHideDir(true);
        FileExplorer fileExplorer = findViewById(R.id.file_picker_explorer);
        fileExplorer.load(config);
    }

    @Override
    public void onFilePicked(@NonNull File file) {
        Toast.makeText(getApplicationContext(), file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
    }

    public void onFilePick(View view) {
        ExplorerConfig config = new ExplorerConfig(this);
        config.setRootDir(getExternalFilesDir(null));
        config.setLoadAsync(false);
        config.setExplorerMode(ExplorerMode.FILE);
        config.setOnFilePickedListener(this);
        FilePicker picker = new FilePicker(this);
        picker.setExplorerConfig(config);
        picker.show();
    }

    public void onDirPick(View view) {
        ExplorerConfig config = new ExplorerConfig(this);
        config.setRootDir(getFilesDir());
        config.setLoadAsync(false);
        config.setExplorerMode(ExplorerMode.DIRECTORY);
        config.setOnFilePickedListener(this);
        FilePicker picker = new FilePicker(this);
        picker.setExplorerConfig(config);
        picker.show();
    }

    public void onDialogPick(View view) {
        new FileExplorerFragment().show(getSupportFragmentManager(), getClass().getName());
    }

}
