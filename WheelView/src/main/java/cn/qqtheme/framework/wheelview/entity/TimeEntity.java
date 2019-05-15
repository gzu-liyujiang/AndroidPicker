package cn.qqtheme.framework.wheelview.entity;

import java.util.Calendar;

/**
 * 时间实体
 *
 * @author liyujiang
 * @date 2019/5/15 17:48
 */
public class TimeEntity extends DateTimeEntity {

    public static TimeEntity now() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return new TimeEntity(hour, minute);
    }

    public TimeEntity(int hour, int minute) {
        super(hour, minute);
    }

}
