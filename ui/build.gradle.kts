import org.gradle.kotlin.dsl.android
import org.gradle.kotlin.dsl.kotlin
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinxSerialization)
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
            api(projects.service.activityprovider.api)
            implementation(projects.service.activityprovider.implementation)
            implementation(libs.bundles.activity.android)
            implementation(libs.bundles.appcompat.android)
            implementation(libs.bundles.core.android)
            implementation(libs.bundles.compose.android)
            implementation(libs.bundles.koin.android)
            api(libs.accompanist.permissions)
        }
        commonMain.dependencies {
            // Modules
            api(projects.domain)
            api(projects.service.auth.api)
            api(projects.service.data.api)
            api(projects.service.storage.api)
            implementation(projects.service.auth.firebase)
            implementation(projects.service.data.firebase)
            implementation(projects.service.storage.datastore)
            api(compose.animation)
            api(compose.foundation)
            api(compose.runtime)
            api(compose.runtimeSaveable)
            api(compose.ui)
            api(compose.material)
            api(compose.material3)
            api(compose.components.resources)
            api(compose.components.uiToolingPreview)
            api(libs.jetbrains.androidx.lifecycle.runtime.compose)
            // Bundles
            api(libs.bundles.constraintlayout.common)
            implementation(libs.bundles.coil.common)
            implementation(libs.bundles.koin.common)
            implementation(libs.bundles.voyager)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.androidx.ui.desktop)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "mobi.cwiklinski.bloodline.resources"
    generateResClass = auto
}

android {
    namespace = "mobi.cwiklinski.bloodline.ui"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}