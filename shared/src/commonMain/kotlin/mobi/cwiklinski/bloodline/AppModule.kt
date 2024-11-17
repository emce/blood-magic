package mobi.cwiklinski.bloodline

import mobi.cwiklinski.bloodline.notification.fcm.createNotificationModule
import mobi.cwiklinski.bloodline.ui.di.uiModule
import org.koin.core.module.Module

val appModule: List<Module> = buildList {
    addAll(createNotificationModule())
    addAll(uiModule)
}