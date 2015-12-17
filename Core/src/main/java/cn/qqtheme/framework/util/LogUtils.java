package cn.qqtheme.framework.util;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

import cn.qqtheme.framework.AppConfig;

/**
 * 将信息记录到“LogCat”，方便调试查错
 *
 * @author 李玉江[QQ:1023694760]
 * @version 2013-11-2
 */
public class LogUtils {
    private static final int MAX_STACK_TRACE_SIZE = 131071; //128 KB - 1
    // 是否开启日志输出,在Debug状态下开启，在Release状态下关闭以提高程序性能，避免日志被人抓取
    private static boolean isDebug = AppConfig.DEBUG_ENABLE;

    /**
     * 记录“debug”级别的信息
     *
     * @param message
     */
    public static void debug(String message) {
        if (isDebug) {
            try {
                Log.d(AppConfig.DEBUG_TAG, message);
            } catch (Exception e) {
                System.out.println(AppConfig.DEBUG_TAG + ">>>" + message);
            }
        }
    }

    /**
     * 记录“warn”级别的信息
     *
     * @param message
     */
    public static void warn(String message) {
        if (isDebug) {
            try {
                Log.w(AppConfig.DEBUG_TAG, message);
            } catch (Exception e) {
                System.out.println(AppConfig.DEBUG_TAG + ">>>" + message);
            }
        }
    }

    /**
     * 记录“warn”级别的信息
     *
     * @param e
     */
    public static void warn(Throwable e) {
        if (isDebug) {
            warn(toStackTraceString(e));
        }
    }

    /**
     * 记录“error”级别的信息
     *
     * @param message
     */
    public static void error(String message) {
        if (isDebug) {
            try {
                Log.e(AppConfig.DEBUG_TAG, message);
            } catch (Exception e) {
                System.out.println(AppConfig.DEBUG_TAG + ">>>" + message);
            }
        }
    }

    /**
     * 记录“error”级别的信息
     *
     * @param e
     */
    public static void error(Throwable e) {
        if (isDebug) {
            error(toStackTraceString(e));
        }
    }

    private static String toStackTraceString(Throwable throwable){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        String stackTraceString = sw.toString();
        //Reduce data to 128KB so we don't get a TransactionTooLargeException when sending the intent.
        //The limit is 1MB on Android but some devices seem to have it lower.
        //See: http://developer.android.com/reference/android/os/TransactionTooLargeException.html
        //And: http://stackoverflow.com/questions/11451393/what-to-do-on-transactiontoolargeexception#comment46697371_12809171
        if (stackTraceString.length() > MAX_STACK_TRACE_SIZE) {
            String disclaimer = " [stack trace too large]";
            stackTraceString = stackTraceString.substring(0, MAX_STACK_TRACE_SIZE - disclaimer.length()) + disclaimer;
        }
        return stackTraceString;
    }

}
