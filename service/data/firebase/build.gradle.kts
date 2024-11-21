import org.gradle.kotlin.dsl.android
import org.gradle.kotlin.dsl.kotlin
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
}

java {
    sourceCompatibility = JavaVersion.toVersion(libs.versions.jdk.get().toInt())
    targetCompatibility = JavaVersion.toVersion(libs.versions.jdk.get().toInt())
}

kotlin {

    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(libs.versions.jdk.get()))
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
            api(projects.common)
            api(projects.domain)
            api(projects.service.data.api)
            api(libs.google.firebase.auth)
            api(libs.google.firebase.database)
            api(libs.bundles.kotlinx.datetime.common)
            api(libs.bundles.kotlinx.coroutines.common)
            api(libs.bundles.koin.common)
        }
        commonTest.dependencies {
            implementation(libs.junit)
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(projects.commonTest)
        }
        desktopMain.dependencies {
            api(libs.kotlinx.coroutines.swing)
        }
    }
}

android {
    namespace = "mobi.cwiklinski.bloodline.data.firebase"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.jdk.get().toInt())
        targetCompatibility = JavaVersion.toVersion(libs.versions.jdk.get().toInt())
    }
}