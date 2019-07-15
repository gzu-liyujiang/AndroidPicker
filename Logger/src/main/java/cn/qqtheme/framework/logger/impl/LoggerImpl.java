package cn.qqtheme.framework.logger.impl;

import android.support.annotation.RestrictTo;
import android.util.Log;

import cn.qqtheme.framework.logger.contract.ILogger;
import cn.qqtheme.framework.logger.annotation.LogLevel;

/**
 * <pre>
 * --------------------------------------------------
 * 默认的日志输出工具
 * --------------------------------------------------
 * Open Source: https://github.com/gzu-liyujiang
 * CSDN Blog: https://blog.csdn.net/waplyj
 * --------------------------------------------------
 * </pre>
 *
 * @author liyujiang
 * @date 2019-05-26 01:47
 */
@SuppressWarnings("unused")
@RestrictTo(RestrictTo.Scope.LIBRARY)
public final class LoggerImpl implements ILogger {
    private boolean enable = true;
    private String tag = "liyujiang";

    @Override
    public void printLog(@LogLevel int level, Object content) {
        if (!enable) {
            return;
        }
        // 由于不少国产手机默认禁用了verbose、debug等级别的日志，
        // 为了所有机型能看到日志，故使用WARN级别及ERROR级别
        if (level < Log.WARN) {
            level = Log.WARN;
        }
        LogPrinter.println(level, tag, content);
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

}