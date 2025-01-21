
import com.mikepenz.aboutlibraries.plugin.DuplicateMode
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.compose.internal.utils.localPropertiesFile
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree
import java.io.ByteArrayOutputStream

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.google.services)
    alias(libs.plugins.play.publisher)
    alias(libs.plugins.parcelize)
    alias(libs.plugins.libraries)
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

@OptIn(ExperimentalKotlinGradlePluginApi::class)
kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(libs.versions.jdk.get()))
            freeCompilerArgs.addAll(
                "-P",
                "plugin:org.jetbrains.kotlin.parcelize:additionalAnnotation=mobi.cwiklinski.bloodline.data.Parcelize"
            )
        }
        //https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-test.html
        instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
    }

    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(libs.versions.jdk.get()))
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            export("io.github.kalinjul.kotlin.multiplatform:oidc-appsupport")
            export("io.github.kalinjul.kotlin.multiplatform:oidc-tokenstore")
            export("io.github.kalinjul.kotlin.multiplatform:oidc-core")
            export("io.github.mirzemehdi:kmpnotifier")
            baseName = "BloodMagic"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.constraintlayout.compose.multiplatform)
            implementation(libs.androidx.compose.material3.window)
            implementation(libs.androidx.lifecycle.compose)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
            // Ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.serialization)
            implementation(libs.ktor.client.logging)
            // Koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.coroutines)
            // Coil
            implementation(libs.coil)
            implementation(libs.coil.compose)
            implementation(libs.coil.compose.svg)
            implementation(libs.coil.network.ktor)
            // DataStore
            implementation(libs.androidx.datastore.core)
            implementation(libs.androidx.datastore.preferences.core)
            // Voyager
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.screenModel)
            implementation(libs.voyager.bottomSheetNavigator)
            implementation(libs.voyager.transitions)
            // https://github.com/adrielcafe/voyager/issues/515
            // implementation(libs.voyager.koin)
            // Firebase
            implementation(libs.google.firebase.common)
            implementation(libs.google.firebase.auth)
            implementation(libs.google.firebase.database)
            implementation(project.dependencies.platform(libs.google.firebase.bom))
            // Libraries
            implementation(libs.libraries)
            implementation(libs.libraries.ui)
            // OIDC
            implementation(libs.oidc.appsupport)
            // Others
            implementation(libs.markdown)
            implementation(libs.markdown.m3)
            implementation(libs.kermit)
            api(libs.notifier)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.turbine)
        }

        androidMain.dependencies {
            implementation(compose.uiTooling)
            implementation(libs.androidx.activityCompose)
            implementation(libs.androidx.browser)
            implementation(libs.androidx.credentials)
            implementation(libs.androidx.credentials.play.services)
            implementation(libs.androidx.workmanager)
            implementation(libs.google.identity)
            implementation(libs.play.services.auth)
            implementation(libs.androidx.datastore.preferences)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
            implementation(libs.koin.android.workmanager)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.ktor.server.core)
            implementation(libs.ktor.server.cio)
            // Skiko library incompatibility https://github.com/JetBrains/skiko/issues/983
            // implementation(libs.androidx.ui.desktop)
            implementation(libs.androidx.datastore.core.okio.jvm)
            implementation(libs.oidc.ktor)
            implementation(libs.slf4j.simple)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.oidc.ktor)
        }

        task("testClasses")

        compilerOptions {
            freeCompilerArgs = listOf(
                "-opt-in=kotlin.RequiresOptIn",
                "-opt-in=kotlin.OptIn",
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlinx.coroutines.ObsoleteCoroutinesApi",
                "-opt-in=kotlinx.coroutines.FlowPreview",
                "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
                "-opt-in=androidx.compose.material.ExperimentalMaterialApi",
                "-Xexpect-actual-classes"
            )
        }

    }
}

android {
    namespace = "mobi.cwiklinski.bloodline"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    val versionCodeString = getCurrentVersion()
    val versionCodeNumber = versionCodeString.split(".").last().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = versionCodeNumber
        versionName = versionCodeString
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        addManifestPlaceholders(
            mapOf("oidcRedirectScheme" to "bloodmagic")
        )
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
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
    track.set("production")
}

compose.resources {
    publicResClass = true
    packageOfResClass = "mobi.cwiklinski.bloodline.resources"
    generateResClass = auto
}

