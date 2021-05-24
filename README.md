# 项目说明

> 2020.05.14

## 开发环境

- JDK 1.8
- Android Studio Latest Version
- Android SDK 30
- Gradle Latest Version
- Plugin@FindViewByME
- Plugin@GsonFormatPlus

## 协作工具

- xiaopiu：产品原型协作
- 蓝湖：UI 设计稿协作
- git：程序代码协作

## 模板文件

- .github/workflows 自动化工作流
- .gitignore 通用的 GIT 版本控制文件忽略规则
- build.gradle 项目构建管理
- gradle.properties 通用的项目配置
- app/proguard-common.pro 通用的混淆规则
- gradle/common.gradle 通用的项目配置，包括 GIT 提交信息、打包排除文件、静态检查白名单等
- gradle/config.gradle 构建配置，包括指定 SDK 版本、是否启用 X86 支持、是否启用混淆
- gradle/dependency.gradle 第三方依赖项集中管理
- gradle/git.gradle 基于 GIT 进行版本号得到 APP 版本号
- gradle/publish.gradle 项目打包发布到 Maven 仓库
- gradle/app.gradle 通用的 APK 打包配置，包括解决依赖冲突、分环境分渠道打包、分包、自动签名、归档备份等
- gradle/library.gradle 通用的库项目配置

## 镜像加速

在天朝使用 jcenter、mavenCentral 及 google 三个远程仓库，Gradle Sync 会很慢，
google 仓库甚至需要`科学上网`才能访问。
为了加快速度，优先用 [阿里云仓库服务](https://maven.aliyun.com/mvn/view) 的仓库作为下载源，
将本项目的[gradle/init.d/init.gradle](/gradle/init.d/init.gradle)复制到`{USER_HOME}/.gradle/`下即可，
`USER_HOME`在 Windows 上大约为`C:/Users/liyujiang/.gradle/`，在 Linux 上大约为`/home/liyujiang/.gradle/`，  
`init.d/init.gradle`内容为：

```gradle
buildscript {
    repositories {
        maven { url 'https://maven.aliyun.com/repositories/jcenter' }
        maven { url 'https://maven.aliyun.com/repositories/google' }
        maven { url 'https://maven.aliyun.com/repository/gradle-plugin' }
    }
}

allprojects {
    repositories {
        maven { url 'https://maven.aliyun.com/repositories/jcenter' }
        maven { url 'https://maven.aliyun.com/repositories/google' }
        maven { url 'https://maven.aliyun.com/repository/central' }
        maven { url "https://www.jitpack.io" }
    }
}
```

## 远程真机

- 免费 [华为远程真机云调试](https://developer.huawei.com/consumer/cn/agconnect/cloud-adjust) 。
- 免费 [小米云测平台远程真机租用](https://testit.miui.com/remote) 。
- 免费 [VIVO 云测平台远程真机](https://vcl.vivo.com.cn/#/machine/picking) 。
- 免费 [OPPO 云测平台远程真机](https://open.oppomobile.com/cloudmachine/device/list-plus) 。
- 免费 [三星远程开发测试平台真机调试](http://samsung.smarterapps.cn/index.php?app=home&mod=Index&act=samsung) 。
- 新人试用 ~~腾讯 WeTest 云真机调试、阿里 EMAS 移动测试远程真机、百度 MTC 远程真机调试、Testin 远程真机测试、AllTesting 真机测试~~ 。

## 架构模式

模块化/组件化。

- 通过模块化/组件化组织代码，面向接口编程，尽可能做到高内聚、低耦合、重复用。
- 模块可分为多种类型，一般分为：基础库（日志打印、网络请求、图片加载等）、三方包（微信登录、统计分析、消息推送等）、业务组件（会员中心、商城等）。
- 常见组件间通信方式：直接依赖（耦合太重，不推荐）、事件或广播（难以溯源，不推荐）、路由（如 @alibaba/ARouter）、面向接口（推荐）。
- 模块化/组件化业务独立，每个业务作为单独的组件，代码实现分离，不会搅在一起。
- 模块化/组件化便于协作，每个开发人员只关心自己负责的模块/组件，每个模块/组件作为一个子工程，没有太多的耦合。
- 模块化/组件化便于维护，各模块/组件管理自己的代码、布局、资源，主工程可以方便添加与移除。

## 设计模式

MVVM：Model-View-ViewModel。使用谷歌架构组件`ViewModel`及`LiveData`或`DataBinding`实现。

## 许可协议

```text
Copyright (c) 2016-present 贵州纳雍穿青人李裕江<1032694760@qq.com>

The software is licensed under the Mulan PSL v2.
You can use this software according to the terms and conditions of the Mulan PSL v2.
You may obtain a copy of Mulan PSL v2 at:
    http://license.coscl.org.cn/MulanPSL2
THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
PURPOSE.
See the Mulan PSL v2 for more details.
```
