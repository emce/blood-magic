package mobi.cwiklinski.bloodline

import mobi.cwiklinski.bloodline.ui.di.uiModule
import org.koin.core.module.Module

expect fun createAppModule(): List<Module>

val appModule: List<Module> = buildList {
    //addAll(createNotificationModule())
    addAll(uiModule)
}