# About
安卓选择器类库，包括日期时间选择器、一二三级联动选择器、颜色选择器、文件目录选择器。
Picker for android, include date&amp;time/option/color/file&amp;directory.

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
注：滑轮选择器名称较长时排版混乱问题还未得到很好解决。API级别>=8，项目编码为UTF-8。

# Simple
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

单项选择器：   
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

三级联动选择器：
```java
        final ArrayList<String> option1 = new ArrayList<String>();
        option1.add("贵州");
        option1.add("北京");
        final ArrayList<ArrayList<String>> option2 = new ArrayList<ArrayList<String>>();
        ArrayList<String> options2_1 = new ArrayList<String>();
        options2_1.add("贵阳");
        options2_1.add("毕节");
        ArrayList<String> options2_2 = new ArrayList<String>();
        options2_2.add("北京");
        option2.add(options2_1);
        option2.add(options2_2);
        final ArrayList<ArrayList<ArrayList<String>>> option3 = new ArrayList<ArrayList<ArrayList<String>>>();
        ArrayList<ArrayList<String>> option3_1 = new ArrayList<ArrayList<String>>();
        ArrayList<String> option3_1_1 = new ArrayList<String>();
        option3_1_1.add("花溪");
        option3_1_1.add("南明");
        option3_1_1.add("金阳");
        option3_1.add(option3_1_1);
        ArrayList<String> option3_1_2 = new ArrayList<String>();
        option3_1_2.add("这里测试很长很长的文字");
        option3_1_2.add("七星关");
        option3_1_2.add("纳雍");
        option3_1.add(option3_1_2);
        ArrayList<ArrayList<String>> option3_2 = new ArrayList<ArrayList<String>>();
        ArrayList<String> option3_2_1 = new ArrayList<String>();
        option3_2_1.add("北京");
        option3_2.add(option3_2_1);
        option3.add(option3_1);
        option3.add(option3_2);
        OptionPicker picker = new OptionPicker(this);
        picker.setLabels("省", "市", "县");
        picker.setOptions(option1, option2, option3);
        picker.setCurrentOptions(0, 1, 2);
        picker.setOnWheelListener(new WheelPicker.OnWheelListener<int[]>() {
            @Override
            public void onSubmit(int[] result) {
                String province = option1.get(result[0]);
                String city = option2.get(result[0]).get(result[1]);
                String district = option3.get(result[0]).get(result[1]).get(result[2]);
                String result = province + "-" + city + "-" + district;
            }
        });
        picker.showAtBottom();
```

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
        FilePicker picker = new FilePicker(this, FilePicker.Mode.File);
        picker.setShowHideDir(false);
        picker.setOnFilePickListener(new FilePicker.OnFilePickListener() {
            @Override
            public void onFilePicked(String currentPath) {
            
            }
        });
        picker.showAtBottom();
```

目录选择器：
```java
        FilePicker picker = new FilePicker(this, FilePicker.Mode.Directory);
        picker.setOnFilePickListener(new FilePicker.OnFilePickListener() {
            @Override
            public void onFilePicked(String currentPath) {
            
            }
        });
        picker.showAtBottom();
```

# Thanks
修改使用了以下项目：<br />
https://code.google.com/p/android-wheel<br />
https://github.com/saiwu-bigkoo/PickerView<br />
https://github.com/jbruchanov/AndroidColorPicker<br />
https://github.com/JoanZapata/base-adapter-helper<br />

# Screenshots
![](/screenshots/datepicker.jpg)   
![](/screenshots/timepicker.jpg)   
![](/screenshots/1optionpicker.jpg)   
![](/screenshots/2optionpicker.jpg)   
![](/screenshots/3optionpicker.jpg)   
![](/screenshots/colorpicker.jpg)   
![](/screenshots/filepicker.jpg)   
![](/screenshots/dirpicker.jpg)   

# Contact
李玉江, QQ:1032694760, Email:liyujiang_tk@yeah.net
