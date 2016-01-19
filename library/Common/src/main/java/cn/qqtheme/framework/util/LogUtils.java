package cn.qqtheme.framework.util;

import android.os.Debug;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import cn.qqtheme.framework.AppConfig;

/**
 * 如果用于android平台，将信息记录到“LogCat”。如果用于java平台，将信息记录到“Console”
 *
 * @author 李玉江[QQ :1023694760]
 * @version 2013 -11-2
 */
public class LogUtils {
    private static final int MAX_STACK_TRACE_SIZE = 131071; //128 KB - 1
    // 是否开启日志输出,在Debug状态下开启，在Release状态下关闭以提高程序性能，避免日志被人抓取
    private static boolean isDebug = AppConfig.DEBUG_ENABLE;
    private static String debugTag = AppConfig.DEBUG_TAG;

    /**
     * Debug.
     *
     * @param message the message
     */
    public static void debug(String message) {
        debug(debugTag, message);
    }

    /**
     * Debug.
     *
     * @param object  the object
     * @param message the message
     */
    public static void debug(Object object, String message) {
        debug(object.getClass().getSimpleName(), message);
    }

    /**
     * 记录“debug”级别的信息
     *
     * @param tag     the tag
     * @param message the message
     */
    public static void debug(String tag, String message) {
        if (isDebug) {
            try {
                Log.d(debugTag + "-" + tag, message);
            } catch (Exception e) {
                System.out.println(tag + ">>>" + message);
            }
        }
    }

    /**
     * Warn.
     *
     * @param e the e
     */
    public static void warn(Throwable e) {
        warn(toStackTraceString(e));
    }

    /**
     * Warn.
     *
     * @param message the message
     */
    public static void warn(String message) {
        warn(debugTag, message);
    }

    /**
     * Warn.
     *
     * @param object  the object
     * @param message the message
     */
    public static void warn(Object object, String message) {
        warn(object.getClass().getSimpleName(), message);
    }

    /**
     * Warn.
     *
     * @param object the object
     * @param e      the e
     */
    public static void warn(Object object, Throwable e) {
        warn(object.getClass().getSimpleName(), toStackTraceString(e));
    }

    /**
     * 记录“warn”级别的信息
     *
     * @param tag     the tag
     * @param message the message
     */
    public static void warn(String tag, String message) {
        if (isDebug) {
            try {
                Log.w(debugTag + tag, message);
            } catch (Exception e) {
                System.out.println(debugTag + ">>>" + message);
            }
        }
    }

    /**
     * Error.
     *
     * @param e the e
     */
    public static void error(Throwable e) {
        error(toStackTraceString(e));
    }

    /**
     * Error.
     *
     * @param message the message
     */
    public static void error(String message) {
        error(debugTag, message);
    }

    /**
     * Error.
     *
     * @param object  the object
     * @param message the message
     */
    public static void error(Object object, String message) {
        error(object.getClass().getSimpleName(), message);
    }

    /**
     * Error.
     *
     * @param object the object
     * @param e      the e
     */
    public static void error(Object object, Throwable e) {
        error(object.getClass().getSimpleName(), toStackTraceString(e));
    }

    /**
     * 记录“error”级别的信息
     *
     * @param tag     the tag
     * @param message the message
     */
    public static void error(String tag, String message) {
        if (isDebug) {
            try {
                Log.e(debugTag + tag, message);
            } catch (Exception e) {
                System.out.println(debugTag + ">>>" + message);
            }
        }
    }

    /**
     * 在某个方法中调用生成.trace文件。然后拿到电脑上用DDMS工具打开分析
     *
     * @see #stopMethodTracing() #stopMethodTracing()#stopMethodTracing()
     */
    public static void startMethodTracing() {
        if (isDebug) {
            Debug.startMethodTracing(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + debugTag + ".trace");
        }
    }

    /**
     * Stop method tracing.
     */
    public static void stopMethodTracing() {
        if (isDebug) {
            Debug.stopMethodTracing();
        }
    }

    /**
     * To stack trace string string.
     *
     * @param throwable the throwable
     * @return the string
     */
    public static String toStackTraceString(Throwable throwable) {
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
