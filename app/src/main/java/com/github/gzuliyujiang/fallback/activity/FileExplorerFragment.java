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

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.github.gzuliyujiang.fallback.R;
import com.github.gzuliyujiang.filepicker.ExplorerConfig;
import com.github.gzuliyujiang.filepicker.FileExplorer;
import com.github.gzuliyujiang.filepicker.annotation.ExplorerMode;

import java.io.File;
import java.util.Objects;

/**
 * @author :Reginer  2022/3/16 16:19.
 * 联系方式:QQ:282921012
 * 功能描述:文件浏览器弹窗
 */
public class FileExplorerFragment extends DialogFragment {

    public FileExplorerFragment() {
    }

    @Override
    @SuppressLint("PrivateResource")
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.Theme_Design_Light_BottomSheetDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(getDialog()).getWindow().getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes(layoutParams);
        return inflater.inflate(R.layout.fragment_file_explorer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ExplorerConfig config = new ExplorerConfig(requireContext());
        config.setRootDir(new File("sdcard"));
        config.setLoadAsync(true);
        config.setExplorerMode(ExplorerMode.FILE);
        config.setShowHomeDir(false);
        config.setShowUpDir(false);
        config.setShowHideDir(false);
        FileExplorer fileExplorer = view.findViewById(R.id.fileExplorer);
        fileExplorer.load(config);
    }

}