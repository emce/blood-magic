package mobi.cwiklinski.bloodline.ui.di

import mobi.cwiklinski.bloodline.auth.firebase.createAuthenticationModule
import mobi.cwiklinski.bloodline.data.firebase.createDataModule
import mobi.cwiklinski.bloodline.storage.datastore.createStorageModule
import mobi.cwiklinski.bloodline.ui.screen.splash.splashModule
import org.koin.core.module.Module

val uiModule = buildList<Module> {
    addAll(createAuthenticationModule())
    add(createDataModule())
    add(createStorageModule())
    splashModule
}