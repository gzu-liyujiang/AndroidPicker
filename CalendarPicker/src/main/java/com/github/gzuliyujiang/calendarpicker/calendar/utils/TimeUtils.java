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

package com.github.gzuliyujiang.calendarpicker.calendar.utils;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 时间工具
 */
public class TimeUtils {
    public final static String YY_M_CN = "yyyy年MM月";
    public final static String YY_MD = "yyyy-MM-dd";

    private final static Map<String, SimpleDateFormat> dateMap = new HashMap<>();

    private static void ensureDateFormatMap(@NonNull String format) {
        if (!dateMap.containsKey(format)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
                dateMap.put(format, sdf);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Date date(String dateText, @NonNull String format) throws Exception {
        ensureDateFormatMap(format);
        if (dateMap.containsKey(format)) {
            SimpleDateFormat sdf = dateMap.get(format);
            return sdf.parse(dateText);
        }
        return null;
    }

    public static String dateText(long date, @NonNull String format) {
        String value = "";
        ensureDateFormatMap(format);
        if (dateMap.containsKey(format)) {
            SimpleDateFormat sdf = dateMap.get(format);
            value = sdf.format(new Date(date));
        }
        return value;
    }

}
