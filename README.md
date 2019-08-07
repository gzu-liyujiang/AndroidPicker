# Summary
[![API 14+](https://img.shields.io/badge/API-14%2B-green.svg)](https://github.com/gzu-liyujiang/AndroidPicker)
[![Download](https://api.bintray.com/packages/gzu-liyujiang/maven/WheelPicker/images/download.svg)](http://jcenter.bintray.com/cn/qqtheme/framework/)
[![JitPack](https://jitpack.io/v/gzu-liyujiang/AndroidPicker.svg)](https://jitpack.io/#gzu-liyujiang/AndroidPicker)
[![Build Status](https://travis-ci.org/gzu-liyujiang/AndroidPicker.svg?branch=master)](https://travis-ci.org/gzu-liyujiang/AndroidPicker)
[![996.icu](https://img.shields.io/badge/link-996.icu-red.svg)](https://996.icu)
[![LICENSE](https://img.shields.io/badge/license-Anti%20996-blue.svg)](https://github.com/996icu/996.ICU/blob/master/LICENSE)

安卓选择器类库，包括日期及时间选择器（可设置范围）、单项选择器（可用于性别、职业、学历、星座等）、城市地址选择器（分省级、地级及县级）、数字选择器（可用于年龄、身高、体重、温度等）、双项选择器、颜色选择器、文件及目录选择器等……
欢迎大伙儿在[Issues](https://github.com/gzu-liyujiang/AndroidPicker/issues)提交你的意见或建议。    
欢迎Fork & Pull requests贡献您的代码，大家共同学习【[AndroidPicker交流群 604235437](https://jq.qq.com/?_wv=1027&k=42bKOeD)】。
[查看更新日志](https://github.com/gzu-liyujiang/AndroidPicker/blob/master/ChangeLog.md)，新版本可能未对旧版API作兼容处理，升级后若编译报错请根据错误提示更改。
<br/>
正在重构2.x版本，感兴趣的小伙伴可以[点击此处查看](https://github.com/gzu-liyujiang/AndroidPicker/tree/refactor-2.0)
# Install
“app”是测试用例；“library”包括WheelPicker、ColorPicker、FilePicker、MultiplePicker，
WheelPicker包括DatePicker、TimePicker、OptionPicker、LinkagePicker、AddressPicker、NumberPicker、DoublePicker等。
其中WheelPicker、FilePicker及ColorPicker是独立的，需要用哪个就只依赖哪个，latest.release表示使用最新版，也可以[参照此处指定具体的版本号](https://github.com/gzu-liyujiang/AndroidPicker/releases)，
具体步骤如下：
第一步，在项目根目录下的build.gradle里加：
```
repositories {
    maven {
        url "https://jitpack.io"
    }
}
```
第二步，在项目的app模块下的build.gradle里加：
滚轮选择器：
```groovy
dependencies {
    compile('com.github.gzu-liyujiang.AndroidPicker:WheelPicker:版本号') {
        exclude group: 'com.android.support'
    }
}
```
文件目录选择器：
```groovy
dependencies {
    compile('com.github.gzu-liyujiang.AndroidPicker:FilePicker:版本号') {
        exclude group: 'com.android.support'
    }
}
```
颜色选择器：
```groovy
dependencies {
    compile('com.github.gzu-liyujiang.AndroidPicker:ColorPicker:版本号') {
        exclude group: 'com.android.support'
    }
}
```
使用Eclipse的话，直接[下载AndroidPicker的jar包](/app/libs/)复制到你的项目的libs下即可。

# ProGuard
由于地址选择器使用了[fastjson](https://github.com/alibaba/fastjson)来解析，混淆时候需要加入以下类似的规则，不混淆Province、City等实体类。
```
-keepattributes InnerClasses,Signature
-keepattributes *Annotation*

-keep class cn.qqtheme.framework.entity.** { *;}
```

# Sample （更多用法详见示例项目）
各种设置方法：
```java
picker.setXXX(...);
```   
如：    
设置选项偏移量，可用来要设置显示的条目数，范围为1-5，1显示3行、2显示5行、3显示7行……
```java
picker.setOffset(...);
```   
设置启用循环
```java
picker.setCycleDisable(false);
```   
设置每项的高度，范围为2-4
```java
picker.setLineSpaceMultiplier(...);
picker.setItemHeight(...);
```   
设置文字颜色、字号、字体等
```java
picker.setTextColor(...);
picker.setTextSize(...);
picker.setTextPadding(...);
picker.setTextSizeAutoFit(...);
picker.setTypeface(...);
```   
设置单位标签
```java
picker.setLabel(...);
picker.setOnlyShowCenterLabel(...))
```   
设置默认选中项
```java
picker.setSelectedItem(...);
picker.setSelectedIndex(...);
```   
设置滚轮项填充宽度，分割线最长
```java
picker.setUseWeight(true);
picker.setDividerRatio(WheelView.DividerConfig.FILL);
```   
设置触摸弹窗外面是否自动关闭
```java
picker.setCanceledOnTouchOutside(...);
```   
设置分隔线配置项，设置null将隐藏分割线及阴影
```java
picker.setDividerConfig(...);
picker.setDividerColor(...);
picker.setDividerRatio(...);
picker.setDividerVisible(...);
```   
设置内容边距
```java
picker.setContentPadding(...);
```   
设置选中项背景色
```java
picker.setShadowColor(...)
```   
自定义顶部及底部视图
```java
picker.setHeaderView(...);
picker.setFooterView(...);
```   
获得内容视图（不要调用picker.show()方法），可以将其加入到其他容器视图（如自定义的Dialog的视图）中
```java
picker.getContentView();
```   
获得按钮视图（需要先调用picker.show()方法），可以调用该视图相关方法，如setVisibility()
```java
picker.getCancelButton();
picker.getSubmitButton();
```   
自定义选择器示例：
```java
        CustomHeaderAndFooterPicker picker = new CustomHeaderAndFooterPicker(this);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int position, String option) {
                showToast(option);
            }
        });
        picker.show();
```
<font color="#FF0000">核心滚轮控件为WheelView，可以参照SinglePicker、DateTimePicker及LinkagePicker自行扩展。</font>

# Screenshots
以下图片显示的效果可能已修改过，实际效果请运行demo查看。   
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
![目录选择器效果图](/screenshots/dir.png)

# Thanks
[基于View的WheelView](https://github.com/weidongjian/androidWheelView)<br />
[基于ListView的WheelView](https://github.com/venshine/WheelView)<br />
[基于ScrollView的WheelView](https://github.com/wangjiegulu/WheelView)<br />


# Contact
<a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=1032694760&site=穿青人&menu=yes"><img border="0" src="http://wpa.qq.com/pa?p=2:1032694760:51" alt="点击这里给我发消息" title="点击这里给我发消息"/></a>
<a target="_blank" href="http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=q8fC0t7BwsrFzIXfwOva2oXIxMY" style="text-decoration:none;"><img src="http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_02.png"/></a>
