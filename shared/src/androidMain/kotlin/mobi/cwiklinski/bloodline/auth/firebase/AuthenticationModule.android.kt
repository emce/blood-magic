package mobi.cwiklinski.bloodline.auth.firebase

import mobi.cwiklinski.bloodline.auth.api.AuthenticationInitializer
import mobi.cwiklinski.bloodline.auth.api.AuthenticationService
import org.koin.dsl.module

actual fun createAuthenticationModule() = module {
    single<AuthenticationService> { AndroidAuthenticationServiceImpl(get(), get(), get()) }
    single<AuthenticationInitializer> { AndroidAuthenticationInitializer() }
}