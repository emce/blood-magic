package mobi.cwiklinski.bloodline.ui.di

import mobi.cwiklinski.bloodline.auth.filed.createAuthenticationModule
import mobi.cwiklinski.bloodline.data.filed.createDataModule
import mobi.cwiklinski.bloodline.storage.datastore.createStorageModule
import mobi.cwiklinski.bloodline.ui.model.LoginScreenModel
import mobi.cwiklinski.bloodline.ui.model.ProfileScreenModel
import mobi.cwiklinski.bloodline.ui.model.RegisterScreenModel
import mobi.cwiklinski.bloodline.ui.model.ResetScreenModel
import mobi.cwiklinski.bloodline.ui.model.SplashScreenModel
import org.koin.core.module.Module
import org.koin.dsl.module

val uiModule = buildList<Module> {
    add(createAuthenticationModule())
    add(createDataModule())
    add(createStorageModule())
    module {
        factory { SplashScreenModel(get()) }
        factory { LoginScreenModel(get(), get(), get()) }
        factory { RegisterScreenModel(get(), get(), get()) }
        factory { ResetScreenModel(get()) }
        factory { ProfileScreenModel(get(), get(), get()) }
    }
}