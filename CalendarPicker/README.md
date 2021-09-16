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

在自己的项目下的`res/values/colors.xml`里重写覆盖即可：

```xml
<resources>
    
    <color name="calendar_background_color">#FFFFFF</color>
    <color name="calendar_background_week_color">#FFFFFF</color>
    <color name="calendar_background_decoration_color">#FFFFFF</color>
    <color name="calendar_text_decoration_color">#333333</color>
    <color name="calendar_month_divide_line_color">#FFFFFF</color>
    <color name="calendar_month_background_color">#FFFFFF</color>
    <color name="calendar_day_text_normal_color">#343434</color>
    <color name="calendar_day_text_invalid_color">#CCCCCC</color>
    <color name="calendar_day_text_select_color">#FFFFFF</color>
    <color name="calendar_day_text_range_color">#FFFFFF</color>
    <color name="calendar_day_text_stress_color">#FF6600</color>
    <color name="calendar_day_background_normal_color">#FFFFFF</color>
    <color name="calendar_day_background_invalid_color">#FFFFFF</color>
    <color name="calendar_day_background_range_color">#EEE75051</color>
    <color name="calendar_day_background_bound_color">#FFE75051</color>
    
</resources>
```
