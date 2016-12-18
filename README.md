# Summary
[![API 14+](https://img.shields.io/badge/API-14%2B-green.svg)](https://github.com/gzu-liyujiang/AndroidPicker)
[![Download](https://api.bintray.com/packages/gzu-liyujiang/maven/WheelPicker/images/download.svg)](http://jcenter.bintray.com/cn/qqtheme/framework/)
[![JitPack](https://jitpack.io/v/gzu-liyujiang/AndroidPicker.svg)](https://jitpack.io/#gzu-liyujiang/AndroidPicker)
[![Build Status](https://travis-ci.org/gzu-liyujiang/AndroidPicker.svg?branch=master)](https://travis-ci.org/gzu-liyujiang/AndroidPicker)

安卓选择器类库，包括日期选择器、时间选择器、单项选择器、城市选择器、颜色选择器、文件选择器、目录选择器、数字选择器、星座选择器、生肖选择器等，可自定义顶部及底部界面，可自定义窗口动画。
欢迎大伙儿在[Issues](https://github.com/gzu-liyujiang/AndroidPicker/issues)提交你的意见或建议。    
欢迎Fork & Pull requests贡献您的代码，大家共同学习【[AndroidPicker交流群 604235437](https://jq.qq.com/?_wv=1027&k=42bKOeD)】。
[查看更新日志](https://github.com/gzu-liyujiang/AndroidPicker/blob/master/ChangeLog.md)，**新版本未对旧版API作兼容处理，升级后若编译报错请根据错误提示更改**。

# Install
“app”是测试用例；“library”包括WheelPicker、ColorPicker、FilePicker，
WheelPicker包括DatePicker、TimePicker、OptionPicker、LinkagePicker、AddressPicker、NumberPicker等。
#### ~~懒人建议直接远程加载jcenter里的~~
WheelPicker、FilePicker及ColorPicker是独立的，需要用哪个就compile哪个。
latest.release表示使用最新版，也可以[参照此处指定具体的版本号](https://github.com/gzu-liyujiang/AndroidPicker/releases)：
```groovy
dependencies {
    compile 'cn.qqtheme.framework:WheelPicker:latest.release'
    compile 'cn.qqtheme.framework:FilePicker:latest.release'
    compile 'cn.qqtheme.framework:ColorPicker:latest.release'
}
```
#### 若jcenter仓库里的无法下载的话，可换[JitPack](https://jitpack.io/#gzu-liyujiang/AndroidPicker)的仓库试试：
第一步，在项目根目录下的build.gradle里加：
```
repositories {
    maven {
        url "https://www.jitpack.io"
    }
}
```
第二步，在项目的app模块下的build.gradle里加：
```
dependencies {
    compile 'com.github.gzu-liyujiang.AndroidPicker:WheelPicker:版本号'
    compile 'com.github.gzu-liyujiang.AndroidPicker:FilePicker:版本号'
    compile 'com.github.gzu-liyujiang.AndroidPicker:ColorPicker:版本号'
}
```
#### 使用Eclipse的话如何集成？
直接下载本项目，复制Common及WheelPicker模块下的“src/main/java”下的所有java代码到你的项目里即可。
如果需要颜色选择器或文件选择器，则复制Common及ColorPicker或FilePicker模块下的“src/main/java”及“src/main/res”下的所有文件到你的项目里。

# ProGuard
由于地址选择器使用了[fastjson](https://github.com/alibaba/fastjson)来解析，混淆时候需要加入以下类似的规则，不混淆Province、City等实体类。
```
-keepattributes InnerClasses,Signature
-keepattributes *Annotation*

-keep class cn.qqtheme.framework.entity.** { *;}
```

# Custom
#### 自定义视图
WheelView这个类是滑轮选择器的核心，可以扩展出各种效果，参见demo的[NestActivity.java](https://github.com/gzu-liyujiang/AndroidPicker/blob/master/app/src/main/java/cn/qqtheme/androidpicker/NestActivity.java)。
```java
WheelView wheelView = (WheelView) findViewById(R.id.wheelview);
wheelView.setTextColor(0xFFFF00FF);
WheelView.LineConfig config = new WheelView.LineConfig();
config.setColor(0xFFFF00FF);//线颜色
config.setAlpha(100);//线透明度
config.setRatio((float) (1.0 / 10.0));//线比率
config.setThick(ConvertUtils.toPx(this, 10));//线粗
wheelView.setLineConfig(config);
wheelView.setItems(new String[]{"贵州穿青人", "少数民族", "不在56个少数民族之列", "第57个民族"});
wheelView.setOnWheelListener(new WheelView.OnWheelListener() {
    @Override
    public void onSelected(boolean isUserScroll, int index, String item) {
        // do something
    }
});
```
#### 自定义窗口进入退出动画
在xml里定义好动画，然后调用setAnimationStyle()即可，如：
```xml
    <style name="Animation.Popup" parent="@android:style/Animation">
        <item name="android:windowEnterAnimation">@android:anim/fade_in</item>
        <item name="android:windowExitAnimation">@android:anim/fade_out</item>
    </style>

```
```java
picker.setAnimationStyle(R.style.Animation_Popup);
```
推荐使用[ViewAnimator](https://github.com/gzu-liyujiang/ViewAnimator)这个动画库来实现：
```groovy
dependencies {
    compile 'com.github.florent37:viewanimator:1.0.3'
}
```
```java
        ViewAnimator.animate(picker.getRootView())
                .slideBottomIn()
                .interpolator(new AccelerateInterpolator())
                .start();
```
#### 自定义顶部及底部界面
添加自己的类，继承自现有的选择器，覆盖makeHeaderView、makeFooterView、onSubmit、onCancel，在确定选择时调用onSubmit，
取消选择时调用onCancel。详见示例：[CustomHeaderAndFooterPicker.java](https://github.com/gzu-liyujiang/AndroidPicker/blob/master/app/src/main/java/cn/qqtheme/androidpicker/CustomHeaderAndFooterPicker.java)。

# Sample （更多用法详见示例项目）
自定义选择器：
```java
        CustomHeaderAndFooterPicker picker = new CustomHeaderAndFooterPicker(this);
        picker.setGravity(Gravity.CENTER);//居中
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int position, String option) {
                showToast(option);
            }
        });
        picker.show();
```
选择器内嵌到其他视图容器：
```java
        final TimePicker picker = new TimePicker(this, TimePicker.HOUR_12);
        picker.setOnWheelListener(new TimePicker.OnWheelListener() {
            @Override
            public void onHourWheeled(int index, String hour) {
                textView.setText(hour + ":" + picker.getSelectedMinute());
            }

            @Override
            public void onMinuteWheeled(int index, String minute) {
                textView.setText(picker.getSelectedHour() + ":" + minute);
            }
        });
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.wheelview_container);
        viewGroup.addView(picker.getContentView());
```

日期选择器：
```java
        DatePicker picker = new DatePicker(this, DatePicker.YEAR_MONTH_DAY);
        picker.setRangeStart(2016, 8, 29);//开始范围
        picker.setRangeEnd(2022, 1, 1);//结束范围
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                showToast(year + "-" + month + "-" + day);
            }
        });
        picker.show();
```

时间选择器：
```java
        TimePicker picker = new TimePicker(this, TimePicker.HOUR_12);
        picker.setRangeStart(9, 0);//09:00
        picker.setRangeEnd(12, 30);//12:30
        picker.setTopLineVisible(false);
        picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
            @Override
            public void onTimePicked(String hour, String minute) {
                showToast(hour + ":" + minute);
            }
        });
        picker.show();
```

单项选择器（可用于性别、学历、职业、生肖、星座等选择）：
```java
        OptionPicker picker = new OptionPicker(this, new String[]{
                "第一项", "第二项", "这是一个很长很长很长很长很长很长很长很长很长的很长很长的很长很长的项"
        });
        picker.setOffset(2);
        picker.setSelectedIndex(1);
        picker.setTextSize(11);
        picker.setLineConfig(new WheelView.LineConfig(0));//使用最长的线
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(String option) {
                showToast(option);
            }
        });
        picker.show();
```

数字选择器(可用于身高、体重、年龄、温度等选择)：
```java
        NumberPicker picker = new NumberPicker(this);
        picker.setOffset(2);//偏移量
        picker.setRange(145, 200, 1);//数字范围
        picker.setSelectedItem(172);
        picker.setLabel("厘米");
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(String option) {
                showToast(option);
            }
        });
        picker.show();
```

二三级联动选择器（详见示例项目，参见AddressPicker）

地址选择器（含省级、地级、县级）：
```java
        ArrayList<Province> data = new ArrayList<Province>();
        String json = AssetsUtils.readText(this, "city.json");
        data.addAll(JSON.parseArray(json, Province.class));
        AddressPicker picker = new AddressPicker(this, result);
        picker.setSelectedItem("贵州", "贵阳", "花溪");
        //picker.setHideProvince(true);//加上此句举将只显示地级及县级
        //picker.setHideCounty(true);//加上此句举将只显示省级及地级
        //picker.setColumnWeight(2/8.0, 3/8.0, 3/8.0);//省级、地级和县级的比例为2:3:3
        picker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
            @Override
            public void onAddressPicked(Province province, City city, County county) {
                showToast(province + city + county);
            }
        });
        picker.show();
```

星座选择器（参见ConstellationPicker）：
```java
        boolean isChinese = Locale.getDefault().getDisplayLanguage().contains("中文");
        OptionPicker picker = new OptionPicker(this,
                isChinese ? new String[]{
                        "水瓶", "双鱼", "白羊", "金牛", "双子", "巨蟹",
                        "狮子", "处女", "天秤", "天蝎", "射手", "摩羯"
                } : new String[]{
                        "Aquarius", "Pisces", "Aries", "Taurus", "Gemini", "Cancer",
                        "Leo", "Virgo", "Libra", "Scorpio", "Sagittarius", "Capricorn"
                });
        picker.setLabel(isChinese ? "座" : "");
        picker.setTopBackgroundColor(0xFFEEEEEE);
        picker.setTopHeight(50);
        picker.setTopLineColor(0xFF33B5E5);
        picker.setTopLineHeight(1);
        picker.setTitleText(isChinese ? "请选择" : "Please pick");
        picker.setTitleTextColor(0xFF999999);
        picker.setTitleTextSize(24);
        picker.setCancelTextColor(0xFF33B5E5);
        picker.setCancelTextSize(22);
        picker.setSubmitTextColor(0xFF33B5E5);
        picker.setSubmitTextSize(22);
        picker.setTextColor(0xFFEE0000, 0xFF999999);
        WheelView.LineConfig config = new WheelView.LineConfig();
        config.setColor(0xFFEE0000);//线颜色
        config.setAlpha(140);//线透明度
        config.setRatio((float) (1.0 / 8.0));//线比率
        picker.setLineConfig(config);
        picker.setBackgroundColor(0xFFE1E1E1);
        //picker.setSelectedItem(isChinese ? "射手" : "Sagittarius");
        picker.setSelectedIndex(10);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                showToast("index=" + index + ", item=" + item);
            }
        });
        picker.show();
```

颜色选择器：
```java
        ColorPicker picker = new ColorPicker(this);
        picker.setInitColor(0xFFDD00DD);
        picker.setOnColorPickListener(new ColorPicker.OnColorPickListener() {
            @Override
            public void onColorPicked(int pickedColor) {
                showToast(ConvertUtils.toColorString(pickedColor));
            }
        });
        picker.show();
```

文件选择器（需要权限android.permission.READ_EXTERNAL_STORAGE）：
```java
        FilePicker picker = new FilePicker(this, FilePicker.FILE);
        picker.setShowHideDir(false);
        picker.setRootPath(StorageUtils.getExternalRootPath() + "Download/");
        //picker.setAllowExtensions(new String[]{".apk"});
        picker.setOnFilePickListener(new FilePicker.OnFilePickListener() {
            @Override
            public void onFilePicked(String currentPath) {
                showToast(currentPath);
            }
        });
        picker.show();
```

目录选择器（需要权限android.permission.READ_EXTERNAL_STORAGE）：
```java
        FilePicker picker = new FilePicker(this, FilePicker.DIRECTORY);
        picker.setOnFilePickListener(new FilePicker.OnFilePickListener() {
            @Override
            public void onFilePicked(String currentPath) {
                showToast(currentPath);
            }
        });
        picker.show();
```

# Screenshots
![滑轮选择器内嵌效果图](/screenshots/nestwheelview.jpg)
![自定义选择器效果图](/screenshots/custom.gif)
![日期选择器效果图](/screenshots/date.gif)            
![日期选择器效果图](/screenshots/monthday.jpg)   
![时间选择器效果图](/screenshots/time.gif)   
![单项选择器效果图](/screenshots/option.gif)           
![地址选择器效果图](/screenshots/address.gif)   
![数字选择器效果图](/screenshots/number.gif)
![星座选择器效果图](/screenshots/constellation.jpg)   
![颜色选择器效果图](/screenshots/color.gif)    
![文件选择器效果图](/screenshots/file.gif)    
![目录选择器效果图](/screenshots/dir.gif)    

# Contact
<a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=1032694760&site=穿青人&menu=yes"><img border="0" src="http://wpa.qq.com/pa?p=2:1032694760:51" alt="点击这里给我发消息" title="点击这里给我发消息"/></a>
<a target="_blank" href="http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=q8fC0t7BwsrFzIXfwOva2oXIxMY" style="text-decoration:none;"><img src="http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_02.png"/></a>

