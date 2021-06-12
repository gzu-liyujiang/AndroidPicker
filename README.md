# AndroidPicker

[![API 19+](https://img.shields.io/badge/API-19%2B-green.svg)](https://github.com/gzu-liyujiang/AndroidPicker)
![Release APK](https://github.com/gzu-liyujiang/AndroidPicker/workflows/Release%20APK/badge.svg)

安卓选择器类库，包括日期及时间选择器（可用于出生日期、营业时间等）、单项选择器（可用于性别、民族、职业、学历、星座等）、二三级联动选择器（可用于车牌号、基金定投日期等）、城市地址选择器（分省级、地市级及区县级）、数字选择器（可用于年龄、身高、体重、温度等）、日历选日期择器（可用于酒店及机票预定日期）、颜色选择器、文件及目录选择器等……

欢迎大伙儿在[Issues](https://github.com/gzu-liyujiang/AndroidPicker/issues)提交你的意见或建议。欢迎 Fork & Pull requests 贡献您的代码，大家共同学习【[AndroidPicker 交流群 604235437](https://jq.qq.com/?_wv=1027&k=42bKOeD)】。

- GitHub：https://github.com/gzu-liyujiang/AndroidPicker
- 码云(GitEE)：https://gitee.com/li_yu_jiang/AndroidPicker

## 接入指引

最新版本：[![jitpack](https://jitpack.io/v/gzu-liyujiang/AndroidPicker.svg)](https://jitpack.io/#gzu-liyujiang/AndroidPicker) （[更新日志](/ChangeLog.md)）

### 注意事项

- 3.0.0 开始完全重构了底层代码，改进了性能，对 XML 布局更友好， 3.x 版本 的 API 和 1.x 及 2.x 版本的不大一样，**请请谨慎升级**。
- [1.x Support 版本封存分支](https://github.com/gzu-liyujiang/AndroidPicker/tree/1.x-support)
- [2.0 androidx 版本封存分支](https://github.com/gzu-liyujiang/AndroidPicker/tree/2.0-androidx)

### 依赖配置

```groovy
allprojects {
    repositories {
        maven { url 'https://www.jitpack.io' }
    }
}
```

所有选择器的基础窗体：

```groovy
dependencies {
    implementation 'com.github.gzu-liyujiang:AndroidPicker:Common:<version>'
}
```

滚轮选择器的滚轮控件：

```groovy
dependencies {
   implementation 'com.github.gzu-liyujiang:AndroidPicker:WheelView:<version>'
}
```

单项/数字、二三级联动、日期/时间等滚轮选择器：

```groovy
dependencies {
    implementation 'com.github.gzu-liyujiang:AndroidPicker:Common:<version>'
    implementation 'com.github.gzu-liyujiang:AndroidPicker:WheelView:<version>'
    implementation 'com.github.gzu-liyujiang:AndroidPicker:WheelPicker:<version>'
}
```

省市区地址选择器：

```groovy
dependencies {
    implementation 'com.github.gzu-liyujiang:AndroidPicker:Common:<version>'
    implementation 'com.github.gzu-liyujiang:AndroidPicker:WheelView:<version>'
    implementation 'com.github.gzu-liyujiang:AndroidPicker:WheelPicker:<version>'
    implementation 'com.github.gzu-liyujiang:AndroidPicker:AddressPicker:<version>'
}
```

文件/目录选择器：

```groovy
dependencies {
    implementation 'com.github.gzu-liyujiang:AndroidPicker:Common:<version>'
    implementation 'com.github.gzu-liyujiang:AndroidPicker:FilePicker:<version>'
}
```

颜色选择器：

```groovy
dependencies {
    implementation 'com.github.gzu-liyujiang:AndroidPicker:Common:<version>'
    implementation 'com.github.gzu-liyujiang:AndroidPicker:ColorPicker:<version>'
}
```

日历日期选择器：

```groovy
dependencies {
    implementation 'com.github.gzu-liyujiang:AndroidPicker:Common:<version>'
    implementation 'com.github.gzu-liyujiang:AndroidPicker:CalendarPicker:<version>'
}
```

旧版本 **Support 稳定版本**：

```groovy
dependencies {
    implementation 'com.github.gzu-liyujiang.AndroidPicker:Common:1.5.6.20181018'
    implementation 'com.github.gzu-liyujiang.AndroidPicker:WheelPicker:1.5.6.20181018'
    implementation 'com.github.gzu-liyujiang.AndroidPicker:FilePicker:1.5.6.20181018'
    implementation 'com.github.gzu-liyujiang.AndroidPicker:ColorPicker:1.5.6.20181018'
}
```

旧版本 **AndroidX 稳定版本**：

```groovy
dependencies {
    implementation 'com.github.gzu-liyujiang.AndroidPicker:Common:2.0.0'
    implementation 'com.github.gzu-liyujiang.AndroidPicker:WheelPicker:2.0.0'
    implementation 'com.github.gzu-liyujiang.AndroidPicker:FilePicker:2.0.0'
    implementation 'com.github.gzu-liyujiang.AndroidPicker:ColorPicker:2.0.0'
}
```

## 混淆规则

项目库混淆无需额外配置。

## 用法示例

常见用法参阅 [demo](/app)，建议拉取代码运行，对比查看实际效果。

## 效果预览

以下图片显示的效果可能已修改过，实际效果请运行 demo 查看。

- ![效果图](/screenshots/1.gif)
- ![效果图](/screenshots/2.gif)
- ![效果图](/screenshots/3.gif)
- ![效果图](/screenshots/4.gif)
- ![效果图](/screenshots/5.gif)
- ![效果图](/screenshots/6.gif)
- ![效果图](/screenshots/7.gif)
- ![效果图](/screenshots/8.gif)

## 特别鸣谢

- [基于 View 的 WheelView](https://github.com/weidongjian/androidWheelView)
- [基于 ListView 的 WheelView](https://github.com/venshine/WheelView)
- [基于 ScrollView 的 WheelView](https://github.com/wangjiegulu/WheelView)
- [SingleDateAndTimePicker](https://github.com/florent37/SingleDateAndTimePicker)
- [China_Province_City](https://github.com/small-dream/China_Province_City)
- [AndroidColorPicker](https://github.com/jbruchanov/AndroidColorPicker)
- [calendar](https://github.com/oxsource/calendar)

## 许可协议

```text
Copyright (c) 2019-2021 gzu-liyujiang <1032694760@qq.com>

The software is licensed under the Mulan PSL v2.
You can use this software according to the terms and conditions of the Mulan PSL v2.
You may obtain a copy of Mulan PSL v2 at:
    http://license.coscl.org.cn/MulanPSL2
THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
PURPOSE.
See the Mulan PSL v2 for more details.
```
