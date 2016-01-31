# Summary
[![API](https://img.shields.io/badge/API-9%2B-green.svg)](https://github.com/gzu-liyujiang/AndroidPicker)
[![Download](https://api.bintray.com/packages/gzu-liyujiang/maven/AndroidPicker/images/download.svg)](http://jcenter.bintray.com/cn/qqtheme/framework/AndroidPicker)   
安卓选择器类库，包括日期选择器、时间选择器、单项选择器、城市选择器、颜色选择器、文件选择器、目录选择器、数字选择器、星座选择器、生肖选择器等，可自定义顶部及底部界面，可自定义窗口动画。   
欢迎大伙儿在[Issues](https://github.com/gzu-liyujiang/AndroidPicker/issues)提交你的意见或建议。欢迎Fork & Pull requests贡献您的代码。     

状态|链接|备注
-----|------|----
~~Deprecated~~|[~~Branch_OldAndroidPicker~~](https://github.com/gzu-liyujiang/AndroidPicker/tree/Branch_OldAndroidPicker)|~~基于android-wheel的旧版选择器，不再维护~~；
*Develop*|[dev](https://github.com/gzu-liyujiang/AndroidPicker/tree/dev)|WheelPicker、ColorPicker及FilePicker合并为一个模块来开发；
**Release**|[master](https://github.com/gzu-liyujiang/AndroidPicker/tree/master)|主分支，WheelPicker、ColorPicker及FilePicker分为三个模块。

# Change Log   
- v1.1.0 - 2016.01.29
+ 添加注解约束，如“setOffset()”只能是1至4；
+ 所有枚举类改为常量来表示，据说这样可以节约内存；
+ 支持自定义选择器的顶部及底部的视图；
+ 支持使用第三方动画库来实现窗口动画；   
- v1.0.3 - 2016.01.19
+ 日期时间、地址、单项、数字等选择器支持伪循环滚动。   
- v1.0.2 - 2016.01.15
+ 年或月变动时，保持之前选择的日不动：如果之前选择的日是之前年月的最大日，则日自动为该年月的最大日。   
- v1.0.1 - 2016.01.14
+ 精简文件选择器的数据适配器；
+ 添加选择器顶部确定、取消按钮所在容器的背景色设置。   
- v1.0.0 - 2016.01.13
+ 发布到jcenter，支持远程maven依赖。   

# Install
“app”是测试用例；“library”包括WheelPicker、ColorPicker、FilePicker，   
WheelPicker包括DatePicker、TimePicker、OptionPicker、AddressPicker、NumberPicker等，   
如果需要所有的的选择器的话，建议依赖“AndroidPicker”。     
```groovy
dependencies {
    compile 'cn.qqtheme.framework:AndroidPicker:latest.integration'
    //compile 'cn.qqtheme.framework:WheelPicker:latest.integration'
    //compile 'cn.qqtheme.framework:ColorPicker:latest.integration'
    //compile 'cn.qqtheme.framework:FilePicker:latest.integration'
}
```
也可以手动下载本项目，复制“library”命名为“AndroidPicker”，然后：
```groovy
dependencies {
    compile project('AndroidPicker')
}
```
*注：本项目使用gradle来构建，Eclipse用户建议换为Android Studio或Intellij IDEA。*   

# Custom
### 自定义窗口进入退出动画(可选，默认动画为淡入淡出)
```xml
<resources>
    <style name="Animation.CustomPopup" parent="@android:style/Animation">
        <item name="android:windowEnterAnimation">@anim/popup_in</item>
        <item name="android:windowExitAnimation">@anim/popup_out</item>
    </style>
</resources>
```   
```java
    picker.setAnimationStyle(R.style.Animation_CustomPopup);
```   
或者使用[ViewAnimator](https://github.com/gzu-liyujiang/ViewAnimator)这个动画库来实现：   
```groovy
dependencies {
    compile 'com.github.florent37:viewanimator:1.0.2'
}
```
```java
        ViewAnimator.animate(picker.getRootView())
                .slideBottomIn()
                .interpolator(new AccelerateInterpolator())
                .start();
```

### 自定义顶部及底部界面
添加自己的类，继承自现有的选择器，覆盖makeHeaderView、makeFooterView，在确定选择时调用onSubmit，
取消选择时调用onCancel。详见示例：CustomHeaderAndFooterPicker.java。   
```java
public class CustomHeaderAndFooterPicker extends OptionPicker {
  
    @Nullable
    @Override
    protected View makeHeaderView() {
        return null;//顶部视图
    }

    @Nullable
    @Override
    protected View makeFooterView() {
        return null;//底部视图
    }

}
```

# Sample
日期选择器：   
```java   
        DatePicker picker = new DatePicker(this, DatePicker.YEAR_MONTH_DAY);
        picker.setRange(1990, 2015);//年份范围
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
        //默认选中当前时间
        TimePicker picker = new TimePicker(this, TimePicker.HOUR_OF_DAY);
        picker.setTopLineVisible(false);
        picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
            @Override
            public void onTimePicked(String hour, String minute) {
                showToast(hour + ":" + minute);
            }
        });
        picker.show();
```

单项选择器（可用于性别、学历、职业、星座等选择）：   
```java   
        OptionPicker picker = new OptionPicker(this, new String[]{
                "第一项", "第二项", "这是一个很长很长很长很长很长很长很长很长很长的很长很长的很长很长的项"
        });
        picker.setOffset(2);
        picker.setSelectedIndex(1);
        picker.setTextSize(11);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(String option) {
                showToast(option);
            }
        });
        picker.show();
```

数字选择器(可用于身高、体重、年龄等选择)：
```java   
        NumberPicker picker = new NumberPicker(this);
        picker.setOffset(2);//偏移量
        picker.setRange(145, 200);//数字范围
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

地址选择器（含省级、地级、县级）：
```java   
        ArrayList<AddressPicker.Province> data = new ArrayList<AddressPicker.Province>();
        String json = AssetsUtils.readText(this, "city.json");
        data.addAll(JSON.parseArray(json, AddressPicker.Province.class));
        AddressPicker picker = new AddressPicker(this, result);
        picker.setSelectedItem("贵州", "贵阳", "花溪");
        picker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
            @Override
            public void onAddressPicked(String province, String city, String county) {
                showToast(province + city + county);
            }
        });
        picker.show();
```

地址选择器（含地级、县级）：
```java   
            ArrayList<AddressPicker.Province> data = new ArrayList<AddressPicker.Province>();
            String json = AssetsUtils.readText(this, "city2.json");
            data.addAll(JSON.parseArray(json, AddressPicker.Province.class));
            AddressPicker picker = new AddressPicker(this, data);
            picker.setHideProvince(true);
            picker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
                @Override
                public void onAddressPicked(String province, String city, String county) {
                    showToast(province + city + county);
                }
            });
            picker.show();
```

星座选择器：
```java   
        ConstellationPicker picker = new ConstellationPicker(this);
        picker.setTopBackgroundColor(0xFFEEEEEE);
        picker.setTopLineVisible(false);
        picker.setCancelTextColor(0xFF33B5E5);
        picker.setSubmitTextColor(0xFF33B5E5);
        picker.setTextColor(0xFFFF0000, 0xFFCCCCCC);
        picker.setLineColor(0xFFEE0000);
        picker.setSelectedItem("射手");
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(String option) {
                showToast(option);
            }
        });
        picker.show();
```

生肖选择器：
```java   
        ChineseZodiacPicker picker = new ChineseZodiacPicker(this);
        picker.setLineVisible(false);
        picker.setSelectedItem("羊");
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(String option) {
                showToast(option);
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
        //noinspection MissingPermission
        FilePicker picker = new FilePicker(this, FilePicker.FILE);
        picker.setShowHideDir(false);
        picker.setRootPath(StorageUtils.getRootPath(this) + "Download/");
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
        //noinspection MissingPermission
        FilePicker picker = new FilePicker(this, FilePicker.DIRECTORY);
        picker.setOnFilePickListener(new FilePicker.OnFilePickListener() {
            @Override
            public void onFilePicked(String currentPath) {
                showToast(currentPath);
            }
        });
        picker.show();
```

# Thanks
库项目修改了使用以下项目：   
https://github.com/wangjiegulu/WheelView   
https://github.com/jbruchanov/AndroidColorPicker   

# Screenshots
![自定义选择器效果图](/screenshots/custom.gif)    
![日期选择器效果图](/screenshots/date.gif)    
![时间选择器效果图](/screenshots/time.gif)    
![单项选择器效果图](/screenshots/option.gif)     
![地址选择器效果图](/screenshots/address.gif)    
![地址选择器效果图](/screenshots/address.png)    
![数字选择器效果图](/screenshots/number.gif)    
![星座选择器效果图](/screenshots/constellation.gif)    
![生肖选择器效果图](/screenshots/chinesezodiac.gif)    
![颜色选择器效果图](/screenshots/color.gif)    
![文件选择器效果图](/screenshots/file.gif)    
![目录选择器效果图](/screenshots/dir.gif)    

# Contact
<a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=1032694760&site=穿青人&menu=yes"><img border="0" src="http://wpa.qq.com/pa?p=2:1032694760:51" alt="点击这里给我发消息" title="点击这里给我发消息"/></a>
<a target="_blank" href="http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=q8fC0t7BwsrFzIXfwOva2oXIxMY" style="text-decoration:none;"><img src="http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_02.png"/></a>

