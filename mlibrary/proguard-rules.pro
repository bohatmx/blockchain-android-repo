# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-dontwarn okio.**
-dontwarn javax.annotation.**
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions
# Add project specific ProGuard rules here.
-keepattributes *Annotation*

# This rule will properly ProGuard all the model classes in
# the package com.aftarobot.library.data.
-keepclassmembers class com.aftarobot.library.data.** {
  *;
}

# Retrofit 2.X
## https://square.github.io/retrofit/ ##

-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
###-keepresourcexmlelements manifest/application/meta-data@value=GlideModule
#### -- Picasso --
 -dontwarn com.squareup.picasso.**

 #### -- OkHttp --

 -dontwarn com.squareup.okhttp.internal.**

 #### -- Apache Commons --

 -dontwarn org.apache.commons.logging.**

 -ignorewarnings
 -keep class * {
     public private *;
     }

  -keep class com.crashlytics.** { *; }
  -dontwarn com.crashlytics.**

