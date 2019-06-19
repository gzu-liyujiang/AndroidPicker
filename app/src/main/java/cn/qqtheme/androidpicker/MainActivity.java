package cn.qqtheme.androidpicker;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;

import com.blankj.utilcode.util.TimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.qqtheme.androidpicker.custom.CustomUiAddressPicker;
import cn.qqtheme.framework.calendarpicker.CalendarPicker;
import cn.qqtheme.framework.wheelpicker.BirthdayPicker;
import cn.qqtheme.framework.wheelpicker.DatePicker;
import cn.qqtheme.framework.wheelpicker.DateTimePicker;
import cn.qqtheme.framework.wheelpicker.PhoneCodePicker;
import cn.qqtheme.framework.wheelpicker.SinglePicker;
import cn.qqtheme.framework.wheelpicker.TimePicker;
import cn.qqtheme.framework.wheelview.annotation.DateMode;
import cn.qqtheme.framework.wheelview.annotation.TimeMode;
import cn.qqtheme.framework.wheelview.entity.DateEntity;
import cn.qqtheme.framework.wheelview.entity.DateTimeEntity;
import cn.qqtheme.framework.wheelview.entity.TimeEntity;

/**
 * 主页
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2016/7/20 20:28
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onNestWheelPickerInView(View view) {
    }

    public void onAnimationStyle(View view) {
    }

    public void onWindowAnimator(View view) {
    }

    public void onCustomStyle(View view) {
        DateTimePicker picker = new DateTimePicker(this);
        picker.setWheelStyle(R.style.WheelDateTime_Custom);
        picker.showAtBottom();
    }

    public void onCalendarDateRangePicker(View view) {
        CalendarPicker picker = new CalendarPicker(this, false);
        picker.showAtBottom();
    }

    public void onCalendarDateSinglePicker(View view) {
        CalendarPicker picker = new CalendarPicker(this, true);
        picker.setRangeDateOnFuture(6);
        picker.showAtBottom();
    }

    public void onYearMonthDayTimePicker(View view) {
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

    public void onYearMonthDayPicker(View view) {
        DateEntity today = DateEntity.today();
        DatePicker picker = new DatePicker(this, DateMode.YEAR_MONTH_DAY);
        picker.setRange(today, new DateEntity(2020, 12, 31));
        picker.setDefaultValue(today);
        picker.showAtBottom();
    }

    public void onYearMonthPicker(View view) {
        DatePicker picker = new DatePicker(this, DateMode.YEAR_MONTH);
        picker.showAtBottom();
    }

    public void onMonthDayPicker(View view) {
        DatePicker picker = new DatePicker(this, DateMode.MONTH_DAY);
        picker.showAtBottom();
    }

    public void onTimePicker(View view) {
        TimePicker picker = new TimePicker(this, TimeMode.HOUR_24);
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        picker.setDefaultValue(new TimeEntity(hour, minute));
        picker.showAtBottom();
    }

    public void onBirthdayPicker(View view) {
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

    public void onSinglePicker(View view) {
        SinglePicker<String> picker = new SinglePicker<>(this);
        List<String> data = new ArrayList<>();
        data.add("第一项");
        data.add("很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长长很长很长");
        data.add("其他项");
        data.add("最后一项");
        picker.setData(data);
        picker.setDefaultItemPosition(2);
        picker.showAtBottom();
    }

    public void onPhoneCodePicker(View view) {
        PhoneCodePicker picker = new PhoneCodePicker(this);
        picker.setSelectCallback(new PhoneCodePicker.SelectCallback() {
            @Override
            public void onItemSelected(int position, String item) {
            }
        });
        picker.showAtBottom();
    }

    public void onNumberPicker(View view) {
    }

    public void onConstellationPicker(View view) {
    }

    public void onDoublePicker(View view) {
    }

    public void onBusinessTimePicker(View view) {
    }

    public void onLinkagePicker(View view) {
    }

    public void onCustomUiAddressPicker(View view) {
        CustomUiAddressPicker picker = new CustomUiAddressPicker(this);
        picker.showAtBottom();
    }

    public void onCustomDataSourceAddressPicker(View view) {
    }

    public void onAddressNoProvincePicker(View view) {
    }

    public void onAddressNoCountyPicker(View view) {
    }

    public void onColorPicker(View view) {
    }

    public void onFilePicker(View view) {
    }

    public void onDirectoryPicker(View view) {
    }

}
