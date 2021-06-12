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

package com.github.gzuliyujiang.fallback.custom;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.github.gzuliyujiang.wheelpicker.contract.AddressLoader;
import com.github.gzuliyujiang.wheelpicker.contract.AddressParser;
import com.github.gzuliyujiang.wheelpicker.contract.AddressReceiver;
import com.github.gzuliyujiang.wheelpicker.entity.ProvinceEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2021/6/7 16:33
 */
public class TextAddressLoader implements AddressLoader {
    private final Context context;

    public TextAddressLoader(Context context) {
        this.context = context;
    }

    @Override
    public void loadJson(@NonNull final AddressReceiver receiver, @NonNull final AddressParser parser) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                StringBuilder sb = new StringBuilder();
                AssetManager am = context.getAssets();
                try (BufferedReader bf = new BufferedReader(new InputStreamReader(am.open("city.txt")))) {
                    String line;
                    while ((line = bf.readLine()) != null) {
                        sb.append(line);
                    }
                } catch (IOException ignore) {
                }
                String json = sb.toString();
                try {
                    final List<ProvinceEntity> data = parser.parseData(json);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            receiver.onAddressReceived(data);
                        }
                    });
                } catch (Exception ignore) {
                }
            }
        });
    }

}
