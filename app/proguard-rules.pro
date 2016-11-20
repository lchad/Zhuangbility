

-dontwarn okio.**

-dontwarn org.w3c.dom.bootstrap.DOMImplementationRegistry

-dontwarn sun.misc.**

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}












#指定压缩级别
-optimizationpasses 5

#不跳过非公共的库的类成员
-dontskipnonpubliclibraryclassmembers

#混淆时采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

#把混淆类中的方法名也混淆了
-useuniqueclassmembernames

#优化时允许访问并修改有修饰符的类和类的成员
-allowaccessmodification

#将文件来源重命名为“SourceFile”字符串
-renamesourcefileattribute SourceFile
#保留行号
-keepattributes SourceFile,LineNumberTable

#保持所有实现 Serializable 接口的类成员
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#Fragment不需要在AndroidManifest.xml中注册，需要额外保护下
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.app.Fragment

# 保持测试相关的代码
-dontnote junit.framework.**
-dontnote junit.runner.**
-dontwarn android.test.**
-dontwarn android.support.test.**
-dontwarn org.junit.**


-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-ignorewarnings
-verbose

-keepattributes *Annotation*
-keepattributes Signature


#-libraryjars libs/asmack-android-19-0.8.10.jar
#-libraryjars libs/android-support-v4.jar

# 保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

# 保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclasseswithmembers class * {
    void onClick*(...);
}
-keepclasseswithmembers class * {
    *** *Callback(...);
}

# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

# 保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

# 保持枚举 enum 类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

# http client
-keep class org.apache.http.** {*; }
-keep class org.apache.**{*;}

# umeng message anysl
# 以下类过滤不混淆
-keep public class * extends com.umeng.**
# 以下包不进行过滤
-keep class com.umeng.** { *; }
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

-keep,allowshrinking class org.android.agoo.service.* {
    public <fields>;
    public <methods>;
}
-keep,allowshrinking class com.umeng.message.* {
    public <fields>;
    public <methods>;
}

-keep public class com.airi.buyue.R$*{
    public static final int *;
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep public class com.umeng.fb.ui.ThreadView {
}


# v4 包的混淆

#-libraryjars ./libs/android-support-v4.jar

#-dontwarn android.support.**

-dontwarn android.support.v4.**

-dontwarn **CompatHoneycomb

-dontwarn **CompatHoneycombMR2

-dontwarn **CompatCreatorHoneycombMR2

-keep interface android.support.v4.app.** { *; }

-keep class android.support.v4.** { *; }

-keep public class * extends android.support.v4.**

#alipay
-keep class com.alipay.android.app.**{*;}

-keepattributes Signature

-keepattributes *Annotation*

#-libraryjars src/main/ormlite-android-4.48.jar
#-libraryjars src/main/ormlite-core-4.48.jar

-dontwarn com.j256.**

-keep class com.j256.** { *; }
-keep class com.j256.ormlite.** { *; }

-keep public class * extends com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
-keep public class * extends com.j256.ormlite.android.apptools.OpenHelperManager
-keep public class * extends com.j256.ormlite.dao
-dontwarn com.j256.ormlite.android.**
-dontwarn com.j256.ormlite.dao.**
-dontwarn com.j256.ormlite.db.**
-dontwarn com.j256.ormlite.field.**
-dontwarn com.j256.ormlite.logger.**
-dontwarn com.j256.ormlite.misc.**
-dontwarn com.j256.ormlite.stmt.**
-dontwarn com.j256.ormlite.support.**
-dontwarn com.j256.ormlite.table.**
-dontwarn com.j256.ormlite.**
-dontwarn com.j256.ormlite.android.**
-dontwarn com.j256.ormlite.field.**
-dontwarn com.j256.ormlite.stmt.**

-keep class com.j256.ormlite.** { *; }
-keep class com.j256.ormlite.android.** { *; }
-keep class com.j256.ormlite.field.** { *; }
-keep class com.j256.ormlite.dao.** { *; }
-keep class com.j256.ormlite.db.** { *; }
-keep class com.j256.ormlite.stmt.** { *; }

#无法保存 bean
-keep class com.ishow.funnymap.bean.** { *; }
#保护<init>(Context context) 不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context);
}

#个推
-dontwarn com.igexin.**
-keep class com.igexin.**{*;}

-keepattributes SourceFile,LineNumberTable

# ACRA needs "annotations" so add this...
#-keepattributes *Annotation*

# volley
-dontwarn com.android.volley.jar.**
-keep class com.android.volley.**{*;}

