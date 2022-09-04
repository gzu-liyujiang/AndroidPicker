# AndroidPicker

[![API 19+](https://img.shields.io/badge/API-19%2B-green.svg)](https://github.com/gzu-liyujiang/AndroidPicker)
![Release APK](https://github.com/gzu-liyujiang/AndroidPicker/workflows/Release%20APK/badge.svg)

安卓选择器类库，包括日期及时间选择器（可用于出生日期、营业时间等）、单项选择器（可用于性别、民族、职业、学历、星座等）、二三级联动选择器（可用于车牌号、基金定投日期等）、城市地址选择器（分省级、地市级及区县级）、数字选择器（可用于年龄、身高、体重、温度等）、日历选日期择器（可用于酒店及机票预定日期）、颜色选择器、文件及目录选择器等……

欢迎大伙儿在[Issues](https://github.com/gzu-liyujiang/AndroidPicker/issues)提交你的意见或建议。欢迎 Fork & Pull
requests 贡献您的代码，大家共同学习【[AndroidPicker 交流群 604235437](https://jq.qq.com/?_wv=1027&k=42bKOeD)】。

- GitHub：https://github.com/gzu-liyujiang/AndroidPicker
- 码云(GitEE)：https://gitee.com/li_yu_jiang/AndroidPicker
- Demo：[https://github.com/gzu-liyujiang/AndroidPicker/blob/master/demo.apk](/demo.apk)

## 接入指引

**最新版本** ：[![jitpack](https://jitpack.io/v/gzu-liyujiang/AndroidPicker.svg)](https://jitpack.io/#gzu-liyujiang/AndroidPicker)
（具体**历史版本号**参见 [更新日志](/ChangeLog.md)）

### 注意事项

- 3.0.0 开始完全重构了底层代码，改进了性能，对 XML 布局更友好， 3.x 版本 的 API 和 1.x 及 2.x 版本的不大一样，**请谨慎升级**。
- [1.x Support 版本封存分支](https://github.com/gzu-liyujiang/AndroidPicker/tree/1.x-support)
- [2.0 androidx 版本封存分支](https://github.com/gzu-liyujiang/AndroidPicker/tree/2.0-androidx)

### 依赖配置

#### 在项目根目录下的`build.gradle`中

```groovy
allprojects {
    repositories {
        maven { url 'https://www.jitpack.io' }
    }
}
```

#### 在项目模块下的`build.gradle`中（以下依赖项不必全部引入，请按需来）

所有选择器的基础窗体（用于自定义弹窗）：

```groovy
dependencies {
    implementation 'com.github.gzu-liyujiang.AndroidPicker:Common:<version>'
}
```

滚轮选择器的滚轮控件（用于自定义滚轮选择器）：

```groovy
dependencies {
    implementation 'com.github.gzu-liyujiang.AndroidPicker:WheelView:<version>'
}
```

单项/数字、二三级联动、日期/时间等滚轮选择器：

```groovy
dependencies {
    implementation 'com.github.gzu-liyujiang.AndroidPicker:WheelPicker:<version>'
}
```

省市区地址选择器：

```groovy
dependencies {
    implementation 'com.github.gzu-liyujiang.AndroidPicker:AddressPicker:<version>'
}
```

文件/目录选择器：

```groovy
dependencies {
    implementation 'com.github.gzu-liyujiang.AndroidPicker:FilePicker:<version>'
}
```

颜色选择器：

```groovy
dependencies {
    implementation 'com.github.gzu-liyujiang.AndroidPicker:ColorPicker:<version>'
}
```

日历日期选择器（[README.md](/CalendarPicker/README.md)）：

```groovy
dependencies {
    implementation 'com.github.gzu-liyujiang.AndroidPicker:CalendarPicker:<version>'
}
```

图片选择器（[README.md](/ImagePicker/README.md)）：

```groovy
dependencies {
    implementation 'com.github.gzu-liyujiang.AndroidPicker:ImagePicker:<version>'
}
```

旧版本 **AndroidX 稳定版本** （不推荐）：

```groovy
dependencies {
    implementation 'com.github.gzu-liyujiang.AndroidPicker:Common:2.0.0'
    implementation 'com.github.gzu-liyujiang.AndroidPicker:WheelPicker:2.0.0'
    implementation 'com.github.gzu-liyujiang.AndroidPicker:FilePicker:2.0.0'
    implementation 'com.github.gzu-liyujiang.AndroidPicker:ColorPicker:2.0.0'
}
```

旧版本 **Support 稳定版本** （不推荐）：

```groovy
dependencies {
    implementation 'com.github.gzu-liyujiang.AndroidPicker:Common:1.5.6.20181018'
    implementation 'com.github.gzu-liyujiang.AndroidPicker:WheelPicker:1.5.6.20181018'
    implementation 'com.github.gzu-liyujiang.AndroidPicker:FilePicker:1.5.6.20181018'
    implementation 'com.github.gzu-liyujiang.AndroidPicker:ColorPicker:1.5.6.20181018'
}
```

## API 说明

[API 说明文档](/API.md) 。

## 混淆规则

项目库混淆无需额外配置。

## 用法示例

常见用法请参阅 [demo](/app)，**高级用法**请细读[源码](/WheelPicker)， 诸如可以重写同名的`assets/china_address.json`来自定义省市区数据，
重写同名的`DialogSheetAnimation`来自定义弹窗动画……。 代码是最好的老师，强烈建议拉取代码运行，尝试修改 demo 对比查看实际效果以便加深理解。

### 在 Java 中

```groovy
List<GoodsCategoryBean> data = new ArrayList<>();
data.add(new GoodsCategoryBean(1, "食品生鲜"));
data.add(new GoodsCategoryBean(2, "家用电器"));
data.add(new GoodsCategoryBean(3, "家居生活"));
data.add(new GoodsCategoryBean(4, "医疗保健"));
data.add(new GoodsCategoryBean(5, "酒水饮料"));
data.add(new GoodsCategoryBean(6, "图书音像"));
OptionPicker picker = new OptionPicker(this);
picker.setTitle("货物分类");
picker.setBodyWidth(140);
picker.setData(data);
picker.setDefaultPosition(2);
picker.setOnOptionPickedListener(this);
//OptionWheelLayout wheelLayout = picker.getWheelLayout();
//wheelLayout.setIndicatorEnabled(false);
//wheelLayout.setTextColor(0xFFFF00FF);
//wheelLayout.setSelectedTextColor(0xFFFF0000);
//wheelLayout.setTextSize(15 * view.getResources().getDisplayMetrics().scaledDensity);
//wheelLayout.setSelectedTextBold(true);
//wheelLayout.setCurtainEnabled(true);
//wheelLayout.setCurtainColor(0xEEFF0000);
//wheelLayout.setCurtainCorner(CurtainCorner.ALL);
//wheelLayout.setCurtainRadius(5 * view.getResources().getDisplayMetrics().density);
//wheelLayout.setOnOptionSelectedListener(new OnOptionSelectedListener() {
//    @Override
//    public void onOptionSelected(int position, Object item) {
//        picker.getTitleView().setText(picker.getWheelView().formatItem(position));
//    }
//});
picker.show();
```

```groovy
DatePicker picker = new DatePicker(this);
//picker.setBodyWidth(240);
//DateWheelLayout wheelLayout = picker.getWheelLayout();
//wheelLayout.setDateMode(DateMode.YEAR_MONTH_DAY);
//wheelLayout.setDateLabel("年", "月", "日");
//wheelLayout.setDateFormatter(new UnitDateFormatter());
//wheelLayout.setRange(DateEntity.target(2021, 1, 1), DateEntity.target(2050, 12, 31), DateEntity.today());
//wheelLayout.setCurtainEnabled(true);
//wheelLayout.setCurtainColor(0xFFCC0000);
//wheelLayout.setIndicatorEnabled(true);
//wheelLayout.setIndicatorColor(0xFFFF0000);
//wheelLayout.setIndicatorSize(view.getResources().getDisplayMetrics().density * 2);
//wheelLayout.setTextColor(0xCCCC0000);
//wheelLayout.setSelectedTextColor(0xFFFF0000);
//wheelLayout.getYearLabelView().setTextColor(0xFF999999);
//wheelLayout.getMonthLabelView().setTextColor(0xFF999999);
picker.getWheelLayout().setResetWhenLinkage(false);
picker.setOnDatePickedListener(this);
picker.show();
```

```groovy
AddressPicker picker = new AddressPicker(this);
picker.setAddressMode(AddressMode.PROVINCE_CITY);
//picker.setAddressMode("china_address_guizhou_city.json", AddressMode.PROVINCE_CITY,
//        new AddressJsonParser.Builder()
//                .provinceCodeField("code")
//                .provinceNameField("name")
//                .provinceChildField("city")
//                .cityCodeField("code")
//                .cityNameField("name")
//                .cityChildField("area")
//                .countyCodeField("code")
//                .countyNameField("name")
//                .build());
//picker.setTitle("贵州省地址选择");
//picker.setDefaultValue("贵州省", "毕节市", "纳雍县");
picker.setOnAddressPickedListener(this);
//LinkageWheelLayout wheelLayout = picker.getWheelLayout();
//wheelLayout.setTextSize(15 * view.getResources().getDisplayMetrics().scaledDensity);
//wheelLayout.setSelectedTextBold(true);
//wheelLayout.setIndicatorEnabled(false);
//wheelLayout.setCurtainEnabled(true);
//wheelLayout.setCurtainColor(0xEE0081FF);
//wheelLayout.setCurtainRadius(5 * view.getResources().getDisplayMetrics().density);
//int padding = (int) (10 * view.getResources().getDisplayMetrics().density);
//wheelLayout.setPadding(padding, 0, padding, 0);
//wheelLayout.setOnLinkageSelectedListener(new OnLinkageSelectedListener() {
//    @Override
//    public void onLinkageSelected(Object first, Object second, Object third) {
//        picker.getTitleView().setText(String.format("%s%s%s",
//                picker.getProvinceWheelView().formatItem(first),
//                picker.getCityWheelView().formatItem(second),
//                picker.getCountyWheelView().formatItem(third)));
//    }
//});
//picker.getProvinceWheelView().setCurtainCorner(CurtainCorner.LEFT);
//picker.getCityWheelView().setCurtainCorner(CurtainCorner.RIGHT);
picker.show();
```

### 在 XML 中

```xml
<com.github.gzuliyujiang.wheelview.widget.WheelView
    android:id="@+id/wheel_view"
    android:layout_width="117dp"
    android:layout_height="wrap_content"
    android:layout_gravity="center_horizontal"
    app:wheel_atmosphericEnabled="true"
    app:wheel_curvedEnabled="true"
    app:wheel_curvedIndicatorSpace="4dp"
    app:wheel_curvedMaxAngle="60"
    app:wheel_indicatorColor="#FF0081FF"
    app:wheel_itemSpace="50dp"
    app:wheel_itemTextColor="#FF474747"
    app:wheel_itemTextColorSelected="#FF0081FF"
    app:wheel_itemTextSize="20sp" />
```

```xml
<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical">

    <com.github.gzuliyujiang.wheelpicker.widget.OptionWheelLayout
        android:id="@+id/wheel_option"
        android:layout_width="90dp"
        android:layout_height="150dp"
        android:layout_gravity="center_horizontal"
        app:wheel_itemTextAlign="center" />

    <com.github.gzuliyujiang.wheelpicker.widget.DateWheelLayout
        android:layout_width="120dp"
        android:layout_height="150dp"
        android:layout_gravity="center_horizontal"
        app:wheel_dateMode="month_day"
        app:wheel_dayLabel="日"
        app:wheel_monthLabel="月" />

    ...

</LinearLayout>
```

### 自定义样式（可选）

#### 全局配置所有选择器样式及配色

```java
//4.0.0版本开始内置支持四种弹窗样式（Default、One、Two、Three），效果可运行Demo查看
public class DemoApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DialogConfig.setDialogStyle(DialogStyle.Default);
        DialogConfig.setDialogColor(new DialogColor()
                .cancelTextColor(0xFF999999)
                .okTextColor(0xFF0099CC));
    }

}
```

#### 自定义 style

- **调用`setStyle`**（只作用于当前选择器，推荐）

在`app/.../res/values/styles.xml`中参考`WheelDefault`写个style，然后设置。

```groovy
picker.getWheelView().setStyle(R.style.WheelStyleDemo);
```

- **重写`WheelDefault`覆盖** （所有选择器都会生效，不推荐）
  
在`app/.../res/values/styles.xml`中**重写`WheelDefault`覆盖** 。

```xml
<style name="WheelDefault">
    <item name="wheel_itemSpace">15dp</item>
    <item name="wheel_itemTextColor">@android:color/darker_gray</item>
    <item name="wheel_itemTextColorSelected">@android:color/holo_blue_dark</item>
    <item name="wheel_itemTextSize">16sp</item>
    <item name="wheel_itemTextSizeSelected">18sp</item>
    <item name="wheel_itemTextBoldSelected">false</item>
    <item name="wheel_sameWidthEnabled">false</item>
    <item name="wheel_atmosphericEnabled">true</item>
    <item name="wheel_curtainEnabled">false</item>
    <item name="wheel_curtainColor">#FFDEDEDE</item>
    <item name="wheel_curvedEnabled">false</item>
    <item name="wheel_curvedMaxAngle">90</item>
    <item name="wheel_cyclicEnabled">false</item>
    <item name="wheel_indicatorEnabled">true</item>
    <item name="wheel_indicatorColor">@android:color/holo_blue_light</item>
    <item name="wheel_indicatorSize">1dp</item>
</style>
```

#### 在Java中集成重写某一选择器样式及配色

```java
//仿蚂蚁财富APP定投周期选择弹窗样式
public class AntFortuneLikePicker extends LinkagePicker {
    private int lastDialogStyle;

    public AntFortuneLikePicker(@NonNull Activity activity) {
        super(activity);
    }

    @Override
    protected void onInit(@NonNull Context context) {
        super.onInit(context);
        lastDialogStyle = DialogConfig.getDialogStyle();
        DialogConfig.setDialogStyle(DialogStyle.Default);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        DialogConfig.setDialogStyle(lastDialogStyle);
    }

    @Override
    protected void initData() {
        super.initData();
        setBackgroundColor(0xFFFFFFFF);
        cancelView.setText("取消");
        cancelView.setTextSize(16);
        cancelView.setTextColor(0xFF0081FF);
        okView.setTextColor(0xFF0081FF);
        okView.setText("确定");
        okView.setTextSize(16);
        titleView.setTextColor(0xFF333333);
        titleView.setText("定投周期");
        titleView.setTextSize(16);
        wheelLayout.setData(new AntFortuneLikeProvider());
        wheelLayout.setAtmosphericEnabled(true);
        wheelLayout.setVisibleItemCount(7);
        wheelLayout.setCyclicEnabled(false);
        wheelLayout.setIndicatorEnabled(true);
        wheelLayout.setIndicatorColor(0xFFDDDDDD);
        wheelLayout.setIndicatorSize((int) (contentView.getResources().getDisplayMetrics().density * 1));
        wheelLayout.setTextColor(0xFF999999);
        wheelLayout.setSelectedTextColor(0xFF333333);
        wheelLayout.setCurtainEnabled(false);
        wheelLayout.setCurvedEnabled(false);
    }

}
````

## 效果预览

以下图片显示的效果可能已修改过，实际效果请运行 demo 查看。

- ![效果图](/screenshots/1.webp)
- ![效果图](/screenshots/2.webp)
- ![效果图](/screenshots/3.webp)
- ![效果图](/screenshots/4.webp)
- ![效果图](/screenshots/5.webp)
- ![效果图](/screenshots/6.webp)
- ![效果图](/screenshots/7.webp)
- ![效果图](/screenshots/8.webp)
- ![效果图](/screenshots/9.webp)

## 特别鸣谢

- [基于 View 的 WheelView](https://github.com/weidongjian/androidWheelView)
- [基于 ListView 的 WheelView](https://github.com/venshine/WheelView)
- [基于 ScrollView 的 WheelView](https://github.com/wangjiegulu/WheelView)
- [SingleDateAndTimePicker](https://github.com/florent37/SingleDateAndTimePicker)
- [China_Province_City](https://github.com/small-dream/China_Province_City)
- [Administrative-divisions-of-China](https://github.com/modood/Administrative-divisions-of-China)
- [AndroidColorPicker](https://github.com/jbruchanov/AndroidColorPicker)
- [calendar](https://github.com/oxsource/calendar)
- [Android-Image-Cropper](https://github.com/ArthurHub/Android-Image-Cropper)

## 许可协议

声明：不授权给 [@q88qaz](https://github.com/q88qaz) 这种“吃人家粮还骂人家娘”的厚颜无耻的伸手党及其关联企业使用 。

### 3.0.0 之后

```text
Copyright (c) 2020-2021 gzu-liyujiang <1032694760@qq.com>

The software is licensed under the Mulan PSL v2.
You can use this software according to the terms and conditions of the Mulan PSL v2.
You may obtain a copy of Mulan PSL v2 at:
    http://license.coscl.org.cn/MulanPSL2
THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
PURPOSE.
See the Mulan PSL v2 for more details.
```

### 3.0.0 之前

```text
MIT License

Copyright (c) 穿青山魈人马<liyujiang_tk@yeah.net>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
