package cn.qqtheme.framework.wheelpicker.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 地址模式
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/6/15 12:19
 */
@Retention(RetentionPolicy.SOURCE)
public @interface AddressMode {
    /**
     * 省级、市级、县级
     */
    int PROVINCE_CITY_COUNTY = 0;
    /**
     * 省级、市级
     */
    int PROVINCE_CITY = 1;
}