compose.desktop {
    application {
        mainClass = "mobi.cwiklinski.bloodline.MainWindowKt"

        buildTypes.release.proguard {
            isEnabled.set(false)
            obfuscate.set(false)
            optimize.set(false)
            configurationFiles.from(project.file("proguard.pro"))
            version.set("7.4.0")
        }

        nativeDistributions {
            packageName = "BloodMagic"
            packageVersion = getCurrentVersion()
            description =
                "Application for keeping records of donations for Honorary Blood Donors in Poland"
            copyright = "© 2016 mobiGEEK Michal Cwiklinski. All rights reserved."
            outputBaseDir.set(layout.buildDirectory.asFile.get().resolve("release"))
            targetFormats(TargetFormat.Pkg)


            macOS {
                bundleID = "mobi.cwiklinski.bloodline"
                iconFile.set(project.file("icons/bloodmagic.icns"))
                minimumSystemVersion = "13.0"
                appCategory = "public.app-category.healthcare-fitness"
                packageVersion = "792"
                infoPlist {
                    extraKeysRawXml = """
                        <key>LSMinimumSystemVersion</key>
                        <string>13.0</string>
                        <key>LSApplicationCategoryType</key>
                        <string>public.app-category.healthcare-fitness</string>
                    """.trimIndent()
                }
                // AppStore
                signing {
                    properties["appleIdentity"]?.let { appleIdentity ->
                        sign.set(true)
                        identity.set(appleIdentity)
                        properties["appleKeychain"]?.let { appleKeychain ->
                            keychain.set(appleKeychain)
                        }
                    }
                }
                // Notarization
                notarization {
                    properties["appleId"]?.let { appleId ->
                        appleID.set(appleId)
                    }
                    properties["applePassword"]?.let { applePassword ->
                        password.set(applePassword)
                    }
                    properties["appleTeamId"]?.let { appleTeamId ->
                        teamID.set(appleTeamId)
                    }
                }
                // TestFlight
                provisioningProfile.set(project.file("../embedded.provisionprofile"))
                runtimeProvisioningProfile.set(project.file("../runtime.provisionprofile"))
                entitlementsFile.set(project.file("../entitlements.plist"))
                runtimeEntitlementsFile.set(project.file("../runtime-entitlements.plist"))
            }
            linux {
                iconFile.set(project.file("icons/bloodmagic.png"))
            }
            windows {
                iconFile.set(project.file("icons/bloodmagic.ico"))
            }
        }
    }
}

aboutLibraries {
    gitHubApiToken = properties["githubToken"].toString()
    includePlatform = false
    duplicationMode = DuplicateMode.MERGE
    registerAndroidTasks = false
    prettyPrint = true
}

buildConfig {
    className("AppConfig")
    packageName("mobi.cwiklinski.bloodline.config")
    useKotlinOutput { internalVisibility = true }

    val firebaseAndroidApiKey = properties["firebaseAndroidApiKey"].toString()
    val firebaseIosApiKey = properties["firebaseIosApiKey"].toString()
    val firebaseMessagingSenderId = properties["firebaseGcmSenderId"].toString()
    val firebaseAppId = properties["firebaseApplicationId"].toString()
    val firebaseStorageBucket = properties["firebaseStorageBucket"].toString()
    val firebaseProjectId = properties["firebaseProjectId"].toString()
    val firebaseDatabaseUrl = properties["firebaseDatabaseUrl"].toString()
    val androidClientId = properties["googleAndroidClientId"].toString()
    val iosClientId = properties["googleIosClientId"].toString()
    val googleClientId = properties["googleClientId"].toString()
    val googleClientSecret = properties["googleClientSecret"].toString()
    val facebookClientId = properties["facebookClientId"].toString()
    val facebookClientSecret = properties["facebookClientSecret"].toString()
    val appleClientId = properties["appleClientId"].toString()
    val appleClientSecret = properties["appleClientSecret"].toString()

    buildConfigField("String", "FIREBASE_ANDROID_API_KEY", firebaseAndroidApiKey)
    buildConfigField("String", "FIREBASE_IOS_API_KEY", firebaseIosApiKey)
    buildConfigField("String", "FIREBASE_MESSAGING_SENDER_ID", firebaseMessagingSenderId)
    buildConfigField("String", "FIREBASE_APP_ID", firebaseAppId)
    buildConfigField("String", "FIREBASE_STORAGE_BUCKET", firebaseStorageBucket)
    buildConfigField("String", "FIREBASE_PROJECT_ID", firebaseProjectId)
    buildConfigField("String", "FIREBASE_DATABASE_URL", firebaseDatabaseUrl)
    buildConfigField("String", "ANDROID_CLIENT_ID", androidClientId)
    buildConfigField("String", "IOS_CLIENT_ID", iosClientId)
    buildConfigField("String", "GOOGLE_CLIENT_ID", googleClientId)
    buildConfigField("String", "GOOGLE_CLIENT_SECRET", googleClientSecret)
    buildConfigField("String", "FACEBOOK_CLIENT_ID", facebookClientId)
    buildConfigField("String", "FACEBOOK_CLIENT_SECRET", facebookClientSecret)
    buildConfigField("String", "APPLE_CLIENT_ID", appleClientId)
    buildConfigField("String", "APPLE_CLIENT_SECRET", appleClientSecret)
}

afterEvaluate {
    tasks.findByName("generateDebugUnitTestLintModel")?.dependsOn(
        tasks.getByName("generateAndroidUnitTestDebugNonAndroidBuildConfig"),
        tasks.getByName("generateAndroidUnitTestNonAndroidBuildConfig"),
    )
    tasks.findByName("lintAnalyzeDebugUnitTest")?.dependsOn(
        tasks.getByName("generateAndroidUnitTestDebugNonAndroidBuildConfig"),
        tasks.getByName("generateAndroidUnitTestNonAndroidBuildConfig"),
    )
    // Disabled due to frameworks linking error
    tasks.filter {
        it.name.contains("TestIos", true)
    }.forEach {
        it.enabled = false
    }
    tasks.withType<JavaExec>().named { it == "jvmRun" }
        .configureEach { mainClass = "mobi.cwiklinski.bloodline.MainWindowKt" }
}

fun getCurrentVersion(): String {
    val stdout = ByteArrayOutputStream()
    val scriptPath = projectDir.path.replace("shared", "")
    exec {
        executable("/bin/sh")
        args("-c", "${scriptPath}/print_version.sh")
        standardOutput = stdout
    }
    return stdout.toString().trim()
}

tasks.register("printVersion") {
    println(getCurrentVersion())
}