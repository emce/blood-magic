package mobi.cwiklinski.bloodline

import mobi.cwiklinski.bloodline.ui.uiModule
import org.koin.core.module.LazyModule
import org.koin.core.module.Module

expect fun createAppModule(): List<Module>

expect fun createAppLazyModule(): List<LazyModule>

val appModule = buildList {
    //addAll(createNotificationModule())
    addAll(uiModule)
}