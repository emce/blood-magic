package mobi.cwiklinski.bloodline.auth.firebase

import mobi.cwiklinski.bloodline.auth.api.AuthenticationInitializer
import mobi.cwiklinski.bloodline.auth.api.AuthenticationService
import org.koin.dsl.lazyModule


fun createAuthenticationModule() = lazyModule {
    factory<AuthenticationService> { AuthenticationServiceImpl(get()) }
    factory<AuthenticationInitializer> { AuthenticationInitializerImpl(get(), get()) }
}