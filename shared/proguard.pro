-printmapping build/release-mapping.txt

-keepclasseswithmembers public class MainKt {  # <-- Change com.company to yours
    public static void main(java.lang.String[]);
}

-dontwarn kotlinx.coroutines.debug.*

-keep class kotlin.** { *; }
-keep class kotlinx.** { *; }
-keep class kotlinx.coroutines.** { *; }
-keep class org.jetbrains.skia.** { *; }
-keep class org.jetbrains.skiko.** { *; }

-assumenosideeffects public class androidx.compose.runtime.ComposerKt {
    void sourceInformation(androidx.compose.runtime.Composer,java.lang.String);
    void sourceInformationMarkerStart(androidx.compose.runtime.Composer,int,java.lang.String);
    void sourceInformationMarkerEnd(androidx.compose.runtime.Composer);
}

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

# A resource is loaded with a relative path so the package of this class must be preserved.
-adaptresourcefilenames okhttp3/internal/publicsuffix/PublicSuffixDatabase.gz

# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*

# OkHttp platform used only on JVM and when Conscrypt and other security providers are available.
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
#################################### SLF4J #####################################
-dontwarn org.slf4j.**

# Prevent runtime crashes from use of class.java.getName()
-dontwarn javax.naming.**

## OkHttp3
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase


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

## Firebase
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
-keepclassmembers class com.google.firebase.quickstart.database.kotlin.models.** {
    *;
}

## Google GMS
-keepclassmembers class com.google.android.gms.dynamite.DynamiteModule {
    ** MODULE_ID;
    ** MODULE_VERSION;
    ** sClassLoader;
}
-keepclassmembers class com.google.android.gms.internal.in {
    ** mOrigin;
    ** mCreationTimestamp;
    ** mName;
    ** mValue;
    ** mTriggerEventName;
    ** mTimedOutEventName;
    ** mTimedOutEventParams;
    ** mTriggerTimeout;
    ** mTriggeredEventName;
    ** mTriggeredEventParams;
    ** mTimeToLive;
    ** mExpiredEventName;
    ** mExpiredEventParams;
}
-keepclassmembers class com.google.devtools.build.android.desugar.runtime.ThrowableExtension {
    ** SDK_INT;
}
-keep class com.google.android.gms.dynamic.IObjectWrapper
-keep class com.google.android.gms.tasks.Task
-keep class com.google.android.gms.tasks.TaskCompletionSource
-keep class com.google.android.gms.tasks.OnSuccessListener
-keep class com.google.android.gms.tasks.OnFailureListener
-keep class com.google.android.gms.tasks.OnCompleteListener
-keep class com.google.android.gms.tasks.Continuation
-keep class com.google.android.gms.measurement.AppMeasurement$EventInterceptor
-keep class com.google.android.gms.measurement.AppMeasurement$OnEventListener
-keep class com.google.android.gms.measurement.AppMeasurement$zza
-keep class com.google.android.gms.internal.zzcgl
-keep class com.google.android.gms.internal.zzbhh
-keep class com.google.android.gms.internal.aad
-keep class com.google.android.gms.internal.aae
-keep class com.google.android.gms.internal.iq
-keep class com.google.android.gms.internal.ly
-keep class com.google.android.gms.internal.kx
-keep class com.google.android.gms.internal.xf
-keep class com.google.android.gms.internal.qu
-keep class com.google.android.gms.internal.qr
-keep class com.google.android.gms.internal.xm
-keep class com.google.android.gms.internal.aaj
-keep class com.google.android.gms.internal.aat
-keep class com.google.android.gms.internal.aah
-keep class com.google.android.gms.internal.rx
-keep class com.google.android.gms.internal.qg
-keep class com.google.android.gms.internal.sh
-keep class com.google.android.gms.internal.qu
-keep class com.google.android.gms.internal.vq
-keep class com.google.android.gms.internal.qi
-keep class com.google.android.gms.internal.oh
-keep class com.google.android.gms.internal.oo
-keep class com.google.android.gms.internal.oc
-keep class com.google.android.gms.internal.oi
-keep class com.google.android.gms.internal.ol
-keep class com.google.android.gms.internal.wn
-keep class com.google.android.gms.internal.oj
-keep class com.google.android.gms.internal.om
-keep class com.google.android.gms.internal.pf
-keep class com.google.android.gms.internal.za
-keep class com.google.android.gms.internal.pz
-keep class com.google.android.gms.internal.zn
-keep class com.google.android.gms.internal.zi
-keep class com.google.android.gms.internal.aen
-keep class com.google.android.gms.internal.aas
-keep class com.google.android.gms.internal.aav
-keep class com.google.android.gms.internal.aag
-keep class com.google.android.gms.internal.abh
-keep class com.google.android.gms.internal.abk
-keep class com.google.android.gms.internal.abq
-keep class com.google.android.gms.internal.abl
-keep class com.google.android.gms.internal.acf
-keep class com.google.android.gms.common.api.Result
-keep class com.google.android.gms.common.zza

-dontnote com.google.android.gms.internal.ql
-dontnote com.google.android.gms.internal.zzcem
-dontnote com.google.android.gms.internal.zzchl

# Firebase notes
-dontnote com.google.firebase.messaging.zza

# Protobuf notes
-dontnote com.google.protobuf.zzc
-dontnote com.google.protobuf.zzd
-dontnote com.google.protobuf.zze

-keep class mobi.cwiklinski.bloodline.** { *; }

# Ignore warnings and Don't obfuscate for now
-dontobfuscate
-ignorewarnings

