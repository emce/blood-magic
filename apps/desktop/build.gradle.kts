import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import java.io.ByteArrayOutputStream

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
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

    jvm("desktop")

    sourceSets {
        val desktopMain by getting
        desktopMain.dependencies {
            //implementation(project(":core:platform-services:inject-desktop"))
            //implementation(project(":feature:main-impl"))
        }
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