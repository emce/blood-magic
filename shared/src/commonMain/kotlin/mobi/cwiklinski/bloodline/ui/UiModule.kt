package mobi.cwiklinski.bloodline.ui

import mobi.cwiklinski.bloodline.ui.model.CenterScreenModel
import mobi.cwiklinski.bloodline.ui.model.DonationScreenModel
import mobi.cwiklinski.bloodline.ui.model.ExitScreenModel
import mobi.cwiklinski.bloodline.ui.model.HomeScreenModel
import mobi.cwiklinski.bloodline.ui.model.LoginScreenModel
import mobi.cwiklinski.bloodline.ui.model.NotificationScreenModel
import mobi.cwiklinski.bloodline.ui.model.ProfileScreenModel
import mobi.cwiklinski.bloodline.ui.model.RegisterScreenModel
import mobi.cwiklinski.bloodline.ui.model.ResetScreenModel
import mobi.cwiklinski.bloodline.ui.model.SetupScreenModel
import mobi.cwiklinski.bloodline.ui.model.SplashScreenModel
import org.koin.dsl.module

val uiModule = buildList {
    add(module {
        // Models
        factory {
            SplashScreenModel(
                callbackManager = get(),
                authService = get(),
                storageService = get(),
            )
        }
        factory {
            SetupScreenModel(
                callbackManager = get(),
                profileService = get(),
                centerService = get(),
                storageService = get()
            )
        }
        single {
            HomeScreenModel(
                callbackManager = get(),
                profileService = get(),
                donationService = get(),
                notificationService = get(),
                storageService = get()
            )
        }
        factory {
            LoginScreenModel(
                callbackManager = get(),
                authService = get(),
                storageService = get()
            )
        }
        factory {
            RegisterScreenModel(
                callbackManager = get(),
                authService = get(),
                profileService = get(),
                storageService = get()
            )
        }
        factory { ResetScreenModel(callbackManager = get(), authService = get()) }
        single {
            ProfileScreenModel(
                callbackManager = get(),
                authService = get(),
                profileService = get(),
                storageService = get(),
                centerService = get()
            )
        }
        single {
            DonationScreenModel(
                callbackManager = get(),
                donationService = get(),
                centerService = get(),
                profileService = get()
            )
        }
        single { CenterScreenModel(callbackManager = get(), centerService = get()) }
        factory {
            ExitScreenModel(
                callbackManager = get(),
                authService = get(),
                donationService = get(),
                profileService = get(),
                storageService = get()
            )
        }
        single {
            NotificationScreenModel(
                callbackManager = get(),
                notificationService = get(),
                storageService = get()
            )
        }
    })
}