package cn.qqtheme.framework.logger.contract;

import cn.qqtheme.framework.logger.annotation.LogLevel;

/**
 * <pre>
 * --------------------------------------------------
 * 调试日志输出接口，可选择使用以下开源项目进行实现：
 * https://github.com/orhanobut/logger
 * https://github.com/elvishew/xLog
 * https://github.com/ZhaoKaiQiang/KLog
 * https://github.com/fengzhizi715/SAF-Kotlin-log
 * https://github.com/EsotericSoftware/minlog
 * --------------------------------------------------
 * Open Source: https://github.com/gzu-liyujiang
 * CSDN Blog: https://blog.csdn.net/waplyj
 * --------------------------------------------------
 * </pre>
 *
 * @author liyujiang
 * @date 2019-05-25 22:31
 */
@SuppressWarnings("unused")
public interface ILogger {

    /**
     * 输出日志
     *
     * @param level   日志级别
     * @param content 日志内容
     */
    void printLog(@LogLevel int level, Object content);

}
