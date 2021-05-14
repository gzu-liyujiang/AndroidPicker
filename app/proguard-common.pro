####################################################################################################
# 系统通用混淆配置，参见 android-sdk/tools/proguard/proguard-android-optimize.txt
####################################################################################################
# ProGuard的介绍以及参数说明，参阅 https://blog.csdn.net/hibit521/article/details/79709659
# -keep 指定需要被保留的类和成员。
# -keepclassmembers 指定需要被保留的类成员，如果他们的类也有被保留。如保留一个序列化类中的所有成员和方法
# -keepclasseswithmembers 指定保留那些含有指定类成员的类，如保留所有包含main方法的类
# -keepnames 指定那些需要被保留名字的类和类成员，如保留那些实现了Serializable接口的类的名字
# -keepclassmembernames 指定那些希望被保留的类成员的名字
# -keepclasseswithmembernames 保留含有指定名字的类和类成员。
# class可以指向任何接口或类。interface只能指向接口，enum只能指向枚举
# 接口或者枚举前面的!表示相对应的非枚举或者接口
# 每个类都必须是全路径指定或者由?、*、**这三个通配符指定
# 类通配符：?任意匹配类名中的一个字符，但是不匹配包名分隔符
# 类通配符：*匹配类名的任何部分除了包名分隔符
# 类通配符：**匹配所有类名的所有部分，包括报名分隔符
# <init>匹配任何构造函数，<fields>匹配任何域，<methods>匹配任何方法，*匹配任何方法和域
# 方法和域通配符：?任意匹配方法名中的单个字符，*匹配方法命中的任意部分
# 数据类型通配符：%匹配任何原生类型，?任意匹配单个字符，*匹配类名的任何部分除了包名分隔符
# 数据类型通配符：**匹配所有类名的所有部分，包括报名分隔符，***匹配任何类型，…匹配任意参数个数
####################################################################################################

-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/* # 谷歌推荐的混淆算法及过滤器，一般不做更改
-allowaccessmodification # 混淆时是否允许改变作用域
-optimizationpasses 5 # 指定代码的压缩级别
-ignorewarnings # 忽略警告
-verbose # 混淆时是否记录日志（混淆后生产映射文件，包含有类名->混淆后类名的映射关系)
-dontpreverify # 不做预校验，preverify是混淆步骤之一，Android不需要preverify，去掉以便加快混淆速度。
#-dontoptimize # 不优化输入的类文件，需要优化才能过滤代码中的日志
#-dontshrink # 不启用压缩，需要压缩才能移除未用到的资源
-dontusemixedcaseclassnames # 混淆时不使用大小写混合，混淆后的类名为小写
-dontskipnonpubliclibraryclasses # 指定不去忽略非公共库的类
-dontskipnonpubliclibraryclassmembers # 指定不去忽略非公共库的类成员

-printseeds proguard/seeds.txt # 未混淆的类和成员
-printusage proguard/unused.txt # 列出从 apk 中删除的代码
-printmapping proguard/mapping.txt # 混淆前后的映射
#-printconfiguration proguard/configuration.txt # 混淆规则融合

-keepattributes Exceptions # 保留异常类
-keepattributes InnerClasses # 保留匿名内部类
-keepattributes SourceFile,LineNumberTable # 保留代码行号
-renamesourcefileattribute SourceFile
-keepattributes Signature,EnclosingMethod #保留泛型与反射

 # 不混淆注解
-keepattributes *Annotation*
-keep class * extends java.lang.annotation.Annotation { *; }
-keep interface * extends java.lang.annotation.Annotation { *; }

# 不混淆明确通过`@Keep`注解标记的类、方法或属性
-keep class android.support.annotation.Keep
-keep @android.support.annotation.Keep class * {*;}
-keepclasseswithmembers class * {
    @android.support.annotation.Keep <methods>;
}
-keepclasseswithmembers class * {
    @android.support.annotation.Keep <fields>;
}
-keepclasseswithmembers class * {
    @android.support.annotation.Keep <init>(...);
}
-keep class androidx.annotation.Keep
-keep @androidx.annotation.Keep class * {*;}
-keepclasseswithmembers class * {
    @androidx.annotation.Keep <init>(...);
}
-keepclasseswithmembers class * {
    @androidx.annotation.Keep <methods>;
}
-keepclasseswithmembers class * {
    @androidx.annotation.Keep <fields>;
}

# 不混淆继承自安卓的四大组件的类名
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Dialog
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class com.google.vending.licensing.ILicensingService
-keep public class * extends android.view.View
-keep public class * extends com.android.layoutlib.bridge.MockView
-keep public class * extends androidx.fragment.app.Fragment

# 不混淆布局文件中为控件配置的onClick方法
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

# 不混淆C/C++底层库暴露的方法
# see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
    native <methods>;
}

# 不混淆所有View的子类及其子类的get、set方法
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}
-keep public class * extends android.view.View {
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 不混淆资源索引类，以便通过资源名称获取资源
-keep class **.R$*
-keepclassmembers class **.R$* {
    public static <fields>;
}
# 不混淆构建配置类，以便通过反射字段获取值
-keep class **.BuildConfig
-keepclassmembers class **.BuildConfig {
    public static <fields>;
}

# 不混淆带有回调函数的**Event、**Listener
-keepclassmembers class * {
    void *(**Event);
    void *(**Listener);
    void gt**;
}

# 不混淆JS于Java的交互桥梁
-keepattributes *JavascriptInterface*
# 如果你的项目中用到了WebView的复杂操作 ，最好加入，对WebView简单说明下:
# 经过实战检验，做腾讯QQ登录，如果引用他们提供的jar，若不加防止WebChromeClient混淆的代码，OAuth认证无法回调，
# 反编译代码后可看到他们有用到WebChromeClient。
-keepclassmembers class * extends android.webkit.WebViewClient {
     public void *(android.webkit.WebView,java.lang.String,android.graphics.Bitmap);
     public boolean *(android.webkit.WebView,java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebChromeClient {
     public void *(android.webkit.WebView,java.lang.String);
}
-keep public class * extends android.webkit.WebView

# 不混淆枚举类
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 不混淆实体类，需要通过JSON进行序列化和反序列化的类（注: Java反射用到的类也不能被混淆）
-keep class * implements android.os.Parcelable { *; }
-keepclassmembers class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator CREATOR;
}
-keepnames class * implements java.io.Serializable
-keep public class * implements java.io.Serializable {
   public *;
}
-keep public class * implements java.io.Serializable {
   public *;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

# 不混淆AndroidX及Material，以便使用反射进行破解
-dontwarn android.support.**
-dontwarn androidx.**
-dontwarn com.google.android.material.**
-keep class androidx.** {*;}
-keep public class * extends androidx.**
-keep interface androidx.** {*;}
-keep class android.support.** {*;}
-keep public class * extends android.support.**
-keep interface android.support.** {*;}
-keep class com.google.android.material.** {*;}

# XML映射问题，一些控件无法Inflate
-keep class org.xmlpull.v1.** {*;}

# 删除控制台日志打印
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}
-assumenosideeffects class java.io.PrintStream {
    public *** println(...);
    public *** print(...);
}

# okhttp3
-dontwarn okhttp3.**
-dontwarn okio.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
