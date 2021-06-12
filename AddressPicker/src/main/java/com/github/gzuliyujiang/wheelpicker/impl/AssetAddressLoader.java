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

package com.github.gzuliyujiang.wheelpicker.impl;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import com.github.gzuliyujiang.wheelpicker.contract.AddressLoader;
import com.github.gzuliyujiang.wheelpicker.contract.AddressParser;
import com.github.gzuliyujiang.wheelpicker.contract.AddressReceiver;
import com.github.gzuliyujiang.wheelpicker.entity.ProvinceEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * 从APK包中的“assets”目录下加载地址数据
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2019/6/17 17:15
 */
@SuppressWarnings("unused")
public class AssetAddressLoader implements AddressLoader {
    private final Context context;
    private final String path;

    public AssetAddressLoader(@NonNull Context context, @NonNull String path) {
        this.context = context;
        this.path = path;
    }

    @Override
    public void loadJson(@NonNull final AddressReceiver receiver, @NonNull final AddressParser parser) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                String text = loadFromAssets();
                final List<ProvinceEntity> data;
                if (TextUtils.isEmpty(text)) {
                    data = new ArrayList<>();
                } else {
                    data = parser.parseData(text);
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        receiver.onAddressReceived(data);
                    }
                });
            }
        });
    }

    @WorkerThread
    private String loadFromAssets() {
        StringBuilder stringBuilder = new StringBuilder();
        AssetManager am = context.getAssets();
        try (BufferedReader bf = new BufferedReader(new InputStreamReader(am.open(path)))) {
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            return "";
        }
        return stringBuilder.toString();
    }

}
