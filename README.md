# About
安卓选择器类库，包括日期时间选择器、一二三级联动选择器、颜色选择器、文件目录选择器。
Picker for android, include date&amp;time/option/color/file&amp;directory.

# Install
1、通过SVN或GIT工具下载本项目，复制“framework-picker”到你的电脑上；
2、用Android Studio或IntelliJ IDEA打开你的项目，New->Import Module，选择“framework-picker”；
3、修改你的项目的build.gradle，加入依赖项“framework-picker”，如：
```
dependencies {
    compile 'com.android.support:support-v4:+'
    compile project(':framework-picker')
}
```

# Simple
日期选择器：   
```java
        DateTimePicker picker = new DateTimePicker(this);
        picker.setMode(DateTimePicker.Mode.YEAR_MONTH_DAY);
        picker.setRange(1990, 2015);
        picker.setSelectedDate(1999, 10, 11);
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
https://code.google.com/p/android-wheel   
https://github.com/saiwu-bigkoo/PickerView   
https://github.com/jbruchanov/AndroidColorPicker   
https://github.com/JoanZapata/base-adapter-helper   

# Screenshots
![](/screenshots/datepicker.jpg)   
![](/screenshots/timepicker.jpg)   
![](/screenshots/1optionpicker.jpg)   
![](/screenshots/2optionpicker.jpg)   
![](/screenshots/colorpicker.jpg)   
![](/screenshots/filepicker.jpg)   
![](/screenshots/dirpicker.jpg)   

# Contact
李玉江, QQ:1032694760, Email:liyujiang_tk@yeah.net
