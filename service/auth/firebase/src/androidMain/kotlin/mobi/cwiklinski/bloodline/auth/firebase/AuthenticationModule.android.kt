@file:JvmName("AndroidAuthenticationModuleUnique")

package mobi.cwiklinski.bloodline.auth.firebase

import mobi.cwiklinski.bloodline.auth.api.AndroidAuthenticationService
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun createAuthenticationModule(): List<Module> = buildList {
    add(element = module { single<AndroidAuthenticationService>{ AndroidAuthenticationServiceImpl() } })
    add(element = commonModule)
}