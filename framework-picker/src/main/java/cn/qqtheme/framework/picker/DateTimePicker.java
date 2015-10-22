package cn.qqtheme.framework.picker;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import cn.qqtheme.framework.helper.Logger;
import cn.qqtheme.framework.view.WheelView.WheelNumericAdapter;
import cn.qqtheme.framework.view.WheelView;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期时间选择器
 *
 * @author Sai
 */
public class DateTimePicker extends WheelPicker<Date> {
    private WheelView yearView, monthView, dayView;
    private WheelView hourView, minuteView;
    private Mode mode = Mode.ALL;
    private int startYear = 1970, endYear = 2070;
    private int currentYear = 1990, currentMonth = 11, currentDay = 4;

    public enum Mode {
        //年月日时分
        ALL,
        //年月日
        YEAR_MONTH_DAY,
        //时分
        HOUR_MINUTE,
        //月日时分
        MONTH_DAY_HOUR_MINUTE
    }

    public DateTimePicker(Activity activity) {
        super(activity);
    }

    @Override
    protected LinearLayout initWheelView() {
        LinearLayout rootLayout = new LinearLayout(activity);
        rootLayout.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        rootLayout.setOrientation(LinearLayout.HORIZONTAL);
        rootLayout.setBackgroundColor(Color.WHITE);
        yearView = new WheelView(activity);
        yearView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, 1.0f));
        rootLayout.addView(yearView);
        monthView = new WheelView(activity);
        monthView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, 1.0f));
        rootLayout.addView(monthView);
        dayView = new WheelView(activity);
        dayView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, 1.0f));
        rootLayout.addView(dayView);
        hourView = new WheelView(activity);
        hourView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, 1.0f));
        rootLayout.addView(hourView);
        minuteView = new WheelView(activity);
        minuteView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, 1.0f));
        rootLayout.addView(minuteView);
        return rootLayout;
    }

    @Override
    public Date getCurrentSelected() {
        int yearIndex = yearView.getCurrentItem();
        int monthIndex = monthView.getCurrentItem();
        int dayIndex = dayView.getCurrentItem();
        int hourIndex = hourView.getCurrentItem();
        int minuteIndex = minuteView.getCurrentItem();
        Logger.debug(String.format("selected index: %s,%s,%s,%s,%s", yearIndex, monthIndex, dayIndex, hourIndex, minuteIndex));
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, yearIndex + startYear);
        calendar.set(Calendar.MONTH, monthIndex);//月份从0开始计数
        calendar.set(Calendar.DAY_OF_MONTH, dayIndex + 1);
        calendar.set(Calendar.HOUR_OF_DAY, hourIndex);
        calendar.set(Calendar.MINUTE, minuteIndex);
        return calendar.getTime();
    }

    /**
     * 设置是否循环滚动
     *
     * @param cyclic
     */
    public void setCyclic(boolean cyclic) {
        yearView.setCyclic(cyclic);
        monthView.setCyclic(cyclic);
        dayView.setCyclic(cyclic);
        hourView.setCyclic(cyclic);
        minuteView.setCyclic(cyclic);
    }

    public void setMode(Mode mode) {
        if (mode == null) {
            this.mode = Mode.YEAR_MONTH_DAY;
        } else {
            this.mode = mode;
        }
        setDate(System.currentTimeMillis());
    }

    public void setRange(int startYear, int endYear) {
        this.startYear = startYear;
        this.endYear = endYear;
        if (currentYear < startYear) {
            currentYear = startYear;
        }
        if (currentYear > endYear) {
            currentYear = endYear;
        }
        setDate(System.currentTimeMillis());
    }

    /**
     * 设置选中的日期
     *
     * @param year
     * @param month
     * @param day
     */
    public void setSelectedDate(int year, int month, int day) {
        if (year < startYear) {
            year = startYear;
        }
        if (year > endYear) {
            year = endYear;
        }
        this.currentYear = year;
        if (month < 1) {
            month = 1;
        }
        if (month > 12) {
            month = 12;
        }
        this.currentMonth = month;
        int days = calculateDaysInMonth(year, month);
        if (day < 1) {
            day = 1;
        }
        if (day > days) {
            day = days;
        }
        this.currentDay = day;
        yearView.setCurrentItem(currentYear - startYear);
        monthView.setCurrentItem(currentMonth - 1);
        dayView.setCurrentItem(currentDay - 1);
    }

    public void setDate(long milliseconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        int year = calendar.get(Calendar.YEAR);
        if (year < startYear) {
            year = startYear;
        }
        if (year > endYear) {
            year = endYear;
        }
        int month = calendar.get(Calendar.MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        yearView.setAdapter(new DateTimeAdapter(startYear, endYear));
        yearView.setLabel("年");
        yearView.setCurrentItem(currentYear - startYear);

        monthView.setAdapter(new DateTimeAdapter(1, 12));
        monthView.setLabel("月");
        monthView.setCurrentItem(currentMonth - 1);

        dayView.setAdapter(new DateTimeAdapter(1, calculateDaysInMonth(year, month + 1)));
        dayView.setLabel("日");
        dayView.setCurrentItem(currentDay - 1);

        hourView.setAdapter(new DateTimeAdapter(0, 23));
        hourView.setLabel("时");
        hourView.setCurrentItem(hour);

        minuteView.setAdapter(new DateTimeAdapter(0, 59));
        minuteView.setLabel("分");
        minuteView.setCurrentItem(minute);

        // 添加"年"监听
        yearView.addChangingListener(new WheelView.OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int maxItem = calculateDaysInMonth(newValue + startYear, monthView.getCurrentItem() + 1);
                dayView.setAdapter(new DateTimeAdapter(1, maxItem));
                if (dayView.getCurrentItem() > maxItem - 1) {
                    dayView.setCurrentItem(maxItem - 1);
                }
            }
        });
        // 添加"月"监听
        monthView.addChangingListener(new WheelView.OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int maxItem = calculateDaysInMonth(yearView.getCurrentItem() + startYear, newValue + 1);
                dayView.setAdapter(new DateTimeAdapter(1, maxItem));
                if (dayView.getCurrentItem() > maxItem - 1) {
                    dayView.setCurrentItem(maxItem - 1);
                }

            }
        });

        // 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
        int textSize = 11;
        switch (mode) {
            case ALL:
                textSize = (screenHeight / 100) * 3;
                break;
            case YEAR_MONTH_DAY:
                textSize = (screenHeight / 100) * 4;
                hourView.setVisibility(View.GONE);
                minuteView.setVisibility(View.GONE);
                break;
            case HOUR_MINUTE:
                textSize = (screenHeight / 100) * 4;
                yearView.setVisibility(View.GONE);
                monthView.setVisibility(View.GONE);
                dayView.setVisibility(View.GONE);
                break;
            case MONTH_DAY_HOUR_MINUTE:
                textSize = (screenHeight / 100) * 3;
                yearView.setVisibility(View.GONE);
                break;
        }

        dayView.TEXT_SIZE = textSize;
        monthView.TEXT_SIZE = textSize;
        yearView.TEXT_SIZE = textSize;
        hourView.TEXT_SIZE = textSize;
        minuteView.TEXT_SIZE = textSize;

    }

    private int calculateDaysInMonth(int year, int month) {
        // 添加大小月月份并将其转换为list,方便之后的判断
        String[] bigMonths = {"1", "3", "5", "7", "8", "10", "12"};
        String[] littleMonths = {"4", "6", "9", "11"};
        List<String> bigList = Arrays.asList(bigMonths);
        List<String> littleList = Arrays.asList(littleMonths);
        // 判断大小月及是否闰年,用来确定"日"的数据
        if (bigList.contains(String.valueOf(month))) {
            return 31;
        } else if (littleList.contains(String.valueOf(month))) {
            return 30;
        } else {
            // 是否闰年
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                return 29;
            } else {
                return 28;
            }
        }
    }

    private class DateTimeAdapter extends WheelNumericAdapter {

        public DateTimeAdapter() {
            super();
        }

        public DateTimeAdapter(int minValue, int maxValue) {
            super(minValue, maxValue);
        }

        public DateTimeAdapter(int minValue, int maxValue, String format) {
            super(minValue, maxValue, format);
        }

        @Override
        public String getItem(int index) {
            String item = super.getItem(index);
            //0-9前面补0
            if (item != null && item.length() == 1) {
                item = "0" + item;
            }
            return item;
        }

    }

}
