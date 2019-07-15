package cn.qqtheme.framework.wheelview.entity;

import java.util.Calendar;

/**
 * 时间数据实体
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/6/17 15:29
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class TimeEntity extends DateTimeEntity {

    public static TimeEntity now() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return new TimeEntity(hour, minute);
    }

    public TimeEntity(int hour, int minute, int second) {
        super(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH), hour, minute, second);
    }

    public TimeEntity(int hour, int minute) {
        this(hour, minute, 0);
    }

}
