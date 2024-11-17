package mobi.cwiklinski.bloodline.notification.fcm

import mobi.cwiklinski.bloodline.notification.api.DesktopNotificationService
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun createNotificationModule(): List<Module> = buildList {
    add(element = module { single<DesktopNotificationService>{ DesktopNotificationServiceImpl() } })
    add(element = commonModule)
}