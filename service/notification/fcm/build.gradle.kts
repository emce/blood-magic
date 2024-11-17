import org.gradle.kotlin.dsl.android
import org.gradle.kotlin.dsl.implementation
import org.gradle.kotlin.dsl.kotlin
import org.jetbrains.compose.internal.utils.localPropertiesFile
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.buildconfig)
    alias(libs.plugins.kotlinCocoapods)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {

    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            export(libs.notifier)
            isStatic = true
        }
    }

    cocoapods {
        version = "1.0.0"
        summary = "Notification Service"
        ios.deploymentTarget = "14.1"

        framework {
            baseName = "notificationService"
        }
        noPodspec()
        pod("FirebaseCore")
        pod("FirebaseMessaging")
    }

    jvm("desktop")

    sourceSets {
        val desktopMain by getting
        androidMain.dependencies {
            api(projects.service.activityprovider.api)
            api(libs.bundles.activity.android)
            implementation(projects.service.activityprovider.implementation)
        }
        commonMain.dependencies {
            api(projects.service.notification.api)
            api(libs.notifier)
            api(libs.bundles.koin.common)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        desktopMain.dependencies {
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}

android {
    namespace = "mobi.cwiklinski.bloodline.notification.fcm"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

buildConfig {
    className("FirebaseConfig")   // forces the class name. Defaults to 'BuildConfig'
    packageName("mobi.cwiklinski.bloodline.config")
    useKotlinOutput { internalVisibility = true }
    val properties = localPropertiesFile.readLines().associate {
        if (it.startsWith("#") || !it.contains("=")) return@associate "" to ""
        val (key, value) = it.split("=", limit = 2)
        key to value
    }

    val firebaseAndroidApiKey = properties["firebaseAndroidApiKey"].toString()
    val firebaseIosApiKey = properties["firebaseIosApiKey"].toString()
    val firebaseMessagingSenderId = properties["firebaseGcmSenderId"].toString()
    val firebaseAppId = properties["firebaseApplicationId"].toString()
    val firebaseStorageBucket = properties["firebaseStorageBucket"].toString()
    val firebaseProjectId = properties["firebaseProjectId"].toString()
    val firebaseDatabaseUrl = properties["firebaseDatabaseUrl"].toString()

    buildConfigField("String", "FIREBASE_ANDROID_API_KEY", firebaseAndroidApiKey)
    buildConfigField("String", "FIREBASE_IOS_API_KEY", firebaseIosApiKey)
    buildConfigField("String", "FIREBASE_MESSAGING_SENDER_ID", firebaseMessagingSenderId)
    buildConfigField("String", "FIREBASE_APP_ID", firebaseAppId)
    buildConfigField("String", "FIREBASE_STORAGE_BUCKET", firebaseStorageBucket)
    buildConfigField("String", "FIREBASE_PROJECT_ID", firebaseProjectId)
    buildConfigField("String", "FIREBASE_DATABASE_URL", firebaseDatabaseUrl)
}