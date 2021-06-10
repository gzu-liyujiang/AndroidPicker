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

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.Serializable;

/**
 * 文件项信息
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2014/05/23 18:02
 */
public class FileEntity implements Serializable {
    private Drawable icon;
    private String name;
    private File file;

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public Drawable getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @NonNull
    @Override
    public String toString() {
        return "FileEntity{" +
                "name='" + name + '\'' +
                ", file='" + file + '\'' +
                '}';
    }

}
