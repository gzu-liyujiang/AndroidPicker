package cn.qqtheme.framework.logger.impl;

import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 突破LogCat字数限制
 * <p>
 * Adapted from https://github.com/fodroid/XDroid-Base/.../XPrinter.java
 *
 * @author wanglei
 * @date 2016/11/29
 * @date 2018/11/30 18:10
 * @date 2019/5/23 23:48
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
class LogPrinter {
    private static final int MAX_LENGTH_OF_SINGLE_MESSAGE = 2048;
    private static final int METHOD_COUNT_IN_TRACE = 2;

    @SuppressWarnings("ConstantConditions")
    static void println(int priority, String tag, @NonNull Object msg) {
        String formatMsg = LogFormatter.formatString(msg) + getTraceElement();
        formatMsg = LogFormatter.formatBorder(new String[]{formatMsg});
        int length = formatMsg.length();
        if (length <= MAX_LENGTH_OF_SINGLE_MESSAGE) {
            printChunk(priority, tag, formatMsg);
            return;
        }
        int start = 0;
        int end = start + MAX_LENGTH_OF_SINGLE_MESSAGE;
        while (start < length) {
            printChunk(priority, tag, formatMsg.substring(start, end));
            start = end;
            end = Math.min(start + MAX_LENGTH_OF_SINGLE_MESSAGE, length);
        }
    }

    private static void printChunk(int priority, String tag, String msg) {
        Log.println(priority, tag, msg);
    }

    /**
     * 可显示调用方法所在的文件行号，在 Android Studio 的 Logcat 处可点击定位。
     * 此方法参考 https://github.com/orhanobut/logger
     */
    private static String getTraceElement() {
        try {
            int methodCount = METHOD_COUNT_IN_TRACE + 1;
            StackTraceElement[] trace = Thread.currentThread().getStackTrace();
            int stackOffset = getStackOffset(trace);
            //corresponding method count with the current stack may exceeds the stack trace.
            //Trims the count
            if (methodCount + stackOffset > trace.length) {
                methodCount = trace.length - stackOffset - 1;
            }
            ArrayList<StackTraceElement> showTraces = new ArrayList<>();
            for (int i = methodCount; i > 1; i--) {
                int stackIndex = i + stackOffset;
                if (stackIndex >= trace.length) {
                    continue;
                }
                showTraces.add(trace[stackIndex]);
            }
            Collections.reverse(showTraces);
            StringBuilder builder = new StringBuilder();
            String level = "    ";
            for (StackTraceElement showTrace : showTraces) {
                builder.append("\n")
                        .append(level)
                        .append(getSimpleClassName(showTrace.getClassName()))
                        .append(".")
                        .append(showTrace.getMethodName())
                        .append(" ");
                String fileName = showTrace.getFileName();
                if (!TextUtils.isEmpty(fileName)) {
                    builder.append("(")
                            .append(fileName)
                            .append(":")
                            .append(showTrace.getLineNumber())
                            .append(")");
                }
                level += "    ";
            }
            return builder.toString();
        } catch (Exception ignore) {
            return "";
        }
    }

    /**
     * Determines the starting index of the stack trace, after method calls made by this class.
     *
     * @param trace the stack trace
     * @return the stack offset
     */
    private static int getStackOffset(StackTraceElement[] trace) {
        // starts at this class after two native calls
        final int minStackOffset = 3;
        for (int i = minStackOffset; i < trace.length; i++) {
            StackTraceElement e = trace[i];
            String name = e.getClassName();
            if (!name.equals(LogPrinter.class.getName())) {
                return --i;
            }
        }
        return -1;
    }

    private static String getSimpleClassName(String name) {
        int lastIndex = name.lastIndexOf(".");
        return name.substring(lastIndex + 1);
    }

}
