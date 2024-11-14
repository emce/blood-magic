package mobi.cwiklinski.bloodline.auth.firebase

import mobi.cwiklinski.bloodline.auth.api.NativeMainAuthenticationService
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun createAuthenticationModule(): List<Module> = buildList {
    add(element = module { single<NativeMainAuthenticationService>{ NativeMainAuthenticationServiceImpl() } })
    add(element = commonModule)
}