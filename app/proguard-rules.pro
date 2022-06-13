-dontoptimize
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

# Remove and use Change 1
#-repackageclasses
# Change 2
-optimizations !method/removal/parameter
# Preserve some attributes that may be required for reflection.
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService
-keep public class com.google.android.vending.licensing.ILicensingService
-dontnote com.android.vending.licensing.ILicensingService
-dontnote com.google.vending.licensing.ILicensingService
-dontnote com.google.android.vending.licensing.ILicensingService
# Keep setters in Views so that animations can still work.
# We want to keep methods in Activity that could be used in the XML attribute onClick.
# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keepclassmembers class * implements android.os.Parcelable {
    public static final ** CREATOR;
}
-keepclassmembers class **.R$* {
    public static <fields>;
}
# Preserve annotated Javascript interface methods.
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
# The support libraries contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# This class is deprecated, but remains for backward compatibility.
-dontwarn android.util.FloatMath
# platform version. We know about them, and they are safe.
-dontnote android.support.**
-dontnote androidx.**
-dontwarn android.support.**
-dontwarn androidx.**

-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}
-keepclassmembers public class * extends android.view.View {
    void set*(***);
    *** get*();
}
# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
    native <methods>;
}

# Understand the @Keep support annotation.
-keep class android.support.annotation.Keep
-keep class androidx.annotation.Keep

-keep @android.support.annotation.Keep class * {*;}
-keep @androidx.annotation.Keep class * {*;}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <methods>;
}

-keepclasseswithmembers class * {
    @androidx.annotation.Keep <methods>;
}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <fields>;
}

-keepclasseswithmembers class * {
    @androidx.annotation.Keep <fields>;
}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <init>(...);
}

-keepclasseswithmembers class * {
    @androidx.annotation.Keep <init>(...);
}

# These classes are duplicated between android.jar and org.apache.http.legacy.jar.
-dontnote org.apache.http.**
-dontnote android.net.http.**
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification

# Retrofit 2.X
## https://square.github.io/retrofit/ ##

-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# OkHttp
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

# Glide specific rules #
# https://github.com/bumptech/glide

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

# Okio
-keep class sun.misc.Unsafe { *; }
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn okio.**

#Facebook


-keepnames class com.facebook.FacebookActivity
-keepnames class com.facebook.CustomTabActivity

-keep class com.facebook.all.All

-keep public class com.android.vending.billing.IInAppBillingService {
    public static com.android.vending.billing.IInAppBillingService asInterface(android.os.IBinder);
    public android.os.Bundle getSkuDetails(int, java.lang.String, java.lang.String, android.os.Bundle);
}

# GMS
-dontwarn com.google.android.gms.measurement.AppMeasurement*

-keepclassmembers class * extends com.google.android.gms.internal.measurement.zzyv {
  <fields>;
}

# Firebase
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes InnerClasses

-keep class com.google.firebase.quickstart.database.java.viewholder.** {
    *;
}

-keepclassmembers class com.google.firebase.quickstart.database.java.models.** {
    *;
}

##########
# Crashlytics:
# Adding this in to preserve line numbers so that the stack traces can be remapped
##########
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

-dontwarn org.apache.commons.**
-dontwarn org.apache.**
# Okio
-keep class sun.misc.Unsafe { *; }
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn okio.**
-dontwarn com.google.*
-ignorewarnings

#-keep class * {
#    public private *;
#}
-keep class com.android.vending.billing.**

##########
# Firebase Database
##########
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes InnerClasses

-keep class com.google.firebase.quickstart.database.java.viewholder.** {
    *;
}

-keepclassmembers class com.google.firebase.quickstart.database.java.models.** {
    *;
}
##########
# Firebase Remote config
##########
-keepattributes EnclosingMethod
-keepattributes InnerClasses

##########
# Youtube Player
##########
# Needed by google-http-client-android when linking against an older platform version
-dontwarn com.google.api.client.extensions.android.**
# Needed by google-api-client-android when linking against an older platform version
-dontwarn com.google.api.client.googleapis.extensions.android.**
# Needed by google-play-services when linking against an older platform version
-dontwarn com.google.android.gms.**
# com.google.client.util.IOUtils references java.nio.file.Files when on Java 7+
-dontnote java.nio.file.Files, java.nio.file.Path
# Suppress notes on LicensingServices
-dontnote **.ILicensingService
# Suppress warnings on sun.misc.Unsafe
-dontnote sun.misc.Unsafe
-dontwarn sun.misc.Unsafe

-dontwarn com.google.common.**
-dontwarn com.google.api.client.json.**

#StarApp

-keep class com.startapp.** {
      *;
}

-keep class com.truenet.** {
      *;
}

-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
-keep class com.masterexcels.excelreader.model.**       { * ; }
-keep class com.masterexcels.excelreader.dao.**       { * ; }
-keep class com.masterexcels.excelreader.db.**       { * ; }
-keep class com.masterexcels.excelreader.services.**       { * ; }
-keep class com.wxiwei.office.fc.**       { * ; }
-keep class com.wxiwei.office.objectpool.**       { * ; }
-keep class com.wxiwei.office.ss.**       { * ; }
-keep class com.pdfreaderdreamw.**       { * ; }
-keep class com.tom_roush.**       { * ; }
-keep class com.github.barteksc.**       { * ; }
-keep class com.masterexcels.**       { * ; }
-keep class com.google.**       { * ; }
-keep class com.common.control.**       { * ; }