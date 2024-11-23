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
            api(libs.bundles.datastore.android)
        }
        commonMain.dependencies {
            api(projects.common)
            api(projects.service.storage.api)
            api(projects.domain)
            api(libs.bundles.datastore.common)
            api(libs.bundles.kotlinx.coroutines.common)
            api(libs.bundles.koin.common)
        }
        commonTest.dependencies {
            implementation(libs.junit)
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
        desktopMain.dependencies {
            api(libs.kotlinx.coroutines.swing)
        }
    }
}

android {
    namespace = "mobi.cwiklinski.bloodline.storage.datastore"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.jdk.get().toInt())
        targetCompatibility = JavaVersion.toVersion(libs.versions.jdk.get().toInt())
    }
}

configurations
    .matching { it.name.endsWith("TestRuntimeClasspath") }
    .configureEach {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-test-junit")
    }