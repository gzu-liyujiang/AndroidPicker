# Summary
[![API 14+](https://img.shields.io/badge/API-14%2B-green.svg)](https://github.com/gzu-liyujiang/AndroidPicker)
[![Download](https://api.bintray.com/packages/gzu-liyujiang/maven/WheelPicker/images/download.svg)](http://jcenter.bintray.com/cn/qqtheme/framework/)
[![JitPack](https://jitpack.io/v/gzu-liyujiang/AndroidPicker.svg)](https://jitpack.io/#gzu-liyujiang/AndroidPicker)
[![Build Status](https://travis-ci.org/gzu-liyujiang/AndroidPicker.svg?branch=master)](https://travis-ci.org/gzu-liyujiang/AndroidPicker)

安卓选择器类库，包括日期及时间选择器（可设置范围）、单项选择器（可用于性别、职业、学历、星座等）、城市地址选择器（分省级、地级及县级）、数字选择器（可用于年龄、身高、体重、温度等）、颜色选择器、文件及目录选择器等……
欢迎大伙儿在[Issues](https://github.com/gzu-liyujiang/AndroidPicker/issues)提交你的意见或建议。    
欢迎Fork & Pull requests贡献您的代码，大家共同学习【[AndroidPicker交流群 604235437](https://jq.qq.com/?_wv=1027&k=42bKOeD)】。
[查看更新日志](https://github.com/gzu-liyujiang/AndroidPicker/blob/master/ChangeLog.md)，新版本可能未对旧版API作兼容处理，升级后若编译报错请根据错误提示更改。

# Install
“app”是测试用例；“library”包括WheelPicker、ColorPicker、FilePicker，
WheelPicker包括DatePicker、TimePicker、OptionPicker、LinkagePicker、AddressPicker、NumberPicker、CarNumberPicker等。
#### ~~懒人建议直接远程加载jcenter里的~~
WheelPicker、FilePicker及ColorPicker是独立的，需要用哪个就compile哪个。
latest.release表示使用最新版，也可以[参照此处指定具体的版本号](https://github.com/gzu-liyujiang/AndroidPicker/releases)，1.3.x之前的版本基于ScrollView，1.4.x之后的版本基于ListView：
```groovy
dependencies {
    compile 'cn.qqtheme.framework:WheelPicker:版本号'
    compile 'cn.qqtheme.framework:FilePicker:版本号'
    compile 'cn.qqtheme.framework:ColorPicker:版本号'
}
```
如果出现“All com.android.support libraries must use the exact same version specification”这种错误，请将依赖改为：
```
compile('cn.qqtheme.framework:WheelPicker:版本号') {
    exclude group: 'com.android.support'
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
直接[下载AndroidPicker的jar包](/app/libs/)复制到你的项目的libs下即可。

# ProGuard
由于地址选择器使用了[fastjson](https://github.com/alibaba/fastjson)来解析，混淆时候需要加入以下类似的规则，不混淆Province、City等实体类。
```
-keepattributes InnerClasses,Signature
-keepattributes *Annotation*

-keep class cn.qqtheme.framework.entity.** { *;}
```

# Sample （更多用法详见示例项目）
继承自定义扩展选择器：
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
        final CarNumberPicker picker = new CarNumberPicker(this);
        picker.setOnWheelListener(new CarNumberPicker.OnWheelListener() {
            @Override
            public void onFirstWheeled(int index, String item) {
                textView.setText(item + ":" + picker.getSelectedSecondItem());
            }

            @Override
            public void onSecondWheeled(int index, String item) {
                textView.setText(picker.getSelectedFirstItem() + ":" + item);
            }
        });
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.wheelview_container);
        viewGroup.addView(picker.getContentView());
```
选择器各个设置项：
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
        picker.setCycleDisable(true);//禁用循环
        picker.setLineConfig(config);
        picker.setTopHeight(50);//顶部标题栏高度
        picker.setTopLineColor(0xFF33B5E5);//顶部标题栏下划线颜色
        picker.setTopLineHeight(1);//顶部标题栏下划线高度
        picker.setTitleText(isChinese ? "请选择" : "Please pick");
        picker.setTitleTextColor(0xFF999999);//顶部标题颜色
        picker.setTitleTextSize(12);//顶部标题文字大小
        picker.setCancelTextColor(0xFF33B5E5);//顶部取消按钮文字颜色
        picker.setCancelTextSize(14);
        picker.setSubmitTextColor(0xFF33B5E5);//顶部确定按钮文字颜色
        picker.setSubmitTextSize(14);
        picker.setTextColor(0xFFEE0000, 0xFF999999);//中间滚动项文字颜色
        WheelView.LineConfig config = new WheelView.LineConfig();
        config.setColor(0xFFEE0000);//线颜色
        config.setAlpha(140);//线透明度
        picker.setLineConfig(config);
        picker.setBackgroundColor(0xFFE1E1E1);
        //picker.setSelectedItem(isChinese ? "射手" : "Sagittarius");
        picker.setSelectedIndex(10);//默认选中项
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                showToast("index=" + index + ", item=" + item);
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
![城市选择器效果图](/screenshots/city.png)
![数字选择器效果图](/screenshots/number.gif)
![星座选择器效果图](/screenshots/constellation.jpg)
![颜色选择器效果图](/screenshots/color.gif)
![文件选择器效果图](/screenshots/file.gif)
![目录选择器效果图](/screenshots/dir.png)

# Contact
<a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=1032694760&site=穿青人&menu=yes"><img border="0" src="http://wpa.qq.com/pa?p=2:1032694760:51" alt="点击这里给我发消息" title="点击这里给我发消息"/></a>
<a target="_blank" href="http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=q8fC0t7BwsrFzIXfwOva2oXIxMY" style="text-decoration:none;"><img src="http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_02.png"/></a>

