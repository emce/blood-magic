package mobi.cwiklinski.bloodline

import mobi.cwiklinski.bloodline.auth.firebase.createAuthenticationModule
import mobi.cwiklinski.bloodline.data.firebase.createDataModule
import mobi.cwiklinski.bloodline.screen.splash.splashModule
import mobi.cwiklinski.bloodline.storage.datastore.createStorageModule
import org.koin.core.module.Module

val appModule = buildList<Module> {
    createAuthenticationModule()
    createDataModule()
    createStorageModule()
    splashModule
}