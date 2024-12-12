package mobi.cwiklinski.bloodline.ui.di

import androidx.constraintlayout.compose.platform.annotation.SuppressWarnings
import mobi.cwiklinski.bloodline.storage.datastore.createStorageModule
import mobi.cwiklinski.bloodline.ui.manager.AppCallbackManager
import mobi.cwiklinski.bloodline.ui.manager.CallbackManager
import mobi.cwiklinski.bloodline.ui.model.CenterScreenModel
import mobi.cwiklinski.bloodline.ui.model.DonationScreenModel
import mobi.cwiklinski.bloodline.ui.model.HomeScreenModel
import mobi.cwiklinski.bloodline.ui.model.LoginScreenModel
import mobi.cwiklinski.bloodline.ui.model.LogoutScreenModel
import mobi.cwiklinski.bloodline.ui.model.ProfileScreenModel
import mobi.cwiklinski.bloodline.ui.model.RegisterScreenModel
import mobi.cwiklinski.bloodline.ui.model.ResetScreenModel
import mobi.cwiklinski.bloodline.ui.model.SetupScreenModel
import mobi.cwiklinski.bloodline.ui.model.SplashScreenModel
import org.koin.dsl.lazyModule
import mobi.cwiklinski.bloodline.auth.filed.createAuthenticationModule as filedAuthModule
import mobi.cwiklinski.bloodline.auth.firebase.createAuthenticationModule as firebaseAuthModule
import mobi.cwiklinski.bloodline.data.filed.createDataModule as filedDataModule
import mobi.cwiklinski.bloodline.data.firebase.createDataModule as firebaseDataModule

val uiModule = buildList {
    addAll(getFirebaseImplementation())
    //addall(filedImplementation())
    add(createStorageModule())
    add(lazyModule {
        // Models
        single<CallbackManager> { AppCallbackManager(get()) }
        single { SplashScreenModel(callbackManager = get(), authService = get()) }
        single { SetupScreenModel(callbackManager = get(), profileService = get(), centerService = get(), storageService = get()) }
        single { HomeScreenModel(callbackManager = get(), profileService = get(), donationService = get(), storageService = get()) }
        single { LoginScreenModel(callbackManager = get(), authService = get(), storageService = get()) }
        single { RegisterScreenModel(callbackManager = get(), authService = get(), profileService = get(), storageService = get()) }
        single { ResetScreenModel(callbackManager = get(), authService = get()) }
        single { ProfileScreenModel(callbackManager = get(), authService = get(), profileService = get(), storageService = get(), centerService = get()) }
        single { DonationScreenModel(callbackManager = get(), donationService = get(), centerService = get(), profileService = get()) }
        single { CenterScreenModel(callbackManager = get(), centerService = get()) }
        single { LogoutScreenModel(callbackManager = get(), authService = get(), storageService = get()) }
    })
}

private fun getFirebaseImplementation() = buildList {
    add(firebaseAuthModule())
    addAll(firebaseDataModule())
}

@SuppressWarnings("unused")
private fun getFiledImplementation() = buildList {
    add(filedAuthModule())
    add(filedDataModule())
}