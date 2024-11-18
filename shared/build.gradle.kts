import org.gradle.kotlin.dsl.api
import org.gradle.kotlin.dsl.kotlin
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.compose.internal.utils.localPropertiesFile
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import java.io.ByteArrayOutputStream

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.google.services)
}

private val gitCommitsCount: Int by lazy {
    val stdout = ByteArrayOutputStream()
    rootProject.exec {
        commandLine("git", "rev-list", "--count", "HEAD")
        standardOutput = stdout
    }
    stdout.toString().trim().toInt()
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
            baseName = "Blood Magic"
            isStatic = true
        }
    }
    
    jvm("desktop")
    
    sourceSets {
        val desktopMain by getting
        
        androidMain.dependencies {
            api(projects.service.activityprovider.api)
            api(libs.notifier)
            implementation(projects.service.activityprovider.implementation)
            implementation(libs.bundles.appcompat.android)
            implementation(libs.bundles.core.android)
            implementation(libs.bundles.compose.android)
            implementation(libs.bundles.koin.android)
        }
        commonMain.dependencies {
            api(projects.domain)
            api(projects.service.auth.api)
            api(projects.service.data.api)
            api(projects.service.storage.api)
            api(projects.service.notification.api)
            implementation(projects.service.notification.fcm)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(projects.ui)
            implementation(libs.bundles.voyager)
            implementation(libs.bundles.koin.common)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
        desktopMain.dependencies {
            api(compose.desktop.currentOs)
            api(libs.kotlinx.coroutines.swing)
            api(libs.androidx.ui.desktop)
        }
    }
    task("testClasses")
}

android {
    namespace = "mobi.cwiklinski.bloodline"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "mobi.cwiklinski.bloodline"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 700 + gitCommitsCount
        versionName = "5.0.$gitCommitsCount"
        vectorDrawables.useSupportLibrary = true
    }
    buildFeatures {
        compose = true
    }
    signingConfigs {
        create("release") {
            val properties = localPropertiesFile.readLines().associate {
                if (it.startsWith("#") || !it.contains("=")) return@associate "" to ""
                val (key, value) = it.split("=", limit = 2)
                key to value
            }
            storeFile = file(properties["bloodlineReleaseKeystore"].toString())
            storePassword = properties["bloodlineReleasePassword"].toString()
            keyAlias = properties["bloodlineReleaseAlias"].toString()
            keyPassword = properties["bloodlineReleasePassword"].toString()
        }
    }
    buildTypes {
        getByName("debug") {
            isDebuggable = true
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
        getByName("release") {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("release")
        }
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

compose.desktop {
    application {
        mainClass = "mobi.cwiklinski.bloodline.MainWindowKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "mobi.cwiklinski.bloodline"
            packageVersion = "1.0.$gitCommitsCount"

            val iconsRoot = project.file("icons")
            macOS {
                iconFile.set(iconsRoot.resolve("bloodmagic.icns"))
            }
            windows {
                iconFile.set(iconsRoot.resolve("bloodmagic.ico"))
            }
            linux {
                iconFile.set(iconsRoot.resolve("bloodmagic.png"))
            }
        }
    }
}