import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.compose.internal.utils.localPropertiesFile
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree
import java.io.ByteArrayOutputStream
import java.nio.charset.Charset

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

fun getGlobalVersionCode(gitCommitsCount: Int) = 700 + gitCommitsCount
fun getGlobalVersionName(gitCommitsCount: Int) = "${libs.versions.app}.${getGlobalVersionCode(gitCommitsCount)}"

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
            implementation(libs.kermit)
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
            implementation(libs.markdown)
            implementation(libs.markdown.m3)
            // Firebase
            implementation(libs.google.firebase.common)
            implementation(libs.google.firebase.auth)
            implementation(libs.google.firebase.database)
            implementation(project.dependencies.platform(libs.google.firebase.bom))
            // Libraries
            implementation(libs.libraries)
            implementation(libs.libraries.ui)
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
            implementation(libs.google.identity)
            implementation(libs.play.services.auth)
            implementation(libs.androidx.datastore.preferences)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.ktor.client.okhttp)
            // Skiko library incompatibility https://github.com/JetBrains/skiko/issues/983
            // implementation(libs.androidx.ui.desktop)
            implementation(libs.google.api.client)
            implementation(libs.google.api.client.gson)
            implementation(libs.google.oauth.client)
            implementation(libs.androidx.datastore.core.okio.jvm)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
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

    val gitCommitCountProvider = providers.of(GitCommitCountValueSource::class) {}
    val commitCount = gitCommitCountProvider.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = getGlobalVersionCode(commitCount)
        versionName = getGlobalVersionName(commitCount)
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    track.set("internal")
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

        val gitCommitCountProvider = providers.of(GitCommitCountValueSource::class) {}
        val commitCount = gitCommitCountProvider.get().toInt()

        nativeDistributions {
            packageName = "BloodMagic"
            packageVersion = getGlobalVersionName(commitCount)
            description =
                "Application for keeping records of donations for Honorary Blood Donors in Poland"
            copyright = "© 2016 mobiGEEK Michal Cwiklinski. All rights reserved."
            outputBaseDir.set(layout.buildDirectory.asFile.get().resolve("release"))
            targetFormats(TargetFormat.Deb, TargetFormat.Msi, TargetFormat.Pkg, TargetFormat.Dmg)


            macOS {
                bundleID = "mobi.cwiklinski.bloodline"
                iconFile.set(project.file("icons/bloodmagic.icns"))
                // APP Store
                signing {
                    properties["appleIdentity"]?.let { appleIdentity ->
                        sign.set(true)
                        identity.set(appleIdentity)
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
    duplicationMode = com.mikepenz.aboutlibraries.plugin.DuplicateMode.MERGE
    prettyPrint = true
}

buildConfig {
    className("FirebaseConfig")
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
    val webClientId = properties["googleClientId"].toString()

    buildConfigField("String", "FIREBASE_ANDROID_API_KEY", firebaseAndroidApiKey)
    buildConfigField("String", "FIREBASE_IOS_API_KEY", firebaseIosApiKey)
    buildConfigField("String", "FIREBASE_MESSAGING_SENDER_ID", firebaseMessagingSenderId)
    buildConfigField("String", "FIREBASE_APP_ID", firebaseAppId)
    buildConfigField("String", "FIREBASE_STORAGE_BUCKET", firebaseStorageBucket)
    buildConfigField("String", "FIREBASE_PROJECT_ID", firebaseProjectId)
    buildConfigField("String", "FIREBASE_DATABASE_URL", firebaseDatabaseUrl)
    buildConfigField("String", "ANDROID_CLIENT_ID", androidClientId)
    buildConfigField("String", "IOS_CLIENT_ID", iosClientId)
    buildConfigField("String", "WEB_CLIENT_ID", webClientId)
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

abstract class GitCommitCountValueSource : ValueSource<String, ValueSourceParameters.None> {
    @get:Inject
    abstract val execOperations: ExecOperations

    override fun obtain(): String {
        val output = ByteArrayOutputStream()
        execOperations.exec {
            commandLine("git", "rev-list", "--count", "HEAD")
            standardOutput = output
        }
        return String(output.toByteArray(), Charset.defaultCharset()).trim()
    }
}