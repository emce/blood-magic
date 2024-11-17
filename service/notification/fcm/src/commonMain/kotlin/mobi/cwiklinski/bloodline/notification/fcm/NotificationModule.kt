package mobi.cwiklinski.bloodline.notification.fcm

import mobi.cwiklinski.bloodline.notification.api.NotificationService
import org.koin.core.module.Module
import org.koin.dsl.module

expect fun createNotificationModule(): List<Module>

val commonModule: Module = module { single<NotificationService> { NotificationServiceImpl() } }