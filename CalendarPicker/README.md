# 日历选择器

基于 [双向日期选择日历控件](https://github.com/oxsource/calendar) 开发。

## 示例

```groovy
        CalendarPicker picker = new CalendarPicker(this);
        picker.setRangeDateOnFuture(3);
        if (singleTimeInMillis == 0) {
            singleTimeInMillis = System.currentTimeMillis();
        }
        picker.setSelectedDate(singleTimeInMillis);
        picker.setOnSingleDatePickListener(new OnSingleDatePickListener() {
            @Override
            public void onSingleDatePicked(@NonNull Date date) {
                singleTimeInMillis = date.getTime();
            }
        });
        picker.show();
```

## 配色

```groovy
        CalendarPicker picker = new CalendarPicker(this);
        picker.setColorScheme(new ColorScheme()
                .daySelectBackgroundColor(0xFF0000FF)
                .dayStressTextColor(0xFF0000DD));
        picker.show();
```
