package cn.qqtheme.framework.wheelview.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 日期模式
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/5/14 17:10
 */
@Retention(RetentionPolicy.SOURCE)
public @interface DateMode {
    /**
     * 不显示
     */
    int NONE = -1;
    /**
     * 年月日
     */
    int YEAR_MONTH_DAY = 0;
    /**
     * 年月
     */
    int YEAR_MONTH = 1;
    /**
     * 月日
     */
    int MONTH_DAY = 2;
}
