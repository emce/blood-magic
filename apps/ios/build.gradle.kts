plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    iosX64 {
        binaries.framework {
            baseName = "BloodMagic"
            isStatic = true
        }
    }
    iosArm64 {
        binaries.framework {
            baseName = "BloodMagic"
            isStatic = true
        }
    }
    iosSimulatorArm64 {
        binaries.framework {
            baseName = "BloodMagic"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            //implementation(project(":core:platform-services:inject-ios"))
            //implementation(project(":feature:main-impl"))
        }
        iosMain.dependencies {}
    }
}