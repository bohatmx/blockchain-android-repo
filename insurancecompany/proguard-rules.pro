# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keepattributes Signature
-keepattributes *Annotation*
-keepattributes Exceptions

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
  ## Android architecture components: Lifecycle
  # LifecycleObserver's empty constructor is considered to be unused by proguard
  -keepclassmembers class * implements android.arch.lifecycle.LifecycleObserver {
      <init>(...);
  }
  # ViewModel's empty constructor is considered to be unused by proguard
  -keepclassmembers class * extends android.arch.lifecycle.ViewModel {
      <init>(...);
  }
  # keep Lifecycle State and Event enums values
  -keepclassmembers class android.arch.lifecycle.Lifecycle$State { *; }
  -keepclassmembers class android.arch.lifecycle.Lifecycle$Event { *; }
  # keep methods annotated with @OnLifecycleEvent even if they seem to be unused
  # (Mostly for LiveData.LifecycleBoundObserver.onStateChange(), but who knows)
  -keepclassmembers class * {
      @android.arch.lifecycle.OnLifecycleEvent *;
  }

  -keepclassmembers class * implements android.arch.lifecycle.LifecycleObserver {
      <init>(...);
  }

  -keep class * implements android.arch.lifecycle.LifecycleObserver {
      <init>(...);
  }
  -keep class * implements android.arch.lifecycle.LiveData {
        <init>(...);
    }
  -keepclassmembers class android.arch.** { *; }
  -keep class android.arch.** { *; }
  -dontwarn android.arch.**
