# Summary
安卓选择器类库，包括日期选择器、时间选择器、单项选择器、城市选择器、颜色选择器、文件选择器、目录选择器、数字选择器、星座选择器、生肖选择器等。

# Install
app是测试用例；Core是其他模块的依赖项；WheelPicker是日期、时间、单项等选择器；ColorPicker是颜色选择器；FilePicker是文件、目录选择器。     
1、通过SVN或GIT工具下载本项目，复制“AndroidPicker”到你的电脑上；<br />
2、用Android Studio或IntelliJ IDEA打开你的项目，New->Import Module，选择“AndroidPicker”下的某个模块；<br />
3、修改你的项目的build.gradle，把选择“AndroidPicker”下的某个模块加入依赖项，如：
```
dependencies {
    compile project(':WheelPicker')
    compile project(':FilePicker')
    compile project(':ColorPicker')
}
```   
4、自定义窗口进入退出动画(可选，默认动画为淡入淡出)：在Application的子类中调用“Popup.setAnimation()”即可，如：
```xml
<resources>
    <style name="Animation.CustomPopup" parent="@android:style/Animation">
        <item name="android:windowEnterAnimation">@anim/popup_in</item>
        <item name="android:windowExitAnimation">@anim/popup_out</item>
    </style>
</resources>
```   
```java
public class DemoApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Popup.setAnimation(R.style.Animation_CustomPopup);
    }

}
```   

注：   
不断学习，持续完善，敬请关注。。。   
gradle为1.3.0，buildTools为23.0.1，sdk为23。   
支持android2.3+，项目编码为UTF-8。   

# Sample
日期选择器：   
```java   
        DatePicker picker = new DatePicker(this);
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
        TimePicker picker = new TimePicker(this);
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
        picker.setTopLineColor(0xFFEE0000);
        picker.setTextColor(0xFFFF0000, 0xFF999999);
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

文件选择器：
```java   
        FilePicker picker = new FilePicker(this);
        picker.setShowHideDir(false);
        picker.setRootPath(StorageUtils.getRootPath(this) + "Download/");
        //picker.setAllowExtensions(new String[]{".apk"});
        picker.setMode(FilePicker.Mode.File);
        picker.setOnFilePickListener(new FilePicker.OnFilePickListener() {
            @Override
            public void onFilePicked(String currentPath) {
                showToast(currentPath);
            }
        });
        picker.show();
```

目录选择器：
```java   
        FilePicker picker = new FilePicker(this);
        picker.setMode(FilePicker.Mode.Directory);
        picker.setOnFilePickListener(new FilePicker.OnFilePickListener() {
            @Override
            public void onFilePicked(String currentPath) {
                showToast(currentPath);
            }
        });
        picker.show();
```

# Thanks
修改了使用以下项目：<br />
https://github.com/wangjiegulu/WheelView<br />
https://github.com/jbruchanov/AndroidColorPicker<br />
https://github.com/JoanZapata/base-adapter-helper<br />
直接使用了以下项目：<br />
https://github.com/alibaba/fastjson<br />

# Screenshots
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
李玉江, QQ:1032694760, Email:liyujiang_tk@yeah.net
