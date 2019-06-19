package cn.qqtheme.framework.wheelview.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 时间模式
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/5/14 17:09
 */
@Retention(RetentionPolicy.SOURCE)
public @interface TimeMode {
    /**
     * 不显示
     */
    int NONE = -1;
    /**
     * 24小时制
     */
    int HOUR_24 = 1;
    /**
     * 12小时制
     */
    int HOUR_12 = 2;
    /**
     * 24小时制精确到秒
     */
    int HOUR_24_SECOND = 3;
}
