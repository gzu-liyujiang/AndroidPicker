package cn.qqtheme.framework.wheelview.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 滚轮条目对齐方式
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/6/19 12:04
 */
@Retention(RetentionPolicy.SOURCE)
public @interface ItemAlign {
    int CENTER = 0;
    int LEFT = 1;
    int RIGHT = 2;
}
