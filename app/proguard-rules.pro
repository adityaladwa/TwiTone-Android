# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\Aditya\WorkSpace\Android_SDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-libraryjars libs

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application

-keepattributes InnerClasses,Signature

-keepnames class com.ladwa.aditya.twitone.** { *;}

-keep interface android.support.v7.** { *; }
-keep class android.support.v7.** { *; }
-keep interface android.support.v4.** { *; }
-keep class android.support.v4.** { *; }

-keep interface com.ladwa.aditya.twitone.data.**
-keep interface com.ladwa.aditya.twitone.mainscreen.**
-keep class com.ladwa.aditya.twitone.mainscreen.**

-keep class twitter4j.** { *; }
-keep interface twitter4j.conf.**

-dontwarn com.google.auto.value.**
-dontwarn autovalue.shaded.**
-dontwarn twitter4j.**
-dontwarn rx.internal.util.unsafe.**