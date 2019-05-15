package cn.qqtheme.framework.wheelview.entity;

import java.util.Calendar;

/**
 * 日期视图
 *
 * @author liyujiang
 * @date 2019/5/15 17:48
 */
public class DateEntity extends DateTimeEntity {

    public static DateEntity today() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return new DateEntity(year, month, day);
    }

    public DateEntity(int year, int month, int day) {
        super(year, month, day);
    }

}
