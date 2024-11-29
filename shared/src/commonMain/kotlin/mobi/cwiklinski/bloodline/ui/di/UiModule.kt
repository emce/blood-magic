package mobi.cwiklinski.bloodline.ui.di

import mobi.cwiklinski.bloodline.auth.firebase.createAuthenticationModule as firebaseAuthModule
import mobi.cwiklinski.bloodline.data.firebase.createDataModule as firebaseDataModule
import mobi.cwiklinski.bloodline.auth.filed.createAuthenticationModule as filedAuthModule
import mobi.cwiklinski.bloodline.data.filed.createDataModule as filedDataModule
import mobi.cwiklinski.bloodline.storage.datastore.createStorageModule
import mobi.cwiklinski.bloodline.ui.model.CenterScreenModel
import mobi.cwiklinski.bloodline.ui.model.DonationScreenModel
import mobi.cwiklinski.bloodline.ui.model.HomeScreenModel
import mobi.cwiklinski.bloodline.ui.model.LoginScreenModel
import mobi.cwiklinski.bloodline.ui.model.ProfileScreenModel
import mobi.cwiklinski.bloodline.ui.model.RegisterScreenModel
import mobi.cwiklinski.bloodline.ui.model.ResetScreenModel
import mobi.cwiklinski.bloodline.ui.model.SetupScreenModel
import mobi.cwiklinski.bloodline.ui.model.SplashScreenModel
import org.koin.core.module.Module
import org.koin.dsl.lazyModule
import org.koin.dsl.module

val uiModule = buildList {
    addAll(getFirebaseImplementation())
    //addall(filedImplementation())
    add(createStorageModule())
    add(lazyModule {
        // Models
        factory { SplashScreenModel(authService = get()) }
        factory { SetupScreenModel(profileService = get(), centerService = get(), storageService = get()) }
        factory { HomeScreenModel(profileService = get(), donationService = get(), storageService = get()) }
        factory { LoginScreenModel(authService = get(), profileService = get(), storageService = get()) }
        factory { RegisterScreenModel(authService = get(), profileService = get(), storageService = get()) }
        factory { ResetScreenModel(authService = get()) }
        factory { ProfileScreenModel(authService = get(), profileService = get(), storageService = get(), centerService = get()) }
        factory { DonationScreenModel(donationService = get(), centerService = get()) }
        factory { CenterScreenModel(centerService = get()) }
    })
}

private fun getFirebaseImplementation() = buildList {
    add(firebaseAuthModule())
    addAll(firebaseDataModule())
}

private fun getFiledImplementation() = buildList {
    add(filedAuthModule())
    add(filedDataModule())
}