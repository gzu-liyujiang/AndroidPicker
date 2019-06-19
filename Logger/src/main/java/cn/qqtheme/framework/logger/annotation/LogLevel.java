package cn.qqtheme.framework.logger.annotation;

import android.support.annotation.IntDef;
import android.util.Log;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <pre>
 * --------------------------------------------------
 * 日志级别约束
 * --------------------------------------------------
 * Open Source: https://github.com/gzu-liyujiang
 * CSDN Blog: https://blog.csdn.net/waplyj
 * --------------------------------------------------
 * </pre>
 *
 * @author liyujiang
 * @date 2019-05-26 02:00
 */
@IntDef(value = {
        Log.VERBOSE,
        Log.DEBUG,
        Log.INFO,
        Log.WARN,
        Log.ERROR,
        Log.ASSERT
})
@Retention(RetentionPolicy.SOURCE)
public @interface LogLevel {
}
