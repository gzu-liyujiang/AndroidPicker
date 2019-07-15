package cn.qqtheme.framework.logger;

import android.util.Log;

import cn.qqtheme.framework.logger.annotation.LogLevel;
import cn.qqtheme.framework.logger.contract.ILogger;
import cn.qqtheme.framework.logger.impl.LoggerImpl;

/**
 * <pre>
 * --------------------------------------------------
 * 输出日志内容，显示线程、调用栈及所在的文件行号等信息，方便开发时调试查错。
 * 注意：建议在Debug状态下开启，在Release状态下关闭，不宜输出敏感信息。
 * --------------------------------------------------
 * Open Source: https://github.com/gzu-liyujiang
 * CSDN Blog: https://blog.csdn.net/waplyj
 * --------------------------------------------------
 * </pre>
 *
 * @author liyujiang
 * @date 2019-05-26 01:43
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public final class CqrLog {
    private static ILogger logger;

    private CqrLog() {
    }

    /**
     * 使用默认的日志输出工具
     *
     * @param enable true-启用日志输出，false-禁用日志输出
     * @param tag    日志标签，启用日志输出时有效
     */
    public static void useDefaultLogger(boolean enable, String tag) {
        LoggerImpl logger = new LoggerImpl();
        logger.setEnable(enable);
        logger.setTag(tag);
        CqrLog.logger = logger;
    }

    /**
     * 设置日志输出工具
     *
     * @param iLogger 指定的调试日志输出工具
     */
    public static void setLogger(ILogger iLogger) {
        logger = iLogger;
    }

    /**
     * 输出 DEBUG 级别的日志
     *
     * @param content 日志内容
     */
    public static void d(Object content) {
        print(Log.DEBUG, content);
    }

    /**
     * 输出 WARN 级别的日志
     *
     * @param content 日志内容
     */
    public static void w(Object content) {
        print(Log.WARN, content);
    }

    /**
     * 输出 ERROR 级别的日志
     *
     * @param content 日志内容
     */
    public static void e(Object content) {
        print(Log.ERROR, content);
    }

    /**
     * 输出日志
     *
     * @param level   日志级别
     * @param content 日志内容
     */
    public static void print(@LogLevel int level, Object content) {
        logger.printLog(level, content);
    }

}
