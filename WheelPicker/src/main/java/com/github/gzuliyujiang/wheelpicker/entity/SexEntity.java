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

package com.github.gzuliyujiang.wheelpicker.entity;

import androidx.annotation.NonNull;

import com.github.gzuliyujiang.wheelview.contract.TextProvider;

import java.io.Serializable;
import java.util.Locale;
import java.util.Objects;

/**
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2021/10/28 9:17
 */
public class SexEntity implements TextProvider, Serializable {
    private static final boolean IS_CHINESE;
    private String id;
    private String name;
    private String english;

    static {
        IS_CHINESE = Locale.getDefault().getDisplayLanguage().contains("中文");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    @Override
    public String provideText() {
        if (IS_CHINESE) {
            return name;
        }
        return english;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SexEntity that = (SexEntity) o;
        return Objects.equals(id, that.id) ||
                Objects.equals(name, that.name) ||
                Objects.equals(english, that.english);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, english);
    }

    @NonNull
    @Override
    public String toString() {
        return "SexEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", english" + english + '\'' +
                '}';
    }

}
