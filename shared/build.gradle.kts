
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.compose.internal.utils.localPropertiesFile
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.ByteArrayOutputStream

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.google.services)
    alias(libs.plugins.kotlinxSerialization)
}

java {
    sourceCompatibility = JavaVersion.toVersion(libs.versions.jdk.get().toInt())
    targetCompatibility = JavaVersion.toVersion(libs.versions.jdk.get().toInt())
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
            jvmTarget.set(JvmTarget.fromTarget(libs.versions.jdk.get()))
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
            api(libs.notifier)
            api(libs.androidx.browser)
            api(libs.bundles.activity.android)
            api(libs.bundles.appcompat.android)
            api(libs.bundles.core.android)
            api(libs.bundles.compose.android)
            api(libs.accompanist.permissions)
            api(libs.bundles.koin.android)
        }
        commonMain.dependencies {
            api(projects.domain)
            api(projects.service.auth.api)
            api(projects.service.data.api)
            api(projects.service.storage.api)
            api(compose.animation)
            api(compose.foundation)
            api(compose.runtime)
            api(compose.runtimeSaveable)
            api(compose.ui)
            api(compose.material)
            api(compose.material3)
            api(compose.material3)
            api(compose.components.resources)
            api(compose.components.uiToolingPreview)
            api(libs.jetbrains.androidx.lifecycle.runtime.compose)
            implementation(libs.bundles.markdown.common)
            api(libs.bundles.voyager)
            api(libs.bundles.koin.common)
            api(libs.bundles.coil.common)
            api(libs.bundles.constraintlayout.common)
            api(libs.napier)
            implementation(projects.service.auth.filed)
            implementation(projects.service.auth.firebase)
            implementation(projects.service.data.filed)
            implementation(projects.service.data.firebase)
            implementation(projects.service.storage.datastore)
            //api(projects.service.notification.api)
            //implementation(projects.service.notification.fcm)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.markdown)
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

    task("testClasses")

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        freeCompilerArgs = listOf(
            "-Xopt-in=kotlin.RequiresOptIn",
            "-Xopt-in=kotlin.OptIn",
            "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-Xopt-in=kotlinx.coroutines.ObsoleteCoroutinesApi",
            "-Xopt-in=kotlinx.coroutines.FlowPreview",
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"
        )
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "mobi.cwiklinski.bloodline.resources"
    generateResClass = auto
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
        sourceCompatibility = JavaVersion.toVersion(libs.versions.jdk.get().toInt())
        targetCompatibility = JavaVersion.toVersion(libs.versions.jdk.get().toInt())
    }
}

compose.desktop {
    application {
        mainClass = "mobi.cwiklinski.bloodline.MainWindowKt"

        buildTypes.release.proguard {
            obfuscate.set(true)
            configurationFiles.from(project.file("desktop.pro"))
        }

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "mobi.cwiklinski.bloodline"
            packageVersion = "1.1.$gitCommitsCount"

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