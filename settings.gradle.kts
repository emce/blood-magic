rootProject.name = "BloodMagic"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            content {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(":shared")
include(":common")
include(":commonTest")
include(":domain")
include(":service:auth:api")
//include(":service:auth:firebase")
include(":service:auth:filed")
include(":service:activityprovider:api")
include(":service:activityprovider:implementation")
include(":service:data:api")
//include(":service:data:firebase")
include(":service:data:filed")
include(":service:storage:api")
include(":service:storage:datastore")
//include(":service:notification:api")
//include(":service:notification:fcm")
include(":ui")
include(":androidApp")
