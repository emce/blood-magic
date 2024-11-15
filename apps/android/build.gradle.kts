import java.io.ByteArrayOutputStream
import org.jetbrains.compose.internal.utils.localPropertiesFile

@Suppress("dsl_scope_violation")

plugins {
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.androidApplication)
    id("org.jetbrains.kotlin.android")
    alias(libs.plugins.androidx.navigation.safeargs)
}

val gitCommitsCount: Int by lazy {
    val stdout = ByteArrayOutputStream()
    rootProject.exec {
        commandLine("git", "rev-list", "--count", "HEAD")
        standardOutput = stdout
    }
    stdout.toString().trim().toInt()
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

dependencies {
    implementation(projects.shared)
    implementation(libs.bundles.activity.android)
    implementation(libs.bundles.appcompat.android)
    implementation(libs.bundles.core.android)
    implementation(libs.bundles.compose.android)
    implementation(libs.bundles.koin.android)
}
