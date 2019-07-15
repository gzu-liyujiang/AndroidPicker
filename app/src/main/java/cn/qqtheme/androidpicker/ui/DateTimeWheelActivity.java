package cn.qqtheme.androidpicker.ui;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;

import com.blankj.utilcode.util.TimeUtils;

import java.util.Calendar;
import java.util.Date;

import cn.qqtheme.androidpicker.BaseActivity;
import cn.qqtheme.androidpicker.R;
import cn.qqtheme.framework.wheelpicker.BirthdayPicker;
import cn.qqtheme.framework.wheelpicker.DatePicker;
import cn.qqtheme.framework.wheelpicker.DateTimePicker;
import cn.qqtheme.framework.wheelpicker.TimePicker;
import cn.qqtheme.framework.wheelview.annotation.DateMode;
import cn.qqtheme.framework.wheelview.annotation.TimeMode;
import cn.qqtheme.framework.wheelview.entity.DateEntity;
import cn.qqtheme.framework.wheelview.entity.DateTimeEntity;
import cn.qqtheme.framework.wheelview.entity.TimeEntity;

/**
 * 日期时间滚轮选择器
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/6/23
 */
public class DateTimeWheelActivity extends BaseActivity {

    @Override
    protected int specifyLayoutRes() {
        return R.layout.activity_date_time_wheel;
    }

    public void onCustomStyle(View view) {
        DateTimePicker picker = new DateTimePicker(this);
        picker.setWheelStyle(R.style.WheelDateTime_Custom);
        picker.showAtBottom();
    }

    public void onYearMonthDayTime(View view) {
        DateTimeEntity now = DateTimeEntity.now();
        DateTimeEntity last = new DateTimeEntity(2020, 1, 1, 23, 59);
        DateTimePicker picker = new DateTimePicker(this, DateMode.YEAR_MONTH_DAY,
                TimeMode.HOUR_24);
        picker.setRange(now, last);
        picker.setDefaultValue(now);
        picker.setDateLabel("年", "月", "日");
        SpannableString ss = new SpannableString(":");
        ss.setSpan(new StyleSpan(Typeface.BOLD), 0, ss.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        picker.setTimeLabel(ss, "", "");
        picker.showAtBottom();
    }

    public void onYearMonthDay(View view) {
        DateEntity today = DateEntity.today();
        DatePicker picker = new DatePicker(this, DateMode.YEAR_MONTH_DAY);
        picker.setRange(today, new DateEntity(2020, 12, 31));
        picker.setDefaultValue(today);
        picker.showAtBottom();
    }

    public void onYearMonth(View view) {
        DatePicker picker = new DatePicker(this, DateMode.YEAR_MONTH);
        picker.showAtBottom();
    }

    public void onMonthDay(View view) {
        DatePicker picker = new DatePicker(this, DateMode.MONTH_DAY);
        picker.showAtBottom();
    }

    public void onTime(View view) {
        TimePicker picker = new TimePicker(this, TimeMode.HOUR_24);
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        picker.setDefaultValue(new TimeEntity(hour, minute));
        picker.showAtBottom();
    }

    public void onBirthday(View view) {
        BirthdayPicker picker = new BirthdayPicker(this);
        Date date = TimeUtils.string2Date("1991-11-11");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        picker.setDefaultValue(new DateEntity(year, month, day));
        picker.showAtBottom();
    }

}
