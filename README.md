# Summary
安卓选择器类库，包括日期时间选择器、一二三级联动选择器、城市选择器、颜色选择器、文件目录选择器、数字选择器。
Picker for android, include date&amp;time/option/color/file&amp;directory/number.

# Install
1、通过SVN或GIT工具下载本项目，复制“framework-picker”到你的电脑上；<br />
2、用Android Studio或IntelliJ IDEA打开你的项目，New->Import Module，选择“framework-picker”；<br />
3、修改你的项目的build.gradle，加入依赖项“framework-picker”，如：
```
dependencies {
    compile 'com.android.support:support-v4:+'
    compile project(':framework-picker')
}
```   
注：   
gradle为1.3.0，buildTools为23.0.1，sdk为23。   
滑轮选择器的名称较长时，将以“...”显示超出的部分。      
支持android2.2+，项目编码为UTF-8。   

# Sample
日期选择器：   
```java   
        DateTimePicker picker = new DateTimePicker(this);
        picker.setMode(DateTimePicker.Mode.YEAR_MONTH_DAY);
        picker.setRange(1990, 2015);
        picker.setSelectedDate(1990, 11, 4);
        picker.setOnWheelListener(new WheelPicker.OnWheelListener<Date>() {
            @Override
            public void onSubmit(Date result) {
                
            }
        });
        picker.showAtBottom();
```

时间选择器：   
```java   
        DateTimePicker picker = new DateTimePicker(this);
        picker.setMode(DateTimePicker.Mode.HOUR_MINUTE);
        picker.setOnWheelListener(new WheelPicker.OnWheelListener<Date>() {
            @Override
            public void onSubmit(Date result) {
                
            }
        });
        picker.showAtBottom();
```

单项选择器（可用于性别、学历、职业等选择）：   
```java   
        OptionPicker picker = new OptionPicker(this);
        picker.setScrollingDuration(100);
        final String[] sex = {"男", "女", "保密"};
        picker.setOptions(sex);
        picker.setCurrentOptions(2);
        picker.setOnWheelListener(new WheelPicker.OnWheelListener<int[]>() {
            @Override
            public void onSubmit(int[] result) {
                String result = sex[result[0]];
            }
        });
        picker.showAtBottom();
```

二级联动选择器：   
```java   
        final ArrayList<String> option1 = new ArrayList<String>();
        option1.add("技术方案");
        option1.add("开发工具");
        option1.add("运行环境");
        option1.add("这里测试很长很长的内容，看看二级联动选择器如何显示");
        final ArrayList<ArrayList<String>> option2 = new ArrayList<ArrayList<String>>();
        ArrayList<String> language = new ArrayList<String>();
        language.add("Java/XML");
        language.add("PHP/MySQL");
        language.add("H5+/MUI");
        option2.add(language);
        ArrayList<String> tool = new ArrayList<String>();
        tool.add("Android Studio");
        tool.add("PhpStorm");
        tool.add("HBuilder");
        option2.add(tool);
        ArrayList<String> environment = new ArrayList<String>();
        environment.add("Android");
        environment.add("WAMP/LAMP");
        environment.add("Android/IOS");
        option2.add(environment);
        ArrayList<String> length = new ArrayList<String>();
        length.add("这里测试很长很长的内容，看看二级联动选择器如何显示");
        option2.add(length);
        OptionPicker picker = new OptionPicker(this);
        picker.setOptions(option1, option2);
        picker.setCurrentOptions(1);
        picker.setOnWheelListener(new WheelPicker.OnWheelListener<int[]>() {
            @Override
            public void onSubmit(int[] result) {
                String result = option1.get(result[0]) + "-" + option2.get(result[0]).get(result[1]);
            }
        });
        picker.showAtBottom();
```

三级联动选择器(参见城市选择器@CityPicker)   

颜色选择器：
```java   
        ColorPicker picker = new ColorPicker(this);
        picker.setInitColor(0xDD00DD);
        picker.setOnColorPickListener(new ColorPicker.OnColorPickListener() {
            @Override
            public void onColorPicked(int pickedColor) {
            
            }
        });
        picker.showAtBottom();
```

文件选择器：
```java   
        FilePicker picker = new FilePicker(this);
        picker.setMode(FilePicker.Mode.File);
        picker.setShowHideDir(false);
        picker.setInitPath(Common.getRootPath(this) + "Download/");
        //picker.setAllowExtensions(new String[]{".apk"});
        picker.setOnFilePickListener(new FilePicker.OnFilePickListener() {
            @Override
            public void onFilePicked(String currentPath) {
            
            }
        });
        picker.showAtBottom();
```

目录选择器：
```java   
        FilePicker picker = new FilePicker(this);
        picker.setMode(FilePicker.Mode.Directory);
        picker.setOnFilePickListener(new FilePicker.OnFilePickListener() {
            @Override
            public void onFilePicked(String currentPath) {
            
            }
        });
        picker.showAtBottom();
```
数字选择器(可用于身高、体重、年龄等选择)：
```java   
        NumberPicker picker = new NumberPicker(this);
        picker.setRange(145, 200);
        picker.setSelectedNumber(172);
        picker.setLabel("cm");
        picker.setOnWheelListener(new WheelPicker.OnWheelListener<Integer>() {
            @Override
            public void onSubmit(Integer result) {

            }
        });
        picker.showAtBottom();
```
城市选择器：
```java   
        CityPicker picker = new CityPicker(activity);
        picker.setSelectedCity("贵州", "毕节", "纳雍");
        picker.setOnCityPickListener(new CityPicker.OnCityPickListener() {
            @Override
            public void onCityPicked(String province, String city, String county) {
                
            }
        });
        picker.showAtBottom();
```

# Thanks
修改了使用以下项目：<br />
https://code.google.com/p/android-wheel<br />
https://github.com/saiwu-bigkoo/PickerView<br />
https://github.com/jbruchanov/AndroidColorPicker<br />
https://github.com/JoanZapata/base-adapter-helper<br />
直接使用了以下项目：<br />
https://github.com/alibaba/fastjson<br />

# Screenshots
![日期选择器效果图](/screenshots/datepicker.jpg)   
![时间选择器效果图](/screenshots/timepicker.jpg)   
![单项选择器效果图](/screenshots/1optionpicker.jpg)   
![二级联动选择器效果图](/screenshots/2optionpicker.jpg)   
![颜色选择器效果图](/screenshots/colorpicker.jpg)   
![文件选择器效果图](/screenshots/filepicker.jpg)   
![目录选择器效果图](/screenshots/dirpicker.jpg)   
![城市选择器效果图](/screenshots/citypicker.jpg)   

# Contact
李玉江, QQ:1032694760, Email:liyujiang_tk@yeah.net
