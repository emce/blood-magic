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
    alias(libs.plugins.play.publisher)
    alias(libs.plugins.kotlinCocoapods)
}

val properties = localPropertiesFile.readLines().associate {
    if (it.startsWith("#") || !it.contains("=")) return@associate "" to ""
    val (key, value) = it.split("=", limit = 2)
    key to value
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
private val globalVersionCode = 700 + gitCommitsCount
private val globalVersionName = "5.1.$globalVersionCode"

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

    cocoapods {
        version = "1.0.0"
        ios.deploymentTarget = "15.1"
        framework {
            baseName = "shared"
        }
        pod("FirebaseCore", linkOnly = true)
        pod("FirebaseAuth", linkOnly = true)
        pod("FirebaseDatabase", linkOnly = true)
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
            api(libs.ktor.android)
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
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(projects.commonTest)
            implementation(libs.turbine)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.androidx.ui.desktop)
            implementation(libs.ktor.jvm)
        }
        iosMain.dependencies {
            implementation(libs.ktor.apple)
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
        versionCode = globalVersionCode
        versionName = globalVersionName
        setProperty("archivesBaseName", "Blood-Magic($globalVersionName)")
        vectorDrawables.useSupportLibrary = true
    }

    signingConfigs {
        create("release") {
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
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard.pro")
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

play {
    serviceAccountCredentials.set(file("../play_config.json"))
    track.set("internal")
}

compose.desktop {
    application {
        mainClass = "mobi.cwiklinski.bloodline.MainWindowKt"

        buildTypes.release.proguard {
            obfuscate.set(true)
            configurationFiles.from(project.file("proguard.pro"))
        }

        nativeDistributions {
            packageName = "BloodMagic"
            packageVersion = globalVersionName
            //description = "Application for keeping records of donations for Honorary Blood Donors in Poland"
            //copyright = "© 2016 mobiGEEK Michal Cwiklinski. All rights reserved."
            outputBaseDir.set(layout.buildDirectory.asFile.get().resolve("release"))
            targetFormats(TargetFormat.Deb, TargetFormat.Dmg, TargetFormat.Msi)

            val iconsRoot = project.file("icons")
            macOS {
                bundleID = "mobi.cwiklinski.bloodline"
                iconFile.set(iconsRoot.resolve("bloodmagic.icns"))

                signing {
                    properties["appleIdentity"]?.let { appleIdentity ->
                        sign.set(true)
                        identity.set(appleIdentity)
                        properties["appleKeychain"]?.let { appleKeychain ->
                            keychain.set(appleKeychain)
                        }
                    }
                }
                provisioningProfile.set(project.file("embedded.provisionprofile"))
                runtimeProvisioningProfile.set(project.file("runtime.provisionprofile"))
                entitlementsFile.set(project.file("../entitlements.plist"))
                runtimeEntitlementsFile.set(project.file("../runtime-entitlements.plist"))
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

tasks.create("printVersionName") {
    doLast { println(globalVersionName) }
}