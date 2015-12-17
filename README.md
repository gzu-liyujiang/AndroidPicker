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
注：   
旧版的基于android-wheel，在分支“branch_OldAndroidPicker”，已放弃维护。   
不断学习，持续完善，敬请关注。。。   
gradle为1.3.0，buildTools为23.0.1，sdk为23。   
滑轮选择器的名称较长时，将以“...”显示超出的部分。      
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
        TimePicker picker = new TimePicker(this);
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
        OptionPicker picker = new OptionPicker(this, new String[]{"第一项", "第二项", "这是一个很长很长的选项"});
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
        picker.setRange(145, 200);//数字范围
        picker.setLabel("厘米");
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(String option) {
                showToast(option);
            }
        });
        picker.show();
```

地址选择器：
```java   
        ArrayList<AddressPicker.Province> data = new ArrayList<AddressPicker.Province>();
        String json = AssetsUtils.readText(activity, "city.json");
        data.addAll(JSON.parseArray(json, AddressPicker.Province.class));
        AddressPicker picker = new AddressPicker(activity, result);
        picker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
            @Override
            public void onAddressPicked(String province, String city, String county) {
                Toast.makeText(activity, province + city + county, Toast.LENGTH_LONG).show();
            }
        });
        picker.show();
```

颜色选择器：
```java   
        ColorPicker picker = new ColorPicker(this);
        picker.setInitColor(0xDD00DD);
        picker.setOnColorPickListener(new ColorPicker.OnColorPickListener() {
            @Override
            public void onColorPicked(int pickedColor) {
                showToast(Common.toColorString(pickedColor));
            }
        });
        picker.show();
```

文件选择器：
```java   
        FilePicker picker = new FilePicker(this);
        picker.setShowHideDir(false);
        picker.setRootPath(Common.getRootPath(this) + "Download/");
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

星座选择器：
```java   
        ConstellationPicker picker = new ConstellationPicker(this);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(String option) {
                showToast(option);
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
![数字选择器效果图](/screenshots/number.gif)    
![颜色选择器效果图](/screenshots/color.gif)    
![文件选择器效果图](/screenshots/file.gif)    
![目录选择器效果图](/screenshots/dir.gif)    
![星座选择器效果图](/screenshots/constellation.gif)    

# Contact
李玉江, QQ:1032694760, Email:liyujiang_tk@yeah.net
