# 项目说明

>2020.05.14

## 开发环境

- JDK 1.8
- Android Studio 4.2.1
- Android SDK 30
- Gradle 6.7.1
- Plugin@FindViewByME
- Plugin@GsonFormatPlus

## 协作工具

- xiaopiu：产品原型协作
- lanhuapp：UI设计稿协作
- git：程序代码协作

## 远程真机

- 免费 [华为远程真机云调试](https://developer.huawei.com/consumer/cn/agconnect/cloud-adjust) 。
- 免费 [小米云测平台远程真机租用](https://testit.miui.com/remote) 。
- 免费 [VIVO 云测平台远程真机](https://vcl.vivo.com.cn/#/machine/picking) 。
- 免费 [OPPO 云测平台远程真机](https://open.oppomobile.com/cloudmachine/device/list-plus) 。
- 免费 [三星远程开发测试平台真机调试](http://samsung.smarterapps.cn/index.php?app=home&mod=Index&act=samsung) 。
- 新人试用 ~~腾讯 WeTest 云真机调试、阿里 EMAS 移动测试远程真机、百度 MTC 远程真机调试、Testin 远程真机测试、AllTesting 真机测试~~ 。

## 架构模式：模块化/组件化

- 通过模块化/组件化组织代码，面向接口编程，尽可能做到高内聚、低耦合、重复用。
- 模块可分为多种类型，一般分为：基础库（日志打印、网络请求、图片加载等）、三方包（微信登录、统计分析、消息推送等）、业务组件（APP外壳、会员中心、商城等）。
- 常见组件间通信方式：直接依赖（耦合太重，不推荐）、事件或广播（难以溯源，不推荐）、路由（如 ARouter）、面向接口（推荐）。

### 模块化/组件化的优势

- 结构清晰：业务独立，每个业务作为单独的组件，代码实现分离，不会搅在一起。
- 便于协作：每个开发人员只关心自己负责的模块/组件，每个模块/组件作为一个子工程，没有太多的耦合。
- 便于维护：各模块/组件管理自己的代码、布局、资源，主工程可以方便添加与移除。

## 设计模式：MVVM

使用谷歌架构组件：`LifeCycle`+`ViewModel`+`LiveData`+`Room`。
