package mobi.cwiklinski.bloodline.auth.firebase

import mobi.cwiklinski.bloodline.auth.api.DesktopMainAuthenticationService
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun createAuthenticationModule(): List<Module> = buildList {
    add(element = module { single<DesktopMainAuthenticationService>{ DesktopMainAuthenticationServiceImpl() } })
    add(element = commonModule)
}