package mobi.cwiklinski.bloodline.notification.fcm

import mobi.cwiklinski.bloodline.notification.api.NativeNotificationService
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun createNotificationModule(): List<Module> = buildList {
    add(element = module { single<NativeNotificationService>{ NativeNotificationServiceImpl() } })
    add(element = commonModule)
}