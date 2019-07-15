package cn.qqtheme.androidpicker.impl;

import android.util.Log;

import com.blankj.utilcode.util.LogUtils;

import cn.qqtheme.androidpicker.BuildConfig;
import cn.qqtheme.framework.logger.contract.ILogger;

/**
 * 自定义日志打印器
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/6/19 20:25
 */
public class LoggerImpl implements ILogger {
    private static final String LOG_TAG = "liyujiang";

    @Override
    public void printLog(int level, Object content) {
        //注：日志边框要正确显示的话，不开启用LogCat的换行“Toggle using soft wraps”
        LogUtils.getConfig().setLogSwitch(BuildConfig.DEBUG);
        LogUtils.getConfig().setConsoleSwitch(BuildConfig.DEBUG);
        LogUtils.getConfig().setLog2FileSwitch(false);
        LogUtils.getConfig().setGlobalTag(LOG_TAG);
        LogUtils.getConfig().setStackDeep(1);
        if (level > Log.WARN) {
            LogUtils.e(content);
        } else {
            LogUtils.w(content);
        }
    }

}

