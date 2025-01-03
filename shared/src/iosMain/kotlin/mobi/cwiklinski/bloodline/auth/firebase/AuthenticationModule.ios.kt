package mobi.cwiklinski.bloodline.auth.firebase

import mobi.cwiklinski.bloodline.auth.api.AuthenticationInitializer
import mobi.cwiklinski.bloodline.auth.api.AuthenticationService
import org.koin.dsl.lazyModule

actual fun createAuthenticationModule() = lazyModule {
    single<AuthenticationService> { IosAuthenticationServiceImpl(get()) }
    single<AuthenticationInitializer> { AuthenticationInitializerImpl(get(), get()) }
}