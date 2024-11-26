package mobi.cwiklinski.bloodline.auth.firebase

import mobi.cwiklinski.bloodline.auth.api.AuthenticationService
import org.koin.dsl.module


fun createAuthenticationModule() = module {
    factory<AuthenticationService> { AuthenticationServiceImpl(get()) }
}