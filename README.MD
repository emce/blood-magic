# Blood magic - aplikacja dla Krwiodawców w Polsce do śledzenia donacji

## Before running!
 - check your system with [KDoctor](https://github.com/Kotlin/kdoctor)
 - install JDK 17 or higher on your machine
 - add `local.properties` file to the project root and set a path to Android SDK there

### Android
Aby przygotować aplikację:
 - uruchom `./gradlew :composeApp:assembleDebug`
 - znajdź plik `.apk` w `shared/build/outputs/apk/debug/composeApp-debug.apk`
Run android UI tests on the connected device: `./gradlew :composeApp:connectedDebugAndroidTest`

### Desktop
Aby uruchomić aplikację: `./gradlew :shared:run`
Aby uruchomić testy: `./gradlew :shared:jvmTest`
Aby wybudowac aplikację, uruchom: `./gradlew clean :shared:packageReleaseDistributionForCurrentOS -PmacOsAppStoreRelease=true`

### iOS
Aby przygotować i uruchomić aplikację  na urządzeniu/emulatorze:
 - Otwórz `iosApp/iosApp.xcproject` w Xcode i uruchom
- Albo użyj [Kotlin Multiplatform Mobile plugin](https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform-mobile) dla Android Studio
Aby uruchomić testy UI: `./gradlew :shared:iosSimulatorArm64Test`

### Resource
Aby przebudować zasoby uruchom:
`./gradlew :shared:generateResourceAccessorsForCommonMain`

### Libraries
Aby wygenerować plik z listą bibliotek, uruchom:
`./gradlew share:exportLibraryDefinitions -PaboutLibraries.exportPath=src/commonMain/composeResources/files -PaboutLibraries.exportVariant=release`


