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

package com.github.gzuliyujiang.filepicker.filter;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;

/**
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2021/6/10 15:07
 */
public class PatternFilter implements FileFilter {
    private final Pattern pattern;

    public PatternFilter(@NonNull Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean accept(File pathname) {
        if (pathname == null) {
            return false;
        }
        return pattern.matcher(pathname.getName()).find();
    }

}
