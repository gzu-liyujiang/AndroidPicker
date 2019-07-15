package cn.qqtheme.androidpicker.ui;

import android.view.View;

import cn.qqtheme.androidpicker.BaseActivity;
import cn.qqtheme.androidpicker.R;
import cn.qqtheme.framework.calendarpicker.CalendarPicker;

/**
 * 日历日期选择器
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/6/23
 */
public class CalendarDateActivity extends BaseActivity {

    @Override
    protected int specifyLayoutRes() {
        return R.layout.activity_calendar_date;
    }

    public void onCalendarDateRangePicker(View view) {
        CalendarPicker picker = new CalendarPicker(this, false);
        picker.setRangeDateOnFuture(6);
        picker.showAtBottom();
    }

    public void onCalendarDateSinglePicker(View view) {
        CalendarPicker picker = new CalendarPicker(this, true);
        picker.showAtBottom();
    }

}
