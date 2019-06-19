package cn.qqtheme.framework.wheelview.entity;

import java.util.Calendar;

/**
 * 日期数据实体
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/6/17 15:29
 */
@SuppressWarnings({"unused"})
public class DateEntity extends DateTimeEntity {

    public static DateEntity today() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return new DateEntity(year, month, day);
    }

    public DateEntity(int year, int month, int day) {
        super(year, month, day, 0, 0, 0);
    }

    public DateEntity(int month, int day) {
        this(Calendar.getInstance().get(Calendar.YEAR), month, day);
    }

}
