import org.gradle.kotlin.dsl.android
import org.gradle.kotlin.dsl.api
import org.gradle.kotlin.dsl.implementation
import org.gradle.kotlin.dsl.kotlin
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
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
            isStatic = true
        }
    }

    jvm("desktop")

    sourceSets {
        val desktopMain by getting
        androidMain.dependencies {
            implementation(projects.service.activityprovider.api)
            implementation(projects.service.activityprovider.implementation)
        }
        commonMain.dependencies {
            api(projects.service.auth.api)
            api(libs.google.firebase.kmm)
            api(libs.bundles.kotlinx.coroutines.common)
            api(libs.bundles.koin.common)
        }
        commonTest.dependencies {
            //implementation(libs.kotlin.test)
            //implementation(libs.kotlin.test.junit)
        }
        desktopMain.dependencies {
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}

android {
    namespace = "mobi.cwiklinski.bloodline.auth.firebase"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}