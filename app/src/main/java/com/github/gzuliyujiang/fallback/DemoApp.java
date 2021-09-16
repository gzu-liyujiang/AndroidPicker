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

package com.github.gzuliyujiang.fallback;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.github.gzuliyujiang.dialog.DialogConfig;
import com.github.gzuliyujiang.dialog.DialogLog;
import com.github.gzuliyujiang.dialog.DialogStyle;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;

/**
 * 应用全局上下文
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2016/7/20 20:28
 */
public class DemoApp extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DialogLog.enable();
        DialogConfig.setDialogStyle(DialogStyle.Default);
        initXCrash(this);
    }

    private static void initXCrash(Application application) {
        try {
            // debugRuntimeOnly "com.iqiyi.xcrash:xcrash-android-lib:x.x.x"，调试运行才集成xCrash
            // compileOnly "com.iqiyi.xcrash:xcrash-android-lib:x.x.x"，编译模式才集成xCrash
            Class.forName("xcrash.XCrash");
            System.out.println("xCrash SDK dependency was found");
            File logDir = application.getExternalFilesDir("xcrash");
            if (logDir == null) {
                logDir = new File(application.getFilesDir(), "xcrash");
                //noinspection ResultOfMethodCallIgnored
                logDir.mkdir();
            }
            System.out.println("xCrash SDK init start: logDir=" + logDir);
            xcrash.ICrashCallback callback = new xcrash.ICrashCallback() {
                @Override
                public void onCrash(String logPath, String emergency) {
                    System.out.println("xCrash SDK: logPath=" + logPath + ", emergency=" + emergency);
                    if (logPath == null || emergency == null) {
                        return;
                    }
                    FileWriter writer = null;
                    try {
                        File file = new File(xcrash.XCrash.getLogDir(), "crash.json");
                        //noinspection ResultOfMethodCallIgnored
                        file.createNewFile();
                        writer = new FileWriter(file, false);
                        writer.write(new JSONObject(xcrash.TombstoneParser.parse(logPath, emergency)).toString());
                    } catch (Exception e) {
                        System.err.println("xCrash SDK failed to export the crash to a JSON file: " + e);
                    } finally {
                        if (writer != null) {
                            try {
                                writer.close();
                            } catch (Exception ignored) {
                            }
                        }
                    }
                }
            };
            xcrash.XCrash.init(application, new xcrash.XCrash.InitParameters()
                    .setJavaRethrow(true)
                    .setJavaLogCountMax(10)
                    .setJavaDumpAllThreadsWhiteList(new String[]{"^main$", "^Binder:.*", ".*Finalizer.*"})
                    .setJavaDumpAllThreadsCountMax(10)
                    .setJavaCallback(callback)
                    .setNativeRethrow(true)
                    .setNativeLogCountMax(10)
                    .setNativeDumpAllThreadsWhiteList(new String[]{"^xcrash\\.sample$", "^Signal Catcher$", "^Jit thread pool$", ".*(R|r)ender.*", ".*Chrome.*"})
                    .setNativeDumpAllThreadsCountMax(10)
                    .setNativeCallback(callback)
                    .setAnrRethrow(true)
                    .setAnrLogCountMax(10)
                    .setAnrCallback(callback)
                    .setPlaceholderCountMax(3)
                    .setPlaceholderSizeKb(512)
                    .setLogDir(logDir.getAbsolutePath())
                    .setLogFileMaintainDelayMs(1000));
            System.out.println("xCrash SDK init end");
        } catch (Exception e) {
            System.out.println("xCrash SDK dependency not found");
        }
    }

}
