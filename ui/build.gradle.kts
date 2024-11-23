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
            api(libs.androidx.browser)
            api(libs.bundles.activity.android)
            api(libs.bundles.appcompat.android)
            api(libs.bundles.core.android)
            api(libs.bundles.compose.android)
            api(libs.bundles.koin.android)
            api(libs.accompanist.permissions)
        }
        commonMain.dependencies {
            // Modules
            api(projects.domain)
            api(projects.service.auth.api)
            api(projects.service.data.api)
            api(projects.service.storage.api)
            implementation(projects.service.auth.filed)
            implementation(projects.service.data.filed)
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
            implementation(libs.napier)
            // Bundles
            api(libs.bundles.constraintlayout.common)
            implementation(libs.bundles.coil.common)
            implementation(libs.bundles.koin.common)
            implementation(libs.bundles.voyager)
        }
        commonTest.dependencies {
            implementation(libs.bundles.tests)
            implementation(libs.kotlinx.coroutines.test)
            implementation(projects.commonTest)
            implementation(libs.turbine)
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
        sourceCompatibility = JavaVersion.toVersion(libs.versions.jdk.get().toInt())
        targetCompatibility = JavaVersion.toVersion(libs.versions.jdk.get().toInt())
    }
}