-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}
-keep class * extends java.lang.annotation.Annotation {
    *;
}

#不混淆R.java
-keep class **.R$* {
    *;
}

#不混淆JavaScript和C/C++桥梁类
-keep public class cn.qqtheme.framework.tool.WebBrowser.JavaScriptBridge
-keep public class * extends cn.qqtheme.framework.tool.WebBrowser.JavaScriptBridge

#不混淆数据库实体类
-keep public class cn.qqtheme.framework.db.Entity
-keep public class * extends cn.qqtheme.framework.db.Entity {
    *;
}

#不混淆JavaBean类
-keep public class cn.qqtheme.framework.bean.JavaBean
-keep public class * extends cn.qqtheme.framework.bean.JavaBean {
    *;
}

#不检查某些第三方类库
-dontwarn android.support.**
-dontwarn com.tencent.**
-dontwarn org.dom4j.**
-dontwarn org.slf4j.**
-dontwarn org.http.mutipart.**
-dontwarn org.apache.**
-dontwarn org.apache.log4j.**
-dontwarn org.apache.commons.logging.**
-dontwarn org.apache.commons.codec.binary.**
-dontwarn weibo4android.**
-dontwarn net.youmi.android.**

#不混淆某些第三方类库
-keep class com.baidu.** { *; }
-keep class com.tencent.** { *; }
-keep class com.amap.api.** { *; }
-keep class com.aps.** { *; }
-keep class vi.com.gdi.bgl.android.java.** { *; }
-keep class org.acra.** { *; }
-keep class com.qq.e.** { *; }
-keep class com.tencent.gdt.**{ *; }
-keep class com.bpkg.m.** { *; }
-keep class net.youmi.android.** { *; }
-keep class com.sixth.adwoad.** { *; }
-keep class cn.domob.android.ads.** { *; }