-keepattributes *Annotation*
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgent
-keep public class * extends android.preference.Preference
-keep public class * extends android.app.Fragment
-keep public class com.android.vending.licensing.ILicensingService
-keep class com.itheima.mobilesafe.engine.AppInfoProvider
-keep class net.youmi.android.** {
*;
}
-keep class com.airi.buyue.entity.**{*;}
-keep class com.airi.buyue.data.**{*;}
-keep class com.airi.buyue.table.**{*;}
-dontshrink
-dontoptimize
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**
-dontwarn com.facebook.**
#-libraryjars src/main/SocialSDK_QQZone_2.jar
-keep enum com.facebook.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public interface com.facebook.**
-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**
-keep public class com.umeng.socialize.* {*;}
-keep public class javax.**
-keep public class android.webkit.**
-keep class com.facebook.**
-keep class com.umeng.scrshot.**
-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**
-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
-keep class im.yixin.sdk.api.YXMessage {*;}
-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}
-keep public class com.airi.buyue.R$*{
    public static final int *;
}

-keep class com.baidu.mapapi.** {*;}

#-libraryjars src/main/libs/locSDK_5.0.jar
-keep class com.baidu.** { *; }
-keep class vi.com.gdi.bgl.android.**{*;}

-keepclasseswithmembernames class * {
    native <methods>;
}

-dontshrink
-dontoptimize
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**
-dontwarn com.facebook.**

#-libraryjars src/main/libs/SocialSDK_QQZone_2.jar

-keep enum com.facebook.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

-keep public interface com.facebook.**
-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**

-keep public class com.umeng.socialize.* {*;}
-keep public class javax.**
-keep public class android.webkit.**

-keep class com.facebook.**
-keep class com.umeng.scrshot.**
-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**

-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}

-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}

-keep class im.yixin.sdk.api.YXMessage {*;}
-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}

-keep public class com.airi.buyue.R$*{
    public static final int *;
}

-keep,allowshrinking class org.android.agoo.service.* {
    public <fields>;
    public <methods>;
}

-keep,allowshrinking class com.umeng.message.* {
    public <fields>;
    public <methods>;
}

-keep public class com.airi.buyue.R$*{
   public static final int *;
}

#amap
-keep class android.support.v4.** {*;}
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment
-keep class com.amap.api.**  {*;}
-keep class com.autonavi.**  {*;}
-keep class com.a.a.**  {*;}

-keep class com.amp.apis.lib.**{*;}
-keep class com.squareup.picasso.**{*;}
-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment
-dontwarn com.amap.api.**
-dontwarn com.a.a.**
-dontwarn com.autonavi.**
-keep class com.amap.api.**  {*;}
-keep class com.autonavi.**  {*;}
-keep class com.a.a.**  {*;}

-keep class com.amap.api.mapcore.**{*;}
-keep class com.amap.api.maps.**{*;}
-keep class com.autonavi.amap.mapcore.*{*;}
-keep class com.amap.api.location.**{*;}
-keep class com.aps.**{*;}
-keep class com.amap.api.services.**{*;}

#//umeng mssg
-keep class com.umeng.message.* {
    public <fields>;
    public <methods>;
}

-keep class com.umeng.message.protobuffer.MessageResponse$PushResponse$Info {
    public <fields>;
    public <methods>;
}

-keep class com.umeng.message.protobuffer.MessageResponse$PushResponse$Info$Builder {
    public <fields>;
    public <methods>;
}

-keep class org.android.agoo.impl.*{
    public <fields>;
    public <methods>;
}

-keep class org.android.agoo.service.* {*;}

-keep class org.android.spdy.**{*;}

-keep public class com.airi.buyue.R$*{
    public static final int *;
}
-keep class org.springframework.**

-dontwarn butterknife.internal.**
-keep class **$$ViewInjector { *; }
-keepnames class * { @butterknife.InjectView *;}

-keepclassmembers class ** {
    public void onEvent*(**);
    void onEvent*(**);
}

-keep class com.airi.buyue.browser.**{*;}

#-keepclassmembers class * {
#    @android.webkit.JavascriptInterface <methods>;
#}
#-keepattributes JavascriptInterface
#-keep public class com.airi.buyue.browser.BrowserActivity$JsObject
#-keep public class * implements com.airi.buyue.browser.BrowserActivity$JsObject
#-keepclassmembers class com.airi.buyue.browser.BrowserActivity$JsObject {
#    <methods>;
#}