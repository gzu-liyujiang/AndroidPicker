package cn.qqtheme.framework.wheelpicker;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import java.util.Calendar;

import cn.qqtheme.framework.wheelview.annotation.DateMode;
import cn.qqtheme.framework.wheelview.entity.DateTimeEntity;

/**
 * 出生日期滚轮选择
 *
 * @author liyujiang
 * @date 2019/5/14 14:31
 */
public class BirthdayPicker extends DatePicker {
    private static final int MAX_AGE = 100;

    public BirthdayPicker(FragmentActivity activity) {
        super(activity, DateMode.YEAR_MONTH_DAY);
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        DateTimeEntity rangeStart = new DateTimeEntity(currentYear - MAX_AGE, 1, 1);
        DateTimeEntity rangeEnd = new DateTimeEntity(currentYear, currentMonth, currentDay);
        setRange(rangeStart, rangeEnd);
    }

    @Override
    public void onViewCreated(@NonNull View contentView) {
        super.onViewCreated(contentView);
        getCancelTextView().setVisibility(View.GONE);
        getConfirmTextView().setText("完成");
    }

}
