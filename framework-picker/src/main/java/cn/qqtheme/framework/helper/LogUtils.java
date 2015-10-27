package cn.qqtheme.framework.helper;

import android.util.Log;

import cn.qqtheme.framework.Config;

/**
 * 将信息记录到“LogCat”，方便调试查错
 *
 * @author 李玉江[QQ:1023694760]
 * @version 2013-11-2
 */
public class LogUtils {
    // 是否开启日志输出,在Debug状态下开启，在Release状态下关闭以提高程序性能，避免日志被人抓取
    private static boolean isDebug = Config.DEBUG_ENABLE;

    /**
     * 记录“debug”级别的信息
     *
     * @param message
     */
    public static void debug(String message) {
        if (isDebug) {
            try {
                Log.d(Config.DEBUG_TAG, message);
            } catch (Exception e) {
                System.out.println(Config.DEBUG_TAG + ">>>" + message);
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
                Log.w(Config.DEBUG_TAG, message);
            } catch (Exception e) {
                System.out.println(Config.DEBUG_TAG + ">>>" + message);
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
            warn(e.toString());
            e.printStackTrace();
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
                Log.e(Config.DEBUG_TAG, message);
            } catch (Exception e) {
                System.out.println(Config.DEBUG_TAG + ">>>" + message);
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
            error(e.toString());
            e.printStackTrace();
        }
    }

}
