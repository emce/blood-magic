-printmapping build/release-mapping.txt

-keepclasseswithmembers public class mobi.cwiklinski.bloodline.MainKt {  # <-- Change com.company to yours
    public static void main(java.lang.String[]);
}

-dontwarn kotlinx.coroutines.debug.*

-keep class kotlin.** { *; }
-keep class kotlinx.** { *; }
-keep class kotlinx.coroutines.** { *; }
-keep class org.jetbrains.skia.** { *; }
-keep class org.jetbrains.skiko.** { *; }
-keep class org.jetbrains.compose.resources.** { *; }

-assumenosideeffects public class androidx.compose.runtime.ComposerKt {
    void sourceInformation(androidx.compose.runtime.Composer,java.lang.String);
    void sourceInformationMarkerStart(androidx.compose.runtime.Composer,int,java.lang.String);
    void sourceInformationMarkerEnd(androidx.compose.runtime.Composer);
}

##---------------Begin: proguard configuration for couroutines  ----------
# When editing this file, update the following files as well:
# - META-INF/com.android.tools/proguard/coroutines.pro
# - META-INF/com.android.tools/r8/coroutines.pro

# ServiceLoader support
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Most of volatile fields are updated with AFU and should not be mangled
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}

# Same story for the standard library's SafeContinuation that also uses AtomicReferenceFieldUpdater
-keepclassmembers class kotlin.coroutines.SafeContinuation {
    volatile <fields>;
}
##---------------End: proguard configuration for Couroutines  ----------

# Keep `Companion` object fields of serializable classes.
# This avoids serializer lookup through `getDeclaredClasses` as done for named companion objects.
-if @kotlinx.serialization.Serializable class **
-keepclassmembers class <1> {
    static <1>$Companion Companion;
}

# Keep `serializer()` on companion objects (both default and named) of serializable classes.
-if @kotlinx.serialization.Serializable class ** {
    static **$* *;
}
-keepclassmembers class <2>$<3> {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep `INSTANCE.serializer()` of serializable objects.
-if @kotlinx.serialization.Serializable class ** {
    public static ** INSTANCE;
}
-keepclassmembers class <1> {
    public static <1> INSTANCE;
    kotlinx.serialization.KSerializer serializer(...);
}

# @Serializable and @Polymorphic are used at runtime for polymorphic serialization.
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault

-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt # core serialization annotations
-dontnote kotlinx.serialization.SerializationKt

# Keep Serializers

-keep,includedescriptorclasses class com.company.package.**$$serializer { *; }  # <-- Change com.company.package
-keepclassmembers class com.company.package.** {  # <-- Change com.company.package to yours
    *** Companion;
}
-keepclasseswithmembers class com.company.package.** { # <-- Change com.company.package to yours
    kotlinx.serialization.KSerializer serializer(...);
}

# When kotlinx.serialization.json.JsonObjectSerializer occurs

-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**

# The support libraries contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version. We know about them, and they are safe.
-dontnote androidx.**
-dontwarn androidx.**

# Understand the @Keep support annotation.
-keep class androidx.annotation.Keep
-keep @androidx.annotation.Keep class * { *; }
-keepclasseswithmembers class * { @androidx.annotation.Keep <methods>; }
-keepclasseswithmembers class * { @androidx.annotation.Keep <fields>; }
-keepclasseswithmembers class * { @androidx.annotation.Keep <init>(...); }

# These classes are duplicated between android.jar and org.apache.http.legacy.jar.
-dontnote org.apache.http.**
-dontnote android.net.http.**

# These classes are duplicated between android.jar and core-lambda-stubs.jar.
-dontnote java.lang.invoke.**

##---------------Begin: proguard configuration for Okhttp  ----------
#Okhttp
# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**

# A resource is loaded with a relative path so the package of this class must be preserved.
-adaptresourcefilenames okhttp3/internal/publicsuffix/PublicSuffixDatabase.gz

# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*

# OkHttp platform used only on JVM and when Conscrypt and other security providers are available.
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
##---------------End: proguard configuration for Okhttp  ----------
##---------------Begin: proguard configuration for okio  ----------

# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*
##---------------End: proguard configuration for okio  ----------


##---------------Begin: proguard configuration for Voyager-------
-keep class cafe.adriel.voyager.*
-keepclassmembers class cafe.adriel.voyager.** { *; }
-keepclassmembers class cafe.adriel.voyager.koin.** { *; }
-keepclassmembers class * implements cafe.adriel.voyager.core.screen.Screen { *; }
##---------------End: proguard configuration for Voyager---------

##---------------Begin: proguard configuration for Firebase------
-keep class com.google.firebase.** { *; }
-keep class com.google.firebase.database.** { *; }
-keep class com.google.firebase.encoders.** { *; }
-dontwarn com.google.firebase.*
-keep class com.google.android.gms.* {  *; }
-dontwarn com.google.android.gms.**
-dontnote **ILicensingService
-dontnote com.google.android.gms.gcm.GcmListenerService
-dontnote com.google.android.gms.**


-dontwarn com.google.android.gms.ads.**
##---------------End: proguard configuration for Firebase--------

##---------------Begin: proguard configuration for Koin ---------
## Koin
# Koin Core
-keep class org.koin.** { *; }
-keep @org.koin.core.annotation.* class ** { *; }

# Keep Koin modules
-keep class * implements org.koin.core.module.Module { *; }
-keep class * implements org.koin.compose.* { *; }
-keep class **Kt { *; }  # Kotlin top-level files

# Keep  DI package
-keep class com.fshou.core.di.** { *; }

# Prevent obfuscation of Koin method names
-keepnames class * {
    @org.koin.* <methods>;
}

##---------------End: proguard configuration for Koin -----------

## Coil
-allowaccessmodification
-flattenpackagehierarchy
-mergeinterfacesaggressively
-dontnote *
-dontwarn org.slf4j.impl.StaticLoggerBinder

# Remove intrinsic assertions.
-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    public static void checkExpressionValueIsNotNull(...);
    public static void checkNotNullExpressionValue(...);
    public static void checkParameterIsNotNull(...);
    public static void checkNotNullParameter(...);
    public static void checkFieldIsNotNull(...);
    public static void checkReturnedValueIsNotNull(...);
}

## Preserve all native method names and the names of their classes.
-keepclasseswithmembernames,includedescriptorclasses class * {
    native <methods>;
}

-keep class mobi.cwiklinski.bloodline.** { *; }

-keeppackagenames resources.**

# Ignore warnings and Don't obfuscate for now
-dontobfuscate
-ignorewarnings

