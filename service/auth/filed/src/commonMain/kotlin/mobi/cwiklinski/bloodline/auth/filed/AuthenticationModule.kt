package mobi.cwiklinski.bloodline.auth.filed

import mobi.cwiklinski.bloodline.auth.api.AuthenticationService
import org.koin.dsl.module

fun createAuthenticationModule() = module {
    single<AuthenticationService> { AuthenticationServiceImpl(get()) }
}