# Summary
[![API 14+](https://img.shields.io/badge/API-14%2B-green.svg)](https://github.com/gzu-liyujiang/AndroidPicker)
[![Download](https://api.bintray.com/packages/gzu-liyujiang/maven/WheelPicker/images/download.svg)](http://jcenter.bintray.com/cn/qqtheme/framework/)
[![JitPack](https://jitpack.io/v/gzu-liyujiang/AndroidPicker.svg)](https://jitpack.io/#gzu-liyujiang/AndroidPicker)
[![Build Status](https://travis-ci.org/gzu-liyujiang/AndroidPicker.svg?branch=master)](https://travis-ci.org/gzu-liyujiang/AndroidPicker)
[![996.icu](https://img.shields.io/badge/link-996.icu-red.svg)](https://996.icu)
[![LICENSE](https://img.shields.io/badge/license-Anti%20996-blue.svg)](https://github.com/996icu/996.ICU/blob/master/LICENSE)

安卓选择器类库，包括日期及时间选择器（可用于出生日期、营业时间等）、单项选择器（可用于性别、职业、学历、星座等）、城市地址选择器（分省级、地级及县级）、数字选择器（可用于年龄、身高、体重、温度等）、双项选择器、颜色选择器、文件及目录选择器等……
欢迎大伙儿在[Issues](https://github.com/gzu-liyujiang/AndroidPicker/issues)提交你的意见或建议。    
欢迎Fork & Pull requests贡献您的代码，大家共同学习【[AndroidPicker交流群 604235437](https://jq.qq.com/?_wv=1027&k=42bKOeD)】。
[查看更新日志](https://github.com/gzu-liyujiang/AndroidPicker/blob/master/ChangeLog.md)，新版本可能未对旧版API作兼容处理，升级后若编译报错请根据错误提示更改。
<br/>
正在重构2.x版本，感兴趣的小伙伴可以[点击此处查看](https://github.com/gzu-liyujiang/AndroidPicker/tree/refactor-2.0)
# Install
[参照此处指定具体的版本号](https://github.com/gzu-liyujiang/AndroidPicker/releases)，具体步骤如下：    
第一步，在项目根目录下的`build.gradle`里加：
```groovy
repositories {
    maven {
        url "https://jitpack.io"
    }
}
```
第二步，在项目的app模块下的`build.gradle`里加：
滚轮选择器：
```groovy
dependencies {
    implementation 'com.github.gzu-liyujiang.AndroidPicker:WheelPicker:最新版本号'
}
```
文件目录选择器：
```groovy
dependencies {
    implementation 'com.github.gzu-liyujiang.AndroidPicker:FilePicker:最新版本号'
}
```
颜色选择器：
```groovy
dependencies {
    implementation 'com.github.gzu-liyujiang.AndroidPicker:ColorPicker:最新版本号'
}
```
日历选择器：
```groovy
dependencies {
    implementation 'com.github.gzu-liyujiang.AndroidPicker:CalendarPicker:最新版本号'
}
```
若`WheelPicker`、`FilePicker`、`ColorPicker`、`CalendarPicker`无法满足需求，可通过`WheelView`或`Popup`进行更灵活的自定义：    
滚轮控件：
```groovy
dependencies {
    api 'com.github.gzu-liyujiang.AndroidPicker:WheelView:最新版本号'
}
```
弹窗：
```groovy
dependencies {
    api 'com.github.gzu-liyujiang.AndroidPicker:Popup:最新版本号'
}
```

~~使用Eclipse的话，直接[下载AndroidPicker的相关的jar包](/app/libs/)复制到你的项目的libs下即可。~~

# ProGuard
若使用了地址选择器，需注意加入以下类似的规则，不混淆`Province`、`City`等实体类，以便能正确进行JSON序列化。
```
-keepattributes InnerClasses,Signature
-keepattributes *Annotation*

-keep class cn.qqtheme.framework.entity.** { *;}
```

# Sample （更多用法详见示例项目）
各种设置方法：
```text
picker.setXXX(...);
```   
如：    
设置选项偏移量，可用来要设置显示的条目数，范围为1-5，1显示3行、2显示5行、3显示7行……
```text
picker.setOffset(...);
```   
设置启用循环
```text
picker.setCycleDisable(false);
```   
设置每项的高度，范围为2-4
```text
picker.setLineSpaceMultiplier(...);
picker.setItemHeight(...);
```   
设置文字颜色、字号、字体等
```text
picker.setTextColor(...);
picker.setTextSize(...);
picker.setTextPadding(...);
picker.setTextSizeAutoFit(...);
picker.setTypeface(...);
```   
设置单位标签
```text
picker.setLabel(...);
picker.setOnlyShowCenterLabel(...))
```   
设置默认选中项
```text
picker.setSelectedItem(...);
picker.setSelectedIndex(...);
```   
设置滚轮项填充宽度，分割线最长
```text
picker.setUseWeight(true);
picker.setDividerRatio(WheelView.DividerConfig.FILL);
```   
设置触摸弹窗外面是否自动关闭
```text
picker.setCanceledOnTouchOutside(...);
```   
设置分隔线配置项，设置null将隐藏分割线及阴影
```text
picker.setDividerConfig(...);
picker.setDividerColor(...);
picker.setDividerRatio(...);
picker.setDividerVisible(...);
```   
设置内容边距
```text
picker.setContentPadding(...);
```   
设置选中项背景色
```text
picker.setShadowColor(...)
```   
自定义顶部及底部视图
```text
picker.setHeaderView(...);
picker.setFooterView(...);
```   
获得内容视图（不要调用picker.show()方法），可以将其加入到其他容器视图（如自定义的Dialog的视图）中
```text
picker.getContentView();
```   
获得按钮视图（需要先调用picker.show()方法），可以调用该视图相关方法，如setVisibility()
```text
picker.getCancelButton();
picker.getSubmitButton();
```   
自定义选择器示例：
```text
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
![日期选择器定制图](/screenshots/datetime_custom.png)
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
[SingleDateAndTimePicker](https://github.com/florent37/SingleDateAndTimePicker)<br />

# Contacts
<a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=1032694760&site=贵州穿青人&menu=yes"><img border="0" src="http://wpa.qq.com/pa?p=2:1032694760:51" alt="点击这里给我发消息" title="点击这里给我发消息"/></a>
<a target="_blank" href="http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=q8fC0t7BwsrFzIXfwOva2oXIxMY" style="text-decoration:none;"><img src="http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_02.png"/></a>
